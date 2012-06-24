/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.open_groups.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.UserError;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.DataTranslationUtils;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.constants.ApplicationConfigParam;
import ro.zg.opengroups.constants.OpenGroupsExceptions;
import ro.zg.opengroups.constants.TypeRelationConfigParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityLink;
import ro.zg.opengroups.vo.EntityList;
import ro.zg.opengroups.vo.Tag;
import ro.zg.opengroups.vo.User;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;
import ro.zg.util.logging.MasterLogManager;

public class OpenGroupsModel {

    private static final String GET_ENTITY_INFO_FLOW = "ro.problems.flows.get-entity-info-by-id";
    private static final String GET_HIERARCHY_MAX_DEPTH = "ro.problems.flows.get-hierarchy-max-depth";
    private static final String GET_CAUSAL_HIERARCHY = "ro.problems.flows.get-entities-list";
    private static final String GET_CAUSES = "ro.problems.flows.get-all-causes-for-entity";
    private static final String LOGIN_WITH_OPENID = "ro.problems.flows.login-with-openid";

    private static OpenGroupsModel instance;

    private OpenGroupsModel() {

    }

    public static OpenGroupsModel getInstance() {
	if (instance == null) {
	    instance = new OpenGroupsModel();
	}
	return instance;
    }

    private ActionsManager getActionsManager() {
	return ActionsManager.getInstance();
    }

    private ApplicationConfigManager getAppConfigManager() {
	return ApplicationConfigManager.getInstance();
    }

    protected String getMessage(String key) {
	return OpenGroupsResources.getMessage(key);
    }

    public Entity getRootEntity() throws ContextAwareException {
	Entity e = new Entity((Long) getAppConfigManager().getApplicationConfigParam(
		ApplicationConfigParam.DEFAULT_ENTITY_ID));
	refreshEntity(e, null);
	// String caption = getMessage("metagroup.caption");
	// e.setTitle(caption);
	return e;
    }

    public void refreshEntity(Entity entity, Long userId) throws ContextAwareException {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("entityId", entity.getId());
	params.put("userId", userId);
	EntityLink selectedCause = entity.getSelectedCause();
	if (selectedCause != null) {
	    params.put("parentLinkId", selectedCause.getLinkId());
	}
	CommandResponse response = getActionsManager().execute(GET_ENTITY_INFO_FLOW, params);
	GenericNameValueList result = (GenericNameValueList) response.getValue("result");
	GenericNameValueContext row = (GenericNameValueContext) result.getValueForIndex(0);
	// selectedEntity.setComplexType(row.getValue("complex_type").toString());
	/* get the tags */
	if (row == null) {
	    throw OpenGroupsExceptions.getNoSuchEntityException(entity.getId());
	}
	entity.update(row);
	entity.setSelectedCause(selectedCause);
	GenericNameValueList tagsResult = (GenericNameValueList) response.getValue("tags");
	entity.setTags(Tag.getTagsList(tagsResult));

    }

    public EntityList getChildrenListForEntity(Entity entity, UserAction ua, Long userId) {
	Map<String, Object> params = ua.getActionParams();
	params.putAll(entity.getFilterValues());
	params.put("pageNumber", entity.getState().getCurrentPageForCurrentAction());
	params.put("itemsOnPage", entity.getState().getItemsPerPage());
	params.put("parentId", entity.getId());
	String complexEntityType = ua.getTargetEntityComplexType();
	boolean showEntityType = true;
	if (!"*".equals(complexEntityType)) {
	    params.put("entityType", complexEntityType);
	    showEntityType = false;
	}
	// params.put("withContent",
	// getAppConfigManager().getComplexEntityBooleanParam(complexEntityType,
	// ComplexEntityParam.LIST_WITH_CONTENT));
	params.put(
		"withContent",
		getAppConfigManager().getTypeRelationBooleanConfigParam(ua.getTypeRelationId(),
			TypeRelationConfigParam.LIST_WITH_CONTENT));
	params.put("userId", userId);
	// if
	// (!getAppConfigManager().getComplexEntityBooleanParam(complexEntityType,
	// ComplexEntityParam.ALLOW_RECURSIVE_LIST)) {
	if (!getAppConfigManager().getTypeRelationBooleanConfigParam(ua.getTypeRelationId(),
		TypeRelationConfigParam.ALLOW_RECURSIVE_LIST)) {
	    params.put("depth", 0);
	}

	CommandResponse response = getActionsManager().execute(ua.getAction(), params);
	GenericNameValueList list = (GenericNameValueList) response.getValue("result");
	Object totalItemsCount = response.getValue("totalItemsCount");
	if (totalItemsCount != null) {
	    entity.getState().setCurrentListTotalItemsCount(Integer.parseInt(totalItemsCount.toString()));
	}
	
	return new EntityList(list, showEntityType);
    }

    public EntityList getHierarchyList(Entity entity, UserAction ua, Long userId) {
	Map<String, Object> params = ua.getActionParams();

	params.put("pageNumber", 1);
	/* we need bring all the hierarchy and display it on the same page */
	params.put("itemsOnPage", 5000);
	params.put("entityId", entity.getId());
	params.put("withContent", true);
	params.put("userId", userId);

	CommandResponse response = getActionsManager().execute(ua.getAction(), params);
	GenericNameValueList list = (GenericNameValueList) response.getValue("result");
	/* remove current entity from the hierarchy */
	list.removeValueForIndex(list.size() - 1);
	return new EntityList(list, true);

    }

    public long getHierarchyMaxDepth(long rootNodeId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("parentId", rootNodeId);
	CommandResponse response = getActionsManager().execute(GET_HIERARCHY_MAX_DEPTH, params);
	Object maxDepth = response.getValue("max_depth");
	if (maxDepth != null) {
	    return Long.parseLong(maxDepth.toString());
	}
	return 0;
    }

    public EntityList getCausalHierarchy(long rootNodeId, int startDepth, int cacheDepth) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("parentId", rootNodeId);
	params.put("startDepth", startDepth);
	params.put("depth", startDepth + cacheDepth);
	params.put("withContent", false);
	params.put("withoutLeafTypes", true);
	params.put("sortList", "depth");
	params.put("pageNumber", 1);
	params.put("itemsOnPage", 5000);

	CommandResponse response = getActionsManager().execute(GET_CAUSAL_HIERARCHY, params);
	GenericNameValueList list = (GenericNameValueList) response.getValue("result");
	return new EntityList(list, false);
    }

    public void populateCauses(Entity entity) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("entityId", entity.getId());

	CommandResponse response = getActionsManager().execute(GET_CAUSES, params);

	if (!response.isSuccessful()) {
	    MasterLogManager.getLogger("OPEN-GROUPS")
		    .error("Failed to get causes for entity " + entity.getId() + ". Error code: "
			    + response.getResponseCode());
	    return;
	}

	GenericNameValueList list = (GenericNameValueList) response.getValue("result");

	if (list != null) {
	    List<EntityLink> causes = new ArrayList<EntityLink>();
	    for (int i = 0; i < list.size(); i++) {
		GenericNameValueContext context = (GenericNameValueContext) list.getValueForIndex(i);
		causes.add(new EntityLink(context));
	    }
	    entity.setCauses(causes);

	    if (entity.getSelectedCause() == null && causes.size() > 0) {
		entity.setSelectedCause(causes.get(0));
	    }
	}
    }

    public User loginWithOpenId(String email, String ip) {
	Map<String, Object> paramsMap = new HashMap<String, Object>();
	paramsMap.put("email", email);
	paramsMap.put("ip", ip);

	CommandResponse response = getActionsManager().execute(LOGIN_WITH_OPENID, paramsMap);
	GenericNameValue result = (GenericNameValue) response.get("result");
	GenericNameValueList list = (GenericNameValueList) result.getValue();
	
	/* so we have a user */
	GenericNameValueContext userRow = (GenericNameValueContext) list.getValueForIndex(0);
	User user = getUserFromParamsContext(userRow);
	return user;
    }

    public User getUserFromParamsContext(GenericNameValueContext userRow) {
	User user = new User();
	String username = (String) userRow.getValue("username");
	long userId = (Long) userRow.getValue("id");
	Timestamp lastLogin = (Timestamp) userRow.getValue("last_login");
	String email = (String) userRow.getValue("email");
	user.setUserId(userId);
	if (username != null) {
	    user.setUsername(username);
	} else {
	    user.setUsername(email);
	}
	user.setLastLogin(lastLogin);
	user.setEmail(email);
	return user;
    }
}
