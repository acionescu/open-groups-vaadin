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
package ro.zg.netcell.vaadin.action;

import java.util.List;
import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.open_groups.model.OpenGroupsModel;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Window;

public abstract class OpenGroupsActionHandler implements ActionHandler<OpenGroupsApplication> {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -5836804822276394224L;

    /* constants */
    public static String GET_POSSIBLE_ACTIONS = "ro.problems.flows.get-actions-by-entity-and-user";
    
    
    private UserAction userAction;
    
    public OpenGroupsActionHandler() {
	
    }
    
    protected UserActionList getAvailableActions(Entity e, String actionLocation) {
	return getActionsManager().getAvailableActions(e, actionLocation);
    }
    
    public UserActionList getGlobalActions(String actionLocation) {
	return getActionsManager().getGlobalActions(actionLocation);
    }
    
    protected ActionsManager getActionsManager() {
	return ActionsManager.getInstance();
    }
    
    protected ApplicationConfigManager getAppConfigManager() {
	return ApplicationConfigManager.getInstance();
    }
    
    protected void centerWindow(Window w, OpenGroupsApplication app) {
	WebBrowser wb = app.getAppContext().getBrowser();
	float width = (float)(wb.getScreenWidth() * 0.9);
	float height = (float)(wb.getScreenHeight() * 0.6);
	
	w.setHeight(height,Sizeable.UNITS_PIXELS);
	w.setWidth(width,Sizeable.UNITS_PIXELS);
	w.setPositionX((int)((wb.getScreenWidth()-width)/2));
	w.setPositionY((int)((wb.getScreenHeight()-height)/6));
    }
    
    protected String getMessage(String key) {
	return OpenGroupsResources.getMessage(key);
    }

//    public CommandResponse executeAction(Map<String,Object> params) {
//	return userAction.execute(params);
//    }
    
    public CommandResponse executeAction(ActionContext actionContext, Map<String,Object> params) {
	return ActionsManager.getInstance().executeAction(actionContext, params);
    }
    
    public CommandResponse executeAction(String action, Map<String,Object> params) {
	return ActionsManager.getInstance().execute(action, params);
    }
    
    public List<String> getCurrentUserTypes(Entity entity, OpenGroupsApplication app){
	return UsersManager.getInstance().getCurrentUserTypes(entity,app);
    }
    
    public boolean checkUserAllowedToExecuteAction(Entity entity, OpenGroupsApplication app, UserAction ua) {
	List<String> currentUserTypes = getCurrentUserTypes(entity, app);
	return currentUserTypes.contains(ua.getUserType());
    }
    
    /**
     * @return the userAction
     */
    public UserAction getUserAction() {
        return userAction;
    }

    /**
     * @param userAction the userAction to set
     */
    public void setUserAction(UserAction userAction) {
        this.userAction = userAction;
    }
    
    public OpenGroupsModel getModel() {
	return OpenGroupsModel.getInstance();
    }
    
}
