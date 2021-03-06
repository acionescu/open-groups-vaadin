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
package ro.zg.netcell.vaadin.action.application;

import java.util.Collection;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.constants.ApplicationConfigParam;
import ro.zg.opengroups.util.OpenGroupsUtil;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.User;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

public class LoadMainWindowHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 7749139033732845419L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	buildGuiLogic(actionContext.getApp(),actionContext);
	getActionsManager().executeAction(ActionsManager.SHOW_CAUSAL_HIERARCHY, actionContext);
	getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS, null, actionContext.getApp(), actionContext.getWindow().getEntityContent(),false, actionContext);
    }

    private void buildGuiLogic(OpenGroupsApplication application,ActionContext ac) throws ContextAwareException {
	addUserHeaderActions(application,ac);
    }

  

    private void addUserHeaderActions(final OpenGroupsApplication app, final ActionContext ac) throws ContextAwareException {
	UserActionList actionList = getGlobalActions(ActionLocations.HEADER);
	if (actionList == null) {
	    return;
	}
	Collection<UserAction> userActions = actionList.getActions().values();
	OpenGroupsMainWindow window = ac.getWindow();
	User user = app.getCurrentUser();
	
	
	CssLayout header = window.getHeader();
	header.removeAllComponents();
	

	if (user != null) {
	    
	    Embedded usericon = new Embedded(null,
		    OpenGroupsResources.getIcon(OpenGroupsIconsSet.USER, OpenGroupsIconsSet.MEDIUM));
	    usericon.addStyleName("middle-left right-margin-10");
	    header.addComponent(usericon);
	    
	    Label userInfo = new Label(/*app.getMessage("login.user.info") + ": " +*/ user.getUsername());
	    userInfo.setSizeUndefined();
	    header.addComponent(userInfo);
	    userInfo.addStyleName("username-label");
	    
	}


	/* get the current user types */
//	List<String> userTypes = UsersManager.getInstance().getCurrentUserTypes(null, app);
	for (final UserAction ua : userActions) {
	    /*
	     * if the current user is not allowed to read/view the current action then skip it
	     */
//	    if (!ua.allowRead(userTypes)) {
	    if(!ua.isVisible(ac)) {
		continue;
	    }
	    Button actButton = new Button(ua.getDisplayName());
	    
	    actButton.addListener(new ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
		    // app.executeAction(ua, null);
		    ua.executeHandler(null, app, null,ac);
		}
	    });

	    header.addComponent(actButton);
	    actButton.addStyleName("middle-right left-margin-10");
	    
	}
	
	Component rootLink = getRootEntityLink(app);
	Component metaLink =  getMetaEntityLink(app);
	rootLink.setSizeUndefined();
	metaLink.setSizeUndefined();

	/* add quick links */
	header.addComponent(rootLink);
	header.addComponent(metaLink);
	rootLink.addStyleName("middle-right left-margin-10");
	metaLink.addStyleName("middle-right left-margin-10");
    }

    private Component getRootEntityLink(OpenGroupsApplication app) throws ContextAwareException {
	Entity selectedEntity = app.getRootEntity();
	if (selectedEntity == null) {
	    selectedEntity = app.getModel().getRootEntity();
	    selectedEntity.getState().setOpened(true);
	}
	return OpenGroupsUtil.getLinkForEntity(selectedEntity, app);
    }

    private Component getMetaEntityLink(OpenGroupsApplication app) throws ContextAwareException {
	Long entityId = (Long) app.getAppConfigManager().getApplicationConfigParam(ApplicationConfigParam.APP_ENTITY_ID);
	if (entityId != null) {
	    Entity entity = new Entity(entityId);
//	    getModel().refreshEntity(entity, null);
	    String caption = getMessage("app.metagroup.caption");
	    entity.setTitle(caption);
	    entity.getState().setOpened(true);
	    entity.getState().setEntityTypeVisible(true);

	    return OpenGroupsUtil.getLinkForEntity(entity, app);
	}
	return null;
    }

	// private Component getLinkForEntity(Entity entity, OpenGroupsApplication
	// app) {
	// String url = app.getURL().toString() + "#" +
	// UriFragments.SHOW_ENTITY_FRAGMENT + entity.getId();
	// return new Label(OpenGroupsUtil.wrapAsA(url, entity.getTitle(), "_self"),
	// Label.CONTENT_XHTML);
	// }

}
