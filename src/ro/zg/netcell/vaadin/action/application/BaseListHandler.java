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

import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityList;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public abstract class BaseListHandler extends BaseEntityHandler {

	/**
     * 
     */
	private static final long serialVersionUID = -4051210482883142670L;

	// protected void displayList(UserAction ua, OpenGroupsApplication app,
	// ComponentContainer targetContainer,GenericNameValueList list,boolean
	// showEntityType) {
	// ComponentContainer displayArea = targetContainer;
	// // Table displayArea = targetContainer;
	// displayArea.removeAllComponents();
	// // displayArea.removeAllItems();
	// for (int i = 0; i < list.size(); i++) {
	// GenericNameValueContext row = (GenericNameValueContext)
	// list.getValueForIndex(i);
	// Entity currentEntity = new Entity(row);
	// currentEntity.getState().setEntityTypeVisible(showEntityType);
	// // Panel entityContainer = new Panel();
	// CssLayout entityContainer = new CssLayout();
	// entityContainer.setSizeFull();
	// currentEntity.setEntityContainer(entityContainer);
	// displayArea.addComponent(entityContainer);
	// // displayArea.addItem(new Object[] {entityContainer}, null);
	// getActionsManager().executeAction(ActionsManager.OPEN_SELECTED_ENTITY,
	// currentEntity, app,entityContainer,false);
	//	   
	// }
	// }

	protected void displayList(UserAction ua, OpenGroupsApplication app,
			ComponentContainer targetContainer, EntityList list) {
		ComponentContainer displayArea = targetContainer;
		// Table displayArea = targetContainer;
		displayArea.removeAllComponents();
		// displayArea.removeAllItems();
		for (Entity currentEntity : list.getItemsList()) {
			// Panel entityContainer = new Panel();
			CssLayout entityContainer = new CssLayout();
//			entityContainer.setSizeFull();
			currentEntity.setEntityContainer(entityContainer);
			
			// displayArea.addItem(new Object[] {entityContainer}, null);
			getActionsManager().executeAction(
					ActionsManager.OPEN_SELECTED_ENTITY, currentEntity, app,
					entityContainer, false);
			displayArea.addComponent(entityContainer);
		}
	}

}
