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

import java.util.HashMap;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;
import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.opengroups.constants.ComplexEntityParam;
import ro.zg.opengroups.constants.ExceptionTypes;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityList;
import ro.zg.opengroups.vo.Tag;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

public class OpenGroupsModel {

	private static final String GET_ENTITY_INFO_FLOW = "ro.problems.flows.get-entity-info-by-id";

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

	public void refreshEntity(Entity entity, Long userId)
			throws ContextAwareException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entityId", entity.getId());
		params.put("userId", userId);
		CommandResponse response = getActionsManager().execute(
				GET_ENTITY_INFO_FLOW, params);
		GenericNameValueList result = (GenericNameValueList) response
				.getValue("result");
		GenericNameValueContext row = (GenericNameValueContext) result
				.getValueForIndex(0);
		// selectedEntity.setComplexType(row.getValue("complex_type").toString());
		/* get the tags */
		if (row == null) {
			ExceptionContext ec = new ExceptionContext();
			ec.put("ENTITY_ID", entity.getId());
			throw new ContextAwareException(ExceptionTypes.NO_SUCH_ENTITY, ec);
		}
		entity.update(row);
		GenericNameValueList tagsResult = (GenericNameValueList) response
				.getValue("tags");
		entity.setTags(Tag.getTagsList(tagsResult));

	}

	public EntityList getChildrenListForEntity(Entity entity, UserAction ua,
			Long userId) {
		Map<String, Object> params = ua.getActionParams();
		params.putAll(entity.getFilterValues());
		params.put("pageNumber", entity.getState()
				.getCurrentPageForCurrentAction());
		params.put("itemsOnPage", entity.getState().getItemsPerPage());
		params.put("parentId", entity.getId());
		String complexEntityType = ua.getTargetEntityComplexType();
		boolean showEntityType = true;
		if (!"*".equals(complexEntityType)) {
			params.put("entityType", complexEntityType);
			showEntityType = false;
		}
		params.put("withContent", getAppConfigManager()
				.getComplexEntityBooleanParam(complexEntityType,
						ComplexEntityParam.LIST_WITH_CONTENT));
		params.put("userId", userId);
		if (!getAppConfigManager().getComplexEntityBooleanParam(
				complexEntityType, ComplexEntityParam.ALLOW_RECURSIVE_LIST)) {
			params.put("depth", 0);
		}

		CommandResponse response = getActionsManager().execute(ua.getAction(),
				params);
		GenericNameValueList list = (GenericNameValueList) response
				.getValue("result");
		Object totalItemsCount = response.getValue("totalItemsCount");
		if (totalItemsCount != null) {
			entity.getState().setCurrentListTotalItemsCount(
					Integer.parseInt(totalItemsCount.toString()));
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

		CommandResponse response = getActionsManager().execute(ua.getAction(),
				params);
		GenericNameValueList list = (GenericNameValueList) response
				.getValue("result");
		/* remove current entity from the hierarchy */
		list.removeValueForIndex(list.size() - 1);
		return new EntityList(list, true);

	}
}
