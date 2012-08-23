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
package ro.zg.opengroups.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.netcell.vaadin.DefaultFormController;
import ro.zg.netcell.vaadin.ExtendedForm;
import ro.zg.netcell.vaadin.FormContext;
import ro.zg.netcell.vaadin.FormController;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.netcell.vaadin.action.FormGenerator;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.netcell.vaadin.action.application.CreateEntityHandler;
import ro.zg.netcell.vaadin.action.application.EntityListHandler;
import ro.zg.netcell.vaadin.action.application.OpenHierarchyForEntityHandler;
import ro.zg.netcell.vaadin.action.application.SetEntityPriorityHandler;
import ro.zg.netcell.vaadin.action.application.SetEntityStatusHandler;
import ro.zg.netcell.vaadin.action.application.UpdateEntityHandler;
import ro.zg.netcell.vaadin.action.application.UserNotificationRulesHandler;
import ro.zg.netcell.vaadin.action.application.VoteEntityHandler;
import ro.zg.netcell.vaadin.action.constants.ActionParamProperties;
import ro.zg.netcell.vaadin.action.user.LoginHandler;
import ro.zg.netcell.vaadin.action.user.LogoutHandler;
import ro.zg.netcell.vaadin.action.user.RegisterUserHandler;
import ro.zg.netcell.vaadin.action.user.RequestPasswordResetHandler;
import ro.zg.netcell.vaadin.action.user.ResetPasswordHandler;
import ro.zg.netcell.vaadin.action.user.UpdateUserHandler;
import ro.zg.netcell.vo.InputParameter;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ActionTypes;
import ro.zg.opengroups.forms.controllers.CreateEntityFormController;
import ro.zg.opengroups.forms.controllers.UpdateEntityFormController;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.ObjectsUtil;
import ro.zg.util.parser.utils.ParseUtils;

import com.vaadin.ui.ComponentContainer;

public class UserAction implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6392501241895451134L;

    // protected ActionsManager actionsManager = ActionsManager.getInstance();

    // private static Map<String, String> actionsDisplayNames;
    // private static Map<String, String> targetEntityDisplayNames;
    // private static Map<String, String> actionTargetDisplayNames;
    private static Map<String, OpenGroupsActionHandler> actionsHandlers;
    private static Map<String, OpenGroupsActionHandler> actionsHandlersByName;
    private static FormController defaultFormController = new DefaultFormController();
    
    private static Map<String,FormController> formControllersByName;
    static {
	/*
	 * actionsDisplayNames = new HashMap<String, String>(); targetEntityDisplayNames = new HashMap<String,
	 * String>(); actionTargetDisplayNames = new HashMap<String, String>();
	 * 
	 * actionsDisplayNames.put("entity.list.newest", "Newest"); actionsDisplayNames.put("entity.list.most_popular",
	 * "Most popular"); actionsDisplayNames.put("entity.list.by.my.priority", "My priorities");
	 * actionsDisplayNames.put("entity.list.by.global.priority", "Global priorities");
	 * actionsDisplayNames.put("user.login", "Login"); actionsDisplayNames.put("user.register", "Register");
	 * actionsDisplayNames.put("user.logout", "Logout"); actionsDisplayNames.put("entity.vote", "Vote");
	 * actionsDisplayNames.put("entity.update", "Update"); actionsDisplayNames.put("entity.upstream.hierarchy",
	 * "Show hierarchy");
	 * 
	 * targetEntityDisplayNames.put("SOLUTION", "Solutions"); targetEntityDisplayNames.put("COMMENT", "Comments");
	 * 
	 * actionTargetDisplayNames.put("entity.create.with_tags/ISSUE", "Create issue");
	 * actionTargetDisplayNames.put("entity.create/COMMENT", "Add comment");
	 * actionTargetDisplayNames.put("entity.create/SOLUTION", "Propose solution");
	 * actionTargetDisplayNames.put("entity.list.recent.activity/*", "Recent activity");
	 */
	actionsHandlers = new HashMap<String, OpenGroupsActionHandler>();
	actionsHandlers.put("ro.problems.flows.login", new LoginHandler());
	actionsHandlers.put("ro.problems.flows.logout", new LogoutHandler());
	actionsHandlers.put("ro.problems.flows.create-user", new RegisterUserHandler());
	actionsHandlers.put("ro.problems.flows.get-entities-list", new EntityListHandler());
	actionsHandlers.put("ro.problems.flows.create-entity-with-tags", new CreateEntityHandler());
	actionsHandlers.put("ro.problems.flows.create-entity", new CreateEntityHandler());
	// actionsHandlers.put("ro.problems.flows.vote-entity", new
	// VoteEntityHandler());
	actionsHandlers.put("ro.problems.flows.update-entity", new UpdateEntityHandler());

	actionsHandlersByName = new HashMap<String, OpenGroupsActionHandler>();
	actionsHandlersByName.put("entity.upstream.hierarchy", new OpenHierarchyForEntityHandler());
	actionsHandlersByName.put("entity.vote", new VoteEntityHandler());
	actionsHandlersByName.put("entity.set.priority", new SetEntityPriorityHandler());
	actionsHandlersByName.put("entity.set.status", new SetEntityStatusHandler());
	actionsHandlersByName.put("user.update.data", new UpdateUserHandler());
	actionsHandlersByName.put("user.request.password.reset", new RequestPasswordResetHandler());
	actionsHandlersByName.put("user.reset.password", new ResetPasswordHandler());
	actionsHandlersByName.put("user.notification.rules.read", new UserNotificationRulesHandler());
	
	
	formControllersByName=new HashMap<String, FormController>();
	CreateEntityFormController createEntityFormController = new CreateEntityFormController();
	formControllersByName.put("entity.create", createEntityFormController);
	formControllersByName.put("entity.create.with_tags", createEntityFormController);
	formControllersByName.put("entity.update", new UpdateEntityFormController());
    }

    private String action;
    private String actionName;
    private Map<String, Object> actionParams;
    /**
     * the parameters that need to be gathered from the user these parameters are defined in the actions table as:
     * <param name>=? the question mark means that the parameter needs to be collected from the user
     */
    private List<String> userInputParamNames;
    /**
     * correspondent of the action_target column in action_strategies table this will have the form
     * actionLocation:actionPath
     */
    private String actionTarget;
    private String displayName;
    private String targetEntityType;
    private String sourceEntityComplexType;
    private String targetEntityComplexType;
    private long sourceComplexTypeId;
    private long targetComplexTypeId;
    private long typeRelationId;
    private String userType;
    private String actionType;
    private String actionLocation;
    private String actionPath;
    private long orderIndex;
    /**
     * this will have the format {@link #sourceEntityComplexType}: {@link #actionLocation} will be used to group the
     * actions based on source entity complex type and the location where the action will be displayed in the gui
     */
    private String sourceEntityActionLocation;
    private boolean allowReadToAll;
    private OpenGroupsActionHandler actionHandler;
    private FormGenerator formGenerator;
    
    List<Map<String, String>> formParamsList;
    private FormController formController;
    
    public UserAction() {

    }

    public UserAction(GenericNameValueContext contextMap) {
	action = (String) contextMap.getValue("action");
	actionTarget = (String) contextMap.getValue("action_target");
	targetEntityType = (String) contextMap.getValue("target_entity_type");
	actionName = (String) contextMap.getValue("action_name");
	sourceEntityComplexType = (String) contextMap.getValue("source_entity_complex_type");
	targetEntityComplexType = (String) contextMap.getValue("target_entity_complex_type");
	userType = (String) contextMap.getValue("user_type");
	actionType = (String) contextMap.getValue("action_type");
	orderIndex = (Long) contextMap.getValue("order_index");
	initActionLocationAndActionPath(actionTarget);
	// initParamsFromString((String) contextMap.getValue("action_params"));
	initActionParamsFromString((String) contextMap.getValue("action_params"));
	sourceEntityActionLocation = sourceEntityComplexType + ":" + actionLocation;
	allowReadToAll = (Boolean) contextMap.getValue("allow_read_to_all");
	actionHandler = actionsHandlersByName.get(actionName);
	if (actionHandler == null) {
	    actionHandler = actionsHandlers.get(action);
	}
	if (actionHandler != null) {
	    actionHandler.setUserAction(this);
	}
	sourceComplexTypeId = Long.parseLong(contextMap.getValue("source_complex_type_id").toString());
	targetComplexTypeId = Long.parseLong(contextMap.getValue("target_complex_type_id").toString());
	typeRelationId = Long.parseLong(sourceComplexTypeId + "" + targetComplexTypeId);
	initDisplayName();
    }

//    public boolean checkUserAllowedToExecuteAction(Entity entity, OpenGroupsApplication app, UserAction ua) {
//	List<String> currentUserTypes = UsersManager.getInstance().getCurrentUserTypes(entity, app);
//	return currentUserTypes.contains(ua.getUserType());
//    }

    public void executeHandler(Entity selectedEntity, OpenGroupsApplication app, ComponentContainer targetContainer,
	    boolean runInSeparateThread, ActionContext ac) {
	/* set this actions as active on current selected entity */
	if (selectedEntity != null) {
	    selectedEntity.getState().setActionActive(actionName);
	}
	ActionsManager.getInstance().executeHandler(getActionHandler(), this, selectedEntity, app, targetContainer,
		runInSeparateThread, ac);
    }

    public void executeHandler(Entity entity, OpenGroupsApplication app, ComponentContainer targetContainer,
	    ActionContext ac) {
	/* run the handler in a separate thread */
	executeHandler(entity, app, targetContainer, false, ac);
    }

    public void executeHandler(OpenGroupsApplication app, Map<String, Object> params) {
	ActionContext ac = new ActionContext(this, app, null, params);
	ac.setWindow(app.getActiveWindow());
	ActionsManager.getInstance().executeHandler(getActionHandler(), ac);
    }

//    public boolean allowRead(List<String> userTypes) {
//	if (allowReadToAll) {
//	    return true;
//	}
//	return userTypes.contains(userType);
//    }

    public boolean isVisible(ActionContext ac) {
	return (arePreconditionsMet(ac) && allowRead(ac));
    }

    public boolean allowRead(ActionContext ac) {
	if (allowReadToAll) {
	    return true;
	}
	return ac.isUserTypePresent(userType);
    }

    public boolean allowExecute(ActionContext ac) {
	return ac.isUserTypePresent(userType);
    }

    private boolean arePreconditionsMet(ActionContext ac) {
	if (ActionTypes.VOTE.equals(actionType)) {
	    Entity e = ac.getEntity();
	    return (e.getSelectedCause() != null || e.getAbsoluteDepth()==0);
	}
	return true;
    }

    public ExtendedForm generateForm(ActionContext actionContext) {
//	if (formGenerator != null) {
//	    return formGenerator.generate();
//	}
	if(formController != null){
	    FormContext formContext = new FormContext(actionName, formParamsList, actionContext);
	    return formController.createForm(formContext);
	}
	
	return null;
    }

    public List<InputParameter> getUserInputParamsList(List<InputParameter> actionParams) {
	if (userInputParamNames.size() == 0) {
	    return actionParams;
	}
	List<InputParameter> userInputParams = new ArrayList<InputParameter>();
	for (InputParameter uip : actionParams) {
	    if (userInputParamNames.contains(uip.getName())) {
		userInputParams.add(uip);
	    }
	}
	return userInputParams;
    }

    public List<InputParameter> getUserInputParamsList(List<InputParameter> actionParams, Map<String, Object> values) {
	if (userInputParamNames.size() == 0) {
	    return actionParams;
	}
	List<InputParameter> userInputParams = new ArrayList<InputParameter>();
	for (InputParameter uip : actionParams) {
	    if (userInputParamNames.contains(uip.getName())) {
		InputParameter p = (InputParameter) ObjectsUtil.copy(uip);
		p.setValue(values.get(p.getName()));
		userInputParams.add(p);
	    }
	}
	return userInputParams;
    }

    public String getFullActionPath() {
	if (actionPath != null) {
	    return actionPath + "/" + actionName;
	}
	return actionName;
    }

    private void initDisplayName() {
	displayName = OpenGroupsResources.getMessage(actionName);

	if (displayName == null) {
	    displayName = OpenGroupsResources.getMessage(actionName + "/" + targetEntityType);
	}
	// if (displayName == null) {
	// System.out.println("displayname for "+actionName+" : "+action+" : "+targetEntityType);
	// // displayName = targetEntityDisplayNames.get(targetEntityType);
	// }
    }

    private void initParamsFromString(String paramsString) {
	actionParams = new HashMap<String, Object>();
	userInputParamNames = new ArrayList<String>();

	if (paramsString != null) {
	    String[] paramsArray = paramsString.trim().split("\\|");
	    for (String param : paramsArray) {
		if ("".equals(param.trim())) {
		    continue;
		}
		String[] pa = param.split("=");
		String name = pa[0].trim();
		String value = pa[1].trim();
		if ("".equals(value)) {
		    value = null;
		} else if ("?".equals(value)) {
		    userInputParamNames.add(name);
		    value = null;
		}
		actionParams.put(name, value);
	    }
	}
    }

    private void initActionParamsFromString(String s) {
	actionParams = new HashMap<String, Object>();
	if (s == null) {
	    return;
	}
	List<Map<String, String>> paramsList = null;
	try {
	    paramsList = (List<Map<String, String>>) ParseUtils.parseCollection(s);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	formParamsList = new ArrayList<Map<String, String>>();
	for (Map<String, String> paramConfig : paramsList) {
	    String value = paramConfig.get(ActionParamProperties.VALUE);
	    if (value != null && !"".equals(value.trim())) {
		String name = paramConfig.get(ActionParamProperties.NAME);
		actionParams.put(name, value);
	    }
	    if ("true".equals(paramConfig.get(ActionParamProperties.IS_FORM_FIELD))) {
		formParamsList.add(paramConfig);
	    }
	}
	if (formParamsList.size() > 0) {
//	    formGenerator = new FormGenerator(actionName, formParamsList, OpenGroupsResources.getBundle());
	    formController = formControllersByName.get(actionName);
	    if(formController == null){
		formController=defaultFormController;
	    }
	    
	}
    }

    private void initActionLocationAndActionPath(String actionTarget) {
	if (actionTarget == null) {
	    return;
	}
	int index = actionTarget.indexOf(":");
	if (index > 0) {
	    actionLocation = actionTarget.substring(0, index);
	    actionPath = actionTarget.substring(index + 1);
	    if ("".equals(actionPath.trim())) {
		actionPath = null;
	    }
	} else {
	    actionLocation = actionTarget;
	}
    }

    /**
     * @return the action
     */
    public String getAction() {
	return action;
    }

    /**
     * @return the actionTarget
     */
    public String getActionTarget() {
	return actionTarget;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
	return displayName;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(String action) {
	this.action = action;
    }

    /**
     * @param actionTarget
     *            the actionTarget to set
     */
    public void setActionTarget(String actionTarget) {
	this.actionTarget = actionTarget;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    /**
     * @return the actionHandler
     */
    public OpenGroupsActionHandler getActionHandler() {
	return actionHandler;
    }

    /**
     * @param actionHandler
     *            the actionHandler to set
     */
    public void setActionHandler(OpenGroupsActionHandler actionHandler) {
	this.actionHandler = actionHandler;
    }

    /**
     * @return the targetEntityType
     */
    public String getTargetEntityType() {
	return targetEntityType;
    }

    /**
     * @param targetEntityType
     *            the targetEntityType to set
     */
    public void setTargetEntityType(String targetEntityType) {
	this.targetEntityType = targetEntityType;
    }

    /**
     * @return the actionName
     */
    public String getActionName() {
	return actionName;
    }

    /**
     * @return the actionParams
     */
    public Map<String, Object> getActionParams() {
	return (Map<String, Object>) ObjectsUtil.copy(actionParams);
    }

    /**
     * @param actionName
     *            the actionName to set
     */
    public void setActionName(String actionName) {
	this.actionName = actionName;
    }

    /**
     * @return the userInputParamNames
     */
    public List<String> getUserInputParamNames() {
	return userInputParamNames;
    }

    /**
     * @return the sourceEntityComplexType
     */
    public String getSourceEntityComplexType() {
	return sourceEntityComplexType;
    }

    /**
     * @return the targetEntityComplexType
     */
    public String getTargetEntityComplexType() {
	return targetEntityComplexType;
    }

    /**
     * @return the userType
     */
    public String getUserType() {
	return userType;
    }

    /**
     * @return the actionLocation
     */
    public String getActionLocation() {
	return actionLocation;
    }

    /**
     * @return the actionPath
     */
    public String getActionPath() {
	return actionPath;
    }

    /**
     * @return the sourceEntityActionLocation
     */
    public String getSourceEntityActionLocation() {
	return sourceEntityActionLocation;
    }

    /**
     * @param sourceEntityActionLocation
     *            the sourceEntityActionLocation to set
     */
    public void setSourceEntityActionLocation(String sourceEntityActionLocation) {
	this.sourceEntityActionLocation = sourceEntityActionLocation;
    }

    /**
     * @return the allowReadToAll
     */
    public boolean isAllowReadToAll() {
	return allowReadToAll;
    }

    /**
     * @param actionPath
     *            the actionPath to set
     */
    protected void setActionPath(String actionPath) {
	this.actionPath = actionPath;
    }

    /**
     * @return the sourceComplexTypeId
     */
    public long getSourceComplexTypeId() {
	return sourceComplexTypeId;
    }

    /**
     * @return the targetComplexTypeId
     */
    public long getTargetComplexTypeId() {
	return targetComplexTypeId;
    }

    /**
     * @return the typeRelationId
     */
    public long getTypeRelationId() {
	return typeRelationId;
    }

    /**
     * @param sourceComplexTypeId
     *            the sourceComplexTypeId to set
     */
    public void setSourceComplexTypeId(long sourceComplexTypeId) {
	this.sourceComplexTypeId = sourceComplexTypeId;
    }

    /**
     * @param targetComplexTypeId
     *            the targetComplexTypeId to set
     */
    public void setTargetComplexTypeId(long targetComplexTypeId) {
	this.targetComplexTypeId = targetComplexTypeId;
    }

    /**
     * @param typeRelationId
     *            the typeRelationId to set
     */
    public void setTypeRelationId(long typeRelationId) {
	this.typeRelationId = typeRelationId;
    }

    /**
     * @return the actionType
     */
    public String getActionType() {
	return actionType;
    }

    /**
     * @param actionType
     *            the actionType to set
     */
    public void setActionType(String actionType) {
	this.actionType = actionType;
    }

    /**
     * @return the orderIndex
     */
    public long getOrderIndex() {
        return orderIndex;
    }

}
