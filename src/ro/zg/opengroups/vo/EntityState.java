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
package ro.zg.opengroups.vo;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.opengroups.constants.ActionLocations;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Table;

/**
 * Holds the state of a selected entity in the user session
 * 
 * @author adi
 * 
 */
public class EntityState implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 901016127262257178L;
    /* to define these constants here is ugly, should be defined in the database, for each entity type */
    public static final int DEFAULT_ITEMS_PER_PAGE = 7;
    public static final String DEFAULT_ACTION = "entity.list.recent.activity";
    
    private boolean opened;
    private int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;
    private List<String> activeActions = new ArrayList<String>();
    private ComponentContainer lastUsedContainer;
    private Integer currentListTotalItemsCount;
    private boolean entityTypeVisible;
    private boolean setStatusButtonVisible;

    private Deque<String> desiredActionTabsQueue = new ArrayDeque<String>();
    private Deque<String> currentActionTabsQueue = new ArrayDeque<String>();

    private UserAction currentTabAction;
    private ComponentContainer currentTabActionContainer;
    private Table childrenListContainer;
    private Entity owner;
    private Map<String, Integer> currentPageForAction = new HashMap<String, Integer>();
    
    public EntityState(Entity owner){
	this.owner=owner;
    }

    public String getDesiredActionsPath() {
	StringBuffer path = new StringBuffer();
	if (desiredActionTabsQueue.size() == 0) {
//	    return path.toString();
//		desiredActionTabsQueue.add(DEFAULT_ACTION);
	    initDesiredAction();
	}
	for (String act : desiredActionTabsQueue) {
	    path.append("/").append(act);
	}
	return path.toString();
    }
    
    private void initDesiredAction(){
	    ActionsManager actionsManager = ActionsManager.getInstance();
	    UserActionList ual =actionsManager.getAvailableActions(owner, ActionLocations.TAB);
	    if(ual != null){
		Collection<UserAction> actions = ual.getActions().values();
		if(actions.size() > 0){
		    for(UserAction ua : actions){
			desiredActionTabsQueue.add(ua.getActionName());
			break;
		    }
		}
	    }
    }

    public void setDesiredActionsPath(String actionsPath) {
	desiredActionTabsQueue.clear();
	if (actionsPath == null || "".equals(actionsPath.trim())) {
	    return;
	}
	String[] actions = actionsPath.trim().split("/");
	for (String action : actions) {
	    desiredActionTabsQueue.add(action);
	}
    }

    public void setCurrentActionsPath(String actionsPath) {
	currentActionTabsQueue.clear();
	String[] actions = actionsPath.split("/");
	for (String action : actions) {
	    currentActionTabsQueue.add(action);
	}
    }

    public String getCurrentActionPath() {
	if (currentTabAction != null) {
	    return "/" + currentTabAction.getFullActionPath();
	}
	return "";
    }

    /**
     * @return the desiredActionTabsQueue
     */
    public Deque<String> getDesiredActionTabsQueue() {
	return desiredActionTabsQueue;
    }

    /**
     * @return the currentActionTabsQueue
     */
    public Deque<String> getCurrentActionTabsQueue() {
	return currentActionTabsQueue;
    }

    /**
     * @return the opened
     */
    public boolean isOpened() {
	return opened;
    }

    /**
     * @param opened
     *            the opened to set
     */
    public void setOpened(boolean opened) {
	this.opened = opened;
    }

    public void setActionActive(String actionName) {
	activeActions.add(actionName);
    }

    public void setActionInactive(String actionName) {
	activeActions.remove(actionName);
    }

    /**
     * @return the activeActions
     */
    public List<String> getActiveActions() {
	return activeActions;
    }

    public boolean isActionActive(String actionName) {
	return activeActions.contains(actionName);
    }

    /**
     * @return the lastUsedContainer
     */
    public ComponentContainer getLastUsedContainer() {
	return lastUsedContainer;
    }

    /**
     * @param lastUsedContainer
     *            the lastUsedContainer to set
     */
    public void setLastUsedContainer(ComponentContainer lastUsedContainer) {
	this.lastUsedContainer = lastUsedContainer;
    }

    /**
     * @return the currentListTotalItemsCount
     */
    public Integer getCurrentListTotalItemsCount() {
	return currentListTotalItemsCount;
    }

    /**
     * @param currentListTotalItemsCount
     *            the currentListTotalItemsCount to set
     */
    public void setCurrentListTotalItemsCount(Integer currentListTotalItemsCount) {
	this.currentListTotalItemsCount = currentListTotalItemsCount;
    }

    /**
     * @return the entityTypeVisible
     */
    public boolean isEntityTypeVisible() {
	return entityTypeVisible;
    }

    /**
     * @param entityTypeVisible
     *            the entityTypeVisible to set
     */
    public void setEntityTypeVisible(boolean entityTypeVisible) {
	this.entityTypeVisible = entityTypeVisible;
    }

    /**
     * @return the setStatusButtonVisible
     */
    public boolean isSetStatusButtonVisible() {
	return setStatusButtonVisible;
    }

    /**
     * @param setStatusButtonVisible
     *            the setStatusButtonVisible to set
     */
    public void setSetStatusButtonVisible(boolean setStatusButtonVisible) {
	this.setStatusButtonVisible = setStatusButtonVisible;
    }

    /**
     * @return the currentTabAction
     */
    public UserAction getCurrentTabAction() {
	return currentTabAction;
    }

    /**
     * @return the currentTabActionContainer
     */
    public ComponentContainer getCurrentTabActionContainer() {
	return currentTabActionContainer;
    }

    /**
     * @param currentTabAction
     *            the currentTabAction to set
     */
    public void setCurrentTabAction(UserAction currentTabAction) {
	this.currentTabAction = currentTabAction;
    }

    /**
     * @param currentTabActionContainer
     *            the currentTabActionContainer to set
     */
    public void setCurrentTabActionContainer(ComponentContainer currentTabActionContainer) {
	this.currentTabActionContainer = currentTabActionContainer;
    }

    /**
     * @param desiredActionTabsQueue
     *            the desiredActionTabsQueue to set
     */
    public void setDesiredActionTabsQueue(Deque<String> desiredActionTabsQueue) {
	this.desiredActionTabsQueue = desiredActionTabsQueue;
    }

    /**
     * @return the childrenListContainer
     */
    public Table getChildrenListContainer() {
	return childrenListContainer;
    }

    /**
     * @param childrenListContainer
     *            the childrenListContainer to set
     */
    public void setChildrenListContainer(Table childrenListContainer) {
	this.childrenListContainer = childrenListContainer;
    }

    /**
     * @return the itemsPerPage
     */
    public int getItemsPerPage() {
	return itemsPerPage;
    }

    public void setCurrentPageForAction(String actionPath, int page) {
	currentPageForAction.put(actionPath, page);
    }

    public int getCurrentPageForAction(String actionPath) {
	if (currentPageForAction.containsKey(actionPath)) {
	    return currentPageForAction.get(actionPath);
	}
	return 1;
    }

    public void resetPageInfo() {
	currentPageForAction.clear();
    }
    
    public void resetPageInfoForCurrentAction() {
	currentPageForAction.remove(getCurrentActionPath());
    }

    public int getCurrentPageForCurrentAction() {
	return getCurrentPageForAction(getCurrentActionPath());
    }
    
    public void setCurrentPageForCurrentAction(int page) {
	setCurrentPageForAction(getCurrentActionPath(), page);
    }
}
