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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.opengroups.constants.UserTypes;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.User;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.ui.ComponentContainer;

public class ActionContext implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -404614270962547099L;
    private UserAction userAction;
    private OpenGroupsApplication app;
    private Entity entity;
    private ComponentContainer targetContainer;
    private boolean runInThread;
    private Map<String, Object> params;
    private OpenGroupsMainWindow window;
    private Entity mainEntity;

    public ActionContext(UserAction ua, OpenGroupsApplication app, Entity e, ComponentContainer c, boolean rit) {
	userAction = ua;
	this.app = app;
	entity = e;
	targetContainer = c;
	runInThread = rit;
	window = app.getActiveWindow();
    }

    public ActionContext(UserAction ua, OpenGroupsApplication app, Entity e, ComponentContainer c) {
	this(ua, app, e, c, false);
    }

    public ActionContext(UserAction ua, OpenGroupsApplication app, Entity e) {
	this(ua, app, e, null, false);
    }

    public ActionContext(UserAction ua, OpenGroupsApplication app, Entity e, Map<String, Object> params) {
	this(ua, app, null);
	this.params = params;
    }

    public ActionContext(OpenGroupsApplication app) {
	this(null, app, null);
    }

    public ActionContext(OpenGroupsApplication app, Map<String, Object> params) {
	this(null, app, null, params);
    }

    public List<String> getCurrentUserTypes() {
	User u = app.getCurrentUser();
	List<String> userTypes = new ArrayList<String>();
	userTypes.add(UserTypes.ANY);
	if (u == null) {
	    userTypes.add(UserTypes.GUEST);
	} else {
	    userTypes.add(UserTypes.MEMBER);
	    if (entity != null) {
		if (u.getUserId() == entity.getCreatorId()) {
		    userTypes.add(UserTypes.CREATOR);
		}
	    }
	}
	return userTypes;
    }
    
    public boolean isUserTypePresent(String userType) {
	List<String> currentUserTypes = getCurrentUserTypes();
	return currentUserTypes.contains(userType);
    }
    
    public boolean isActionVisible() {
	return userAction.isVisible(this);
    }

    public boolean isActionAllowed(){
	return isUserTypePresent(userAction.getUserType());
    }
    
    /* getters/setters */

    /**
     * @return the runInThread
     */
    public boolean isRunInThread() {
	return runInThread;
    }

    /**
     * @param runInThread
     *            the runInThread to set
     */
    public void setRunInThread(boolean runInThread) {
	this.runInThread = runInThread;
    }

    /**
     * @return the userAction
     */
    public UserAction getUserAction() {
	return userAction;
    }

    /**
     * @return the app
     */
    public OpenGroupsApplication getApp() {
	return app;
    }

    /**
     * @return the entity
     */
    public Entity getEntity() {
	return entity;
    }

    /**
     * @return the targetContainer
     */
    public ComponentContainer getTargetContainer() {
	return targetContainer;
    }

    /**
     * @param userAction
     *            the userAction to set
     */
    public void setUserAction(UserAction userAction) {
	this.userAction = userAction;
    }

    /**
     * @param app
     *            the app to set
     */
    public void setApp(OpenGroupsApplication app) {
	this.app = app;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(Entity entity) {
	this.entity = entity;
    }

    /**
     * @param targetContainer
     *            the targetContainer to set
     */
    public void setTargetContainer(ComponentContainer targetContainer) {
	this.targetContainer = targetContainer;
    }

    /**
     * @return the params
     */
    public Map<String, Object> getParams() {
	return params;
    }

    /**
     * @param params
     *            the params to set
     */
    public void setParams(Map<String, Object> params) {
	this.params = params;
    }

    /**
     * @return the window
     */
    public OpenGroupsMainWindow getWindow() {
	return window;
    }

    /**
     * @param window
     *            the window to set
     */
    public void setWindow(OpenGroupsMainWindow window) {
	this.window = window;
    }

    /**
     * @return the mainEntity
     */
    public Entity getMainEntity() {
	return mainEntity;
    }

    /**
     * @param mainEntity
     *            the mainEntity to set
     */
    public void setMainEntity(Entity mainEntity) {
	this.mainEntity = mainEntity;
    }

}
