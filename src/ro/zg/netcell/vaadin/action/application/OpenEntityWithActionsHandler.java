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
import ro.zg.opengroups.vo.Entity;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public class OpenEntityWithActionsHandler extends BaseEntityHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 1556713610595976472L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	
	Entity entity = actionContext.getEntity();
	OpenGroupsApplication app = actionContext.getApp();
	
//	Panel parent = ((Panel)actionContext.getTargetContainer());
//	parent.removeAllComponents();
//	
//	SplitPanel sp = new SplitPanel(SplitPanel.ORIENTATION_VERTICAL);
//	sp.setSizeFull();
//	sp.setSplitPosition(50);
//	parent.setContent(sp);
	
	ComponentContainer targetContainer = actionContext.getTargetContainer();
	targetContainer.removeAllComponents();
	/* show the component */
	CssLayout entityContainer = new CssLayout();
	/* set current container the entity container */
//	application.setTargetComponent(entityContainer);
	targetContainer.addComponent(entityContainer);
//	targetContainer.setExpandRatio(entityContainer, 2f);
	
//	sp.setFirstComponent(entityContainer);
	getActionsManager().executeAction(ActionsManager.OPEN_SELECTED_ENTITY_WITH_HEADER_ACTIONS, entity,app,entityContainer,false,actionContext);
	if(app.hasErrors()) {
	    return;
	}
	/* come back to the original container */
//	application.setTargetComponent(targetContainer);
	
//	/* get available actions for the selected entity */
//	UserActionList availableActions = getAvailableActions(application,ActionLocations.TAB);
//	/* add the container for the actions to the window */
//	VerticalLayout actionsContainer = new VerticalLayout();
//	targetContainer.addComponent(actionsContainer);
//	/* display the actions */
////	application.setTargetComponent(actionsContainer);
//	availableActions.executeHandler(application,actionsContainer,true);
	
	/* add the container for the actions to the window */
	CssLayout actionsContainer = new CssLayout();
	targetContainer.addComponent(actionsContainer);
//	targetContainer.setExpandRatio(actionsContainer, 1f);
//	sp.setSecondComponent(actionsContainer);
	displayActionsForSelectedEntity(entity,app, actionsContainer,actionContext);
//	parent.addComponent(targetContainer);
	
    }
    
}
