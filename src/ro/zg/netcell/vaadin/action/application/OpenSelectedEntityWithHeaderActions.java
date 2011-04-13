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

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public class OpenSelectedEntityWithHeaderActions extends BaseEntityHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -5472441110834512067L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	OpenGroupsApplication app = actionContext.getApp();
	Entity entity = actionContext.getEntity();
	ComponentContainer container = actionContext.getTargetContainer();

	getActionsManager().executeAction(ActionsManager.REFRESH_SELECTED_ENTITY, entity,
		app, container, false);
	if(app.hasErrors()) {
	    return;
	}

	container.removeAllComponents();
	container.setSizeFull();
	UserActionList headerActions = getAvailableActions(entity, ActionLocations.HEADER);
	if (headerActions != null && headerActions.getActions() != null) {
	    // HorizontalLayout actionsContainer = new HorizontalLayout();
	    for (final UserAction ha : headerActions.getActions().values()) {

		/* create container for this action */
		CssLayout hac = (CssLayout) entity.getHeaderActionContainer(ha.getActionName());
		if (hac == null) {
		    hac = new CssLayout();
		    hac.setSizeFull();
		    entity.addHeaderActionContainer(ha.getActionName(), hac);
		    hac.setVisible(false);
		}
		container.addComponent(hac);
	    }
	}
//	Panel entityContainer = new Panel();
	CssLayout entityContainer = new CssLayout();
	entityContainer.setSizeFull();
	container.addComponent(entityContainer);
	getActionsManager().executeAction(ActionsManager.OPEN_SELECTED_ENTITY, entity,app, entityContainer, false);
	
	
    }

}
