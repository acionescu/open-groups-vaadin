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
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class OpenEntityInWindowHandler extends OpenGroupsActionHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 5447007711424480071L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	Entity selectedEntity = actionContext.getEntity();
	Window w = new Window(selectedEntity.getTitle());
	w.setModal(true);
	final OpenGroupsApplication app = actionContext.getApp();
	OpenGroupsMainWindow mainWindow = app.getMainWindow();
	centerWindow(w, app);
	mainWindow.addWindow(w);
//	application.setTargetComponent(w);
	
	w.addListener(new CloseListener() {
	    @Override
	    public void windowClose(CloseEvent e) {
		app.popSelectedEntity();
		app.refreshCurrentSelectedEntity();
	    }
	});
	getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS,selectedEntity,app,w,false);
    }

}
