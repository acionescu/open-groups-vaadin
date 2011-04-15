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
import java.util.List;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.constants.ApplicationConfigParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.TabContainer;
import ro.zg.opengroups.vo.User;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class LoadMainWindowHandler extends OpenGroupsActionHandler {

	/**
     * 
     */
	private static final long serialVersionUID = 7749139033732845419L;

	@Override
	public void handle(ActionContext actionContext) throws Exception {
		// initMainWindow(application);
		buildGuiLogic(actionContext.getApp());
	}

	private void buildGuiLogic(OpenGroupsApplication application) {
		addUserHeaderActions(application);
		addMainRootEntityTab(application);
		addApplicationEntityTab(application);
		// addUserTabActions(application);
	}

	private void addMainRootEntityTab(OpenGroupsApplication app) {
		/* set the default entity as the current selected entity */
		Entity selectedEntity = app.getRootEntity();
		if (selectedEntity == null) {
			selectedEntity = new Entity((Long) getAppConfigManager()
					.getApplicationConfigParam(
							ApplicationConfigParam.DEFAULT_ENTITY_ID));
			// getActionsManager().executeAction(ActionsManager.REFRESH_SELECTED_ENTITY,
			// selectedEntity, app, null,
			// false);
			String caption = getMessage("metagroup.caption");
			selectedEntity.setTitle(caption);
			app.setRootEntity(selectedEntity);
			selectedEntity.getState().setOpened(true);
			TabContainer tabContainer = app.addTab(selectedEntity, false);
			tabContainer.setRefreshOn(true);
			// app.setSelectedTab(tabContainer.getContainer());
			// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS,
			// selectedEntity,app,
			// tabContainer.getContainer(), false);
		}
	}

	private void addApplicationEntityTab(OpenGroupsApplication app) {
		Long entityId = (Long) getAppConfigManager().getApplicationConfigParam(
				ApplicationConfigParam.APP_ENTITY_ID);
		if (entityId != null) {
			Entity entity = new Entity(entityId);
			String caption = getMessage("app.metagroup.caption");
			entity.setTitle(caption);
			entity.getState().setOpened(true);
			entity.getState().setEntityTypeVisible(true);
			TabContainer tabContainer = app.addTab(entity, false);
			tabContainer.setRefreshOn(true);
			// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS,
			// entity, app,
			// tabContainer.getContainer(), false);
		}
	}

	private void addUserTabActions(OpenGroupsApplication app) {
		if (app.getCurrentUser() != null) {
			UserActionList actionsList = ActionsManager.getInstance()
					.getGlobalActions(ActionLocations.TAB);
			app.getMainWindow().setUserActionsTabVisible(true);
			actionsList.executeHandler(null, app, app.getMainWindow()
					.getUserActionsContainer());
		}
	}

	private void addUserHeaderActions(final OpenGroupsApplication app) {
		UserActionList actionList = getGlobalActions(ActionLocations.HEADER);
		if (actionList == null) {
			return;
		}
		Collection<UserAction> userActions = actionList.getActions().values();

		User user = app.getCurrentUser();
		GridLayout userArea = app.getMainWindow().getHeaderPanel();
		userArea.removeAllComponents();

		if (user != null) {
			Label userInfo = new Label(app.getMessage("login.user.info") + ": "
					+ user.getUsername());
			// userInfo.setSizeFull();
			userArea.addComponent(userInfo, 0, 0);
			userArea.setColumnExpandRatio(0, 1f);
			userArea.setComponentAlignment(userInfo, Alignment.MIDDLE_LEFT);
		}
		HorizontalLayout actionsContainer = new HorizontalLayout();
		actionsContainer.setSizeUndefined();

		/* get the current user types */
		List<String> userTypes = UsersManager.getInstance()
				.getCurrentUserTypes(null, app);
		for (final UserAction ua : userActions) {
			/*
			 * if the current user is not allowed to read/view the current
			 * action then skip it
			 */
			if (!ua.allowRead(userTypes)) {
				continue;
			}
			Button actButton = new Button(ua.getDisplayName());
			actButton.addListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					// app.executeAction(ua, null);
					ua.executeHandler(null, app, null);
				}
			});

			actionsContainer.addComponent(actButton);
		}
		userArea.addComponent(actionsContainer, 1, 0);
		userArea.setColumnExpandRatio(1, 1f);
		userArea
				.setComponentAlignment(actionsContainer, Alignment.MIDDLE_RIGHT);

	}

}
