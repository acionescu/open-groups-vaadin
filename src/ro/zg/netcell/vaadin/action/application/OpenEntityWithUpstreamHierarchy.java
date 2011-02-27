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

import java.util.Map;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.data.GenericNameValueList;

public class OpenEntityWithUpstreamHierarchy extends BaseListHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 8467893494049659768L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	OpenGroupsApplication app = actionContext.getApp();
	Entity selectedEntity= actionContext.getEntity();
	UserAction ua = actionContext.getUserAction();
	
	Map<String, Object> params = ua.getActionParams();
	params.putAll(selectedEntity.getFilterValues());
	params.put("pageNumber", 1);
	/* we need bring all the hierarchy and display it on the same page */
	params.put("itemsOnPage", 5000);
	params.put("entityId", selectedEntity.getId());
	params.put("withContent", true);
	params.put("userId", app.getCurrentUserId());
	
	ComponentContainer container = actionContext.getTargetContainer();
	container.removeAllComponents();
	VerticalLayout hierarchyContainer = new VerticalLayout();
	container.addComponent(hierarchyContainer);
//	app.setTargetComponent(hierarchyContainer);
	displayHierarchyList(selectedEntity,ua, app, hierarchyContainer,params);
	VerticalLayout entityContainer = new VerticalLayout();
	container.addComponent(entityContainer);
//	app.setTargetComponent(entityContainer);
	getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS, selectedEntity,app,entityContainer,false);
    }
    
    private void displayHierarchyList(Entity entity,UserAction ua, OpenGroupsApplication app,ComponentContainer targetContainer, Map<String, Object> params) {
	CommandResponse response = executeAction(new ActionContext(ua, app, entity), params);
	GenericNameValueList list = (GenericNameValueList) response.getValue("result");
	/* remove current entity from the hierarchy */
	list.removeValueForIndex(list.size()-1);
//	displayList(ua, app, targetContainer,list,true);
    }

}
