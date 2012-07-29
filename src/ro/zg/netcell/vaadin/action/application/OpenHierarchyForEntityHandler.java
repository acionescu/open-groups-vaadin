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
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityList;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.ui.ComponentContainer;

public class OpenHierarchyForEntityHandler extends BaseListHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 6626133264094574584L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	OpenGroupsApplication app = actionContext.getApp();
	Entity selectedEntity= actionContext.getEntity();
	UserAction ua = actionContext.getUserAction();
	
//	Map<String, Object> params = ua.getActionParams();
//	params.putAll(selectedEntity.getFilterValues());
//	params.put("pageNumber", 1);
//	/* we need bring all the hierarchy and display it on the same page */
//	params.put("itemsOnPage", 5000);
//	params.put("entityId", selectedEntity.getId());
//	params.put("withContent", true);
//	params.put("userId", app.getCurrentUserId());
	
	EntityList entityList = app.getModel().getHierarchyList(selectedEntity, ua, app.getCurrentUserId());
//	
	ComponentContainer container = actionContext.getTargetContainer();
	container.removeAllComponents();

//	displayHierarchyList(selectedEntity,ua, app, container,params);
	displayList(actionContext, app, container, entityList);
	
    }
//    private void displayHierarchyList(Entity entity,UserAction ua, OpenGroupsApplication app,ComponentContainer targetContainer, Map<String, Object> params) {
//	CommandResponse response = executeAction(new ActionContext(ua, app, entity), params);
//	GenericNameValueList list = (GenericNameValueList) response.getValue("result");
//	/* remove current entity from the hierarchy */
//	list.removeValueForIndex(list.size()-1);
//	displayList(ua, app, targetContainer,list,true);
//    }
}
