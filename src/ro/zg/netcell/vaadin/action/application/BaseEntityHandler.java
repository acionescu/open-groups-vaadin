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
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.UserActionList;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

public abstract class BaseEntityHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -3654060286201762359L;

    protected void displayActionsForSelectedEntity(Entity entity, OpenGroupsApplication application,
	    ComponentContainer actionsContainer, ActionContext ac) {
	// ComponentContainer targetContainer = application.getTargetComponent();
	/* get available actions for the selected entity */
	UserActionList availableActions = getAvailableActions(entity, ActionLocations.TAB);
	
	/* display the actions */
	// application.setTargetComponent(actionsContainer);
	if (availableActions != null) {
	    availableActions.executeHandler(entity, application, actionsContainer, false,ac);
	}
	
    }

}
