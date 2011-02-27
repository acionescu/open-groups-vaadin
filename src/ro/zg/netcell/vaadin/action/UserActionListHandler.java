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

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.themes.Reindeer;

/**
 * This is going to basically create a tabsheet with all the nested actions from a {@link UserActionList} object
 * 
 * @author adi
 * 
 */
public class UserActionListHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 8494158542993409953L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	ComponentContainer displayArea = actionContext.getTargetContainer();
	displayArea.removeAllComponents();
	
	UserActionList ual = (UserActionList) actionContext.getUserAction();
	final OpenGroupsApplication app = actionContext.getApp();
	final Entity entity = actionContext.getEntity();
	
	TabSheet actionsTabSheet = new TabSheet();
	actionsTabSheet.addStyleName(Reindeer.TABSHEET_MINIMAL);
	actionsTabSheet.setWidth("100%");
	displayArea.addComponent(actionsTabSheet);
	/* add listener */
	actionsTabSheet.addListener(new SelectedTabChangeListener() {

	    @Override
	    public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabSheet = event.getTabSheet();
		
		AbstractComponentContainer selectedTabContent = (AbstractComponentContainer) tabSheet.getSelectedTab();
		UserAction ua = (UserAction) selectedTabContent.getData();
		if (entity != null) {
		    Deque<String> desiredActionsQueue = entity.getState().getDesiredActionTabsQueue();
		    /*
		     * if a desired action exists, it will be set afterwards, otherwise allow the first action from the
		     * list to be selected by default
		     */
		    if (desiredActionsQueue.size() != 0) {
			String nextAction = desiredActionsQueue.peek();
			if (nextAction.equals(ua.getActionName())) {
			    /* remove action from the queue */
			    desiredActionsQueue.remove();
			} else {
			    /* if this action does not match with the next desired action, do nothing */
			    return;
			}
		    }
		}
//		 entity.getState().addCurrentActionToQueue(ua.getActionName());
		// System.out.println(entity.getState().getCurrentActionTabsQueue());
		if (ua instanceof UserActionList) {
		    ua.executeHandler(entity, app, selectedTabContent, false);
		} else {
		    
		    if (entity != null) {
			entity.getState().setCurrentTabAction(ua);
			entity.getState().setCurrentTabActionContainer(selectedTabContent);
			entity.getState().setCurrentActionsPath(ua.getFullActionPath());
			entity.getState().getDesiredActionTabsQueue().clear();
			app.setFragmentToCurrentEntity();
		    }
		    ua.executeHandler(entity, app, selectedTabContent, false);
		}
		
	    }
	});
	/* add the tabsheet to the target component */

	List<String> currentUserTypes = getCurrentUserTypes(entity, app);
	Map<String, ComponentContainer> actionPathContainers = new HashMap<String, ComponentContainer>();
	List<UserAction> actionsList = new ArrayList<UserAction>(ual.getActions().values());
	for (UserAction cua : actionsList) {

	    /* display only the actions that the user is allowed to see */
	    if (!cua.allowRead(currentUserTypes)) {
		continue;
	    }

	    AbstractComponentContainer tabContent = new VerticalLayout();
	   
	    if (cua instanceof UserActionList) {
		((VerticalLayout) tabContent).setMargin(false);
	    } else {
		tabContent = new Panel();
		((Panel)tabContent).setScrollable(false);
	    }
	    tabContent.setSizeFull();
	    
	    actionPathContainers.put(cua.getActionName(), tabContent);
	    tabContent.setData(cua);
	    actionsTabSheet.addTab(tabContent, cua.getDisplayName(), null);
	}
	if (entity != null) {
	    Deque<String> desiredActionsQueue = entity.getState().getDesiredActionTabsQueue();
	    
	    if (desiredActionsQueue.size() != 0) {
//		System.out.println("desired actions: " + entity.getState().getDesiredActionsPath());
//		System.out.println("full url: "+app.getFullUrl());
		/* select the tab specified by the next desired action */
		actionsTabSheet.setSelectedTab(actionPathContainers.get(desiredActionsQueue.peek()));
	    }
	}
	
    }
    
    
//    @Override
//    public void handle(ActionContext actionContext) throws Exception {
//	ComponentContainer displayArea = actionContext.getTargetContainer();
//	displayArea.removeAllComponents();
//	
//	UserActionList ual = (UserActionList) actionContext.getUserAction();
//	final OpenGroupsApplication app = actionContext.getApp();
//	final Entity entity = actionContext.getEntity();
//	
//	TabSheetWrapper actionsTabSheet = new TabSheetWrapper();
//	actionsTabSheet.addStyleName(Reindeer.TABSHEET_MINIMAL);
//	actionsTabSheet.setWidth("100%");
//	displayArea.addComponent(actionsTabSheet);
//	/* add listener */
//	actionsTabSheet.addListener(new SelectedTabChangeListenerWrapper() {
//
//	    @Override
//	    public void selectedTabChange(SelectedTabChangeEventWrapper event) {
//		TabSheetWrapper tabSheet = event.getTabSheetWrapper();
//		
//		AbstractComponentContainer selectedTabContent = (AbstractComponentContainer) tabSheet.getSelectedTab();
//		UserAction ua = (UserAction) selectedTabContent.getData();
//		if (entity != null) {
//		    Deque<String> desiredActionsQueue = entity.getState().getDesiredActionTabsQueue();
//		    /*
//		     * if a desired action exists, it will be set afterwards, otherwise allow the first action from the
//		     * list to be selected by default
//		     */
//		    if (desiredActionsQueue.size() != 0) {
//			String nextAction = desiredActionsQueue.peek();
//			if (nextAction.equals(ua.getActionName())) {
//			    /* remove action from the queue */
//			    desiredActionsQueue.remove();
//			} else {
//			    /* if this action does not match with the next desired action, do nothing */
//			    return;
//			}
//		    }
//		}
////		 entity.getState().addCurrentActionToQueue(ua.getActionName());
//		// System.out.println(entity.getState().getCurrentActionTabsQueue());
//		if (ua instanceof UserActionList) {
//		    ua.executeHandler(entity, app, selectedTabContent, false);
//		} else {
//		    ua.executeHandler(entity, app, selectedTabContent, false);
//		    if (entity != null) {
//			entity.getState().setCurrentTabAction(ua);
//			entity.getState().setCurrentTabActionContainer(selectedTabContent);
//			entity.getState().setCurrentActionsPath(ua.getFullActionPath());
//			entity.getState().getDesiredActionTabsQueue().clear();
//		    }
//		}
//		
//	    }
//	});
//	/* add the tabsheet to the target component */
//
//	List<String> currentUserTypes = getCurrentUserTypes(entity, app);
//	Map<String, ComponentContainer> actionPathContainers = new HashMap<String, ComponentContainer>();
//	List<UserAction> actionsList = new ArrayList<UserAction>(ual.getActions().values());
//	for (UserAction cua : actionsList) {
//
//	    /* display only the actions that the user is allowed to see */
//	    if (!cua.allowRead(currentUserTypes)) {
//		continue;
//	    }
//
//	    AbstractComponentContainer tabContent = new VerticalLayout();
//	   
//	    if (cua instanceof UserActionList) {
//		((VerticalLayout) tabContent).setMargin(false);
//	    } else {
//		tabContent = new Panel();
//		((Panel)tabContent).setScrollable(false);
//	    }
//	    tabContent.setSizeFull();
//	    
//	    actionPathContainers.put(cua.getActionName(), tabContent);
//	    tabContent.setData(cua);
//	    actionsTabSheet.addTab(tabContent, cua.getDisplayName(),null);
//	}
//	if (entity != null) {
//	    Deque<String> desiredActionsQueue = entity.getState().getDesiredActionTabsQueue();
//	    if (desiredActionsQueue.size() != 0) {
//		System.out.println("set tab action: " + desiredActionsQueue.peek());
//		/* select the tab specified by the next desired action */
//		actionsTabSheet.setSelectedTab(actionPathContainers.get(desiredActionsQueue.peek()));
//	    }
//	}
//	
//    }
}
