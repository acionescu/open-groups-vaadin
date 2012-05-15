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

import java.util.HashMap;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.gui.constants.OpenGroupsStyles;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ComplexEntityParam;
import ro.zg.opengroups.constants.TypeRelationConfigParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityList;
import ro.zg.opengroups.vo.FilterOption;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

public class EntityListHandler extends BaseListHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -9150832279012100967L;

    @Override
    public void handle(final ActionContext actionContext) throws Exception {

	final UserAction ua = actionContext.getUserAction();
	final OpenGroupsApplication app = actionContext.getApp();

	ComponentContainer targetContainer = actionContext.getTargetContainer();
	targetContainer.removeAllComponents();
	targetContainer.addStyleName(OpenGroupsStyles.LIST_ACTIONS_CONTAINER);
	
	final Entity entity = actionContext.getEntity();
	/* reset filters */
	entity.resetFilters();

	// Panel listContainer = new Panel();
	// ((VerticalLayout)listContainer.getContent()).setMargin(false);

	CssLayout refreshButtonContainer = new CssLayout();
	// refreshButtonContainer.setSizeFull();
//	refreshButtonContainer.setWidth("100%");
	refreshButtonContainer.addStyleName(OpenGroupsStyles.LIST_REFRESH_BAR);
	
	// final VerticalLayout listContainer = new VerticalLayout();

	 final CssLayout listAndPageControlsContainer = new CssLayout();
//	final VerticalLayout listAndPageControlsContainer = new VerticalLayout();

	listAndPageControlsContainer.addStyleName("list-container");
	// listAndPageControlsContainer.setSizeFull();
//	listAndPageControlsContainer.setWidth("100%");

	Button refreshButton = new Button();
	refreshButton.setDescription(getMessage("refresh.list"));
	refreshButton.setIcon(OpenGroupsResources.getIcon(OpenGroupsIconsSet.REFRESH, OpenGroupsIconsSet.MEDIUM));
	refreshButton.addStyleName(BaseTheme.BUTTON_LINK+ " middle-right");
	refreshButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		refreshList(entity, ua, app, listAndPageControlsContainer, actionContext);
	    }
	});

	targetContainer.addComponent(refreshButtonContainer);
	refreshButtonContainer.addComponent(refreshButton);

	ComponentContainer filtersContainer = initFilters(entity, ua, app, actionContext);
	targetContainer.addComponent(filtersContainer);
	
	targetContainer.addComponent(listAndPageControlsContainer);
	int itemsCount = refreshList(entity, ua, app, listAndPageControlsContainer, actionContext);
    }

    private int refreshList(Entity entity, UserAction ua, OpenGroupsApplication app, ComponentContainer displayArea,
	    ActionContext ac) {
	displayArea.removeAllComponents();
	ac.getWindow().setFragmentToEntity(entity);

	EntityList list = getModel().getChildrenListForEntity(entity, ua, app.getCurrentUserId());
	int listSize = list.getItemsList().size();
	if (listSize == 0) {
	    displayNoItemsMessage(ua.getTargetEntityComplexType(), displayArea);
	} else {
	    // final Table listContainer = new Table();
	    // listContainer.setSizeFull();
	    // listContainer.setPageLength(0);
	    // listContainer.addStyleName("components-inside");
	    // listContainer.addStyleName("list-table");
	    // listContainer.addContainerProperty("", CssLayout.class, null);
	    // displayArea.addComponent(listContainer);

	    displayList(ac, app, displayArea, list);

	    /* add page controls */
	    CssLayout pageControlsContainer = new CssLayout();
	    pageControlsContainer.addStyleName("middle-right");
//	    pageControlsContainer.setSpacing(true);
	    refreshPageControls(entity, ua, app, pageControlsContainer, ac);
	    displayArea.addComponent(pageControlsContainer);

	}

	/* set current target component as lastUsedContainer on selected entity */
	entity.getState().setLastUsedContainer(displayArea);

	return listSize;

    }

    private void refreshPageControls(final Entity entity, final UserAction ua, final OpenGroupsApplication app,
	    final ComponentContainer container, final ActionContext ac) {
	
	final int itemsPerPage = entity.getState().getItemsPerPage();

	Button prevButton = new Button();
	prevButton.setIcon(OpenGroupsResources.getIcon(OpenGroupsIconsSet.LEFT_ARROW, OpenGroupsIconsSet.SMALL));

	ComboBox currentPageSelect = new ComboBox();
	currentPageSelect.setImmediate(true);
	currentPageSelect.setNullSelectionAllowed(false);
	currentPageSelect.setNewItemsAllowed(false);

	double totalItemsCount = entity.getState().getCurrentListTotalItemsCount();

	if (totalItemsCount < 0) {
	    /* the type of the listed entities */
	    String targetEntityComplexType = ua.getTargetEntityComplexType().toLowerCase();
	    /* let's find out if this list is recursive or not */
	    boolean isRecursive = false;
	    /* first check if the target type allows a recursive list in the first place */
//	    if (getAppConfigManager().getComplexEntityBooleanParam(ua.getTargetEntityComplexType(),
//		    ComplexEntityParam.ALLOW_RECURSIVE_LIST)) {
	    if(getAppConfigManager().getTypeRelationBooleanConfigParam(
			ua.getTypeRelationId(), TypeRelationConfigParam.ALLOW_RECURSIVE_LIST)) {
		/*
		 * if it does, check if the filter is actually set to display all items, that means the recursion depth
		 * is undefined or greater than 0
		 */
		isRecursive = (entity.getFilterValue("depth") == null) ? true : false;
		if (!isRecursive) {
		    long listDepth = (Long) entity.getFilter("depth").getValue();
		    if (listDepth > 0) {
			isRecursive = true;
		    }
		}
	    }

	    /* if the list is recursive get the number of all entities of the specified type under selected entity */
	    if (isRecursive) {
		totalItemsCount = entity.getRecursiveSubtypeEntitiesCount().get(targetEntityComplexType);
	    }
	    /* not recursive, get only the number of first level entities of the specified type */
	    else {
		totalItemsCount = entity.getSubtypeEntitiesCount().get(targetEntityComplexType);
	    }
	}

	/*
	 * now that we know the total possible entities in this list, and the items per page, we can calculate, the
	 * number of pages
	 */
	int numberOfPages = (int) Math.ceil(totalItemsCount / itemsPerPage);
	final int currentPage=entity.getState().getCurrentPageForCurrentAction();
	
	/* populate the currentpage combobox with the number of pages */
	for (int i = 1; i <= numberOfPages; i++) {
	    currentPageSelect.addItem(i + "/" + numberOfPages);
	}
	String currentPageString = currentPage + "/" + numberOfPages;
	currentPageSelect.setValue(currentPageString);
	int width = currentPageString.length() * 20;
	currentPageSelect.setWidth(width + "px");

	Button nextButton = new Button();
	nextButton.setIcon(OpenGroupsResources.getIcon(OpenGroupsIconsSet.RIGHT_ARROW, OpenGroupsIconsSet.SMALL));

	if (currentPage == 1) {
	    prevButton.setEnabled(false);
	}
	if (currentPage == numberOfPages) {
	    nextButton.setEnabled(false);
	}

	prevButton.addStyleName("middle-left right-margin-10");
	currentPageSelect.addStyleName("middle-left right-margin-10");
	nextButton.addStyleName("middle-left");
	
	container.addComponent(prevButton);
	container.addComponent(currentPageSelect);
	container.addComponent(nextButton);

	/* listeners */

	prevButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		entity.getState().setCurrentPageForCurrentAction(currentPage - 1);
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer(), ac);
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	nextButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		entity.getState().setCurrentPageForCurrentAction(currentPage + 1);
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer(), ac);
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	currentPageSelect.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		String value = (String) event.getProperty().getValue();
		String page = value.substring(0, value.indexOf("/"));
		entity.getState().setCurrentPageForCurrentAction(Integer.parseInt(page));
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer(), ac);
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

    }

    private ComponentContainer initFilters(Entity entity, UserAction ua, OpenGroupsApplication app,
	    final ActionContext ac) {

	// VerticalLayout filtersLayoutWithCaption = new VerticalLayout();
	// filtersLayoutWithCaption.setWidth("100%");

	// Label filterLabel = new Label(getMessage("filter") + ":");
	// filtersLayoutWithCaption.addComponent(filterLabel);

	boolean hasFilters = false;
	CssLayout filtersLayout = new CssLayout();
	filtersLayout.addStyleName(OpenGroupsStyles.LIST_FILTERS_BAR);
//	filtersLayout.setWidth("100%");
//	filtersLayout.setSpacing(true);

	// filtersLayoutWithCaption.addComponent(filtersLayout);

	String complexType = ua.getTargetEntityComplexType();

	/* add depth filter */
//	boolean allowRecursiveList = getAppConfigManager().getComplexEntityBooleanParam(
//		ua.getTargetEntityComplexType(), ComplexEntityParam.ALLOW_RECURSIVE_LIST);
	boolean allowRecursiveList = getAppConfigManager().getTypeRelationBooleanConfigParam(
		ua.getTypeRelationId(), TypeRelationConfigParam.ALLOW_RECURSIVE_LIST);
	
	if (allowRecursiveList) {
	    ComponentContainer listByDepthFilter = getListByDepthFilter(entity, ua, app, ac);
	    listByDepthFilter.addStyleName("middle-left right-margin-10");
	    filtersLayout.addComponent(listByDepthFilter);
	    hasFilters = true;
	}

	/* add status filter */
	if (app.getCurrentUser() != null
		&& getAppConfigManager().getComplexEntityBooleanParam(complexType, ComplexEntityParam.ALLOW_STATUS)) {
	    ComponentContainer statusFilter = getStatusFilter(entity, ua, app, ac);
	    statusFilter.addStyleName("middle-left right-margin-10");
	    filtersLayout.addComponent(statusFilter);
	    hasFilters = true;
	}

	/* add global status filter */
	if (getAppConfigManager().getComplexEntityBooleanParam(complexType, ComplexEntityParam.ALLOW_STATUS)) {
	    ComponentContainer globalStatusFilter = getGlobalStatusFilter(entity, ua, app, ac);
	    globalStatusFilter.addStyleName("middle-left right-margin-10");
	    filtersLayout.addComponent(globalStatusFilter);
	    hasFilters = true;
	}

	/* add tag filter */
	if (getAppConfigManager().getComplexEntityBooleanParam(complexType, ComplexEntityParam.ALLOW_TAG)) {
	    ComponentContainer tagsFilter = getTagsFilter(entity, ua, app, ac);
	    tagsFilter.addStyleName("middle-left right-margin-10");
	    filtersLayout.addComponent(tagsFilter);
	    hasFilters = true;
	}

//	GridLayout filtersContainer = new GridLayout(1, 1);
//	if (hasFilters) {
//	    filtersContainer = new GridLayout(2, 1);
//	}
//	filtersContainer.setMargin(false);
//	// filtersContainer.setSizeFull();
//	filtersContainer.setWidth("100%");
//	filtersContainer.setSpacing(true);
//	filtersContainer.addStyleName(OpenGroupsStyles.LIST_FILTERS_BAR);
//
//	if (hasFilters) {
//	    filtersContainer.addComponent(filtersLayout, 0, 0);
//	    filtersContainer.setColumnExpandRatio(0, 1f);
//	    filtersContainer.setComponentAlignment(filtersLayout, Alignment.MIDDLE_LEFT);
//	}

	/* add search filter */
	Component searchFilter = getSearchFilter(entity, ua, app, ac);
	searchFilter.addStyleName("middle-right");
//	filtersContainer.addComponent(searchFilter);
//	filtersContainer.setComponentAlignment(searchFilter, Alignment.MIDDLE_RIGHT);
//
//	return filtersContainer;
	
	filtersLayout.addComponent(searchFilter);
	return filtersLayout;
    }

    private ComponentContainer getListByDepthFilter(final Entity entity, final UserAction ua,
	    final OpenGroupsApplication app, final ActionContext ac) {
	ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setNullSelectionAllowed(false);
	select.setNewItemsAllowed(false);
	select.setWidth("115px");
	String paramName = "depth";
	FilterOption f2 = new FilterOption(getMessage("list.first.level." + ua.getTargetEntityType().toLowerCase()),
		paramName, 0L);
	FilterOption f1 = new FilterOption(getMessage("list.all." + ua.getTargetEntityType().toLowerCase()), paramName,
		null);

	select.addItem(f1);
	select.addItem(f2);

	final Entity selectedEntity = entity;
	FilterOption currentFilter = selectedEntity.getFilter(paramName);
	if (currentFilter == null || !select.containsId(currentFilter)) {
	    currentFilter = f1;
	    selectedEntity.setFilter(currentFilter);
	}

	select.setValue(currentFilter);

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		entity.getState().resetPageInfoForCurrentAction();
		selectedEntity.setFilter((FilterOption) event.getProperty().getValue());
		refreshList(entity, ua, app, selectedEntity.getState().getLastUsedContainer(), ac);
	    }
	});

	CssLayout container = new CssLayout();
	Label label = new Label(getMessage("by.depth"));
	container.addComponent(label);
	container.addComponent(select);
	return container;

    }

    private ComponentContainer getSearchFilter(final Entity entity, final UserAction ua,
	    final OpenGroupsApplication app, final ActionContext ac) {
	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setNullSelectionAllowed(true);
	select.setNewItemsAllowed(true);
	String stringToBigError = OpenGroupsResources.getMessage("search.string.to.big.error");
	select.addValidator(new StringLengthValidator(stringToBigError, 0, 1000, true));
	select.setWidth("350px");

	final String paramName = "searchString";
	String searchValue = (String) entity.getFilterValue(paramName);
	if (searchValue != null) {
	    select.addItem(searchValue);
	    select.setValue(searchValue);
	}

	// HorizontalLayout layout = new HorizontalLayout();
	// layout.setSpacing(true);
	// layout.setSizeUndefined();

	// final String searchCaption = OpenGroupsResources.getMessage("search");
	// Button searchButton = new Button(searchCaption);
	// searchButton.addListener(new ClickListener() {
	//
	// @Override
	// public void buttonClick(ClickEvent event) {
	// select.validate();
	// String value = (String) select.getValue();
	// if (value != null && "".equals(value.trim())) {
	// value = null;
	// }
	// FilterOption fo = new FilterOption(searchCaption, paramName, value);
	// entity.setFilter(fo);
	// refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
	// }
	// });

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		select.validate();
		String value = (String) select.getValue();
		if (value != null && "".equals(value.trim())) {
		    value = null;
		}
		FilterOption fo = new FilterOption(null, paramName, value);
		entity.setFilter(fo);
		/* set current page to 1 */
		entity.getState().resetPageInfoForCurrentAction();
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer(), ac);
	    }
	});

	CssLayout container = new CssLayout();
	container.setSizeUndefined();
	Label label = new Label(getMessage("search"));
	container.addComponent(label);
	container.addComponent(select);
	return container;
    }

    private ComponentContainer getTagsFilter(final Entity entity, final UserAction ua, final OpenGroupsApplication app,
	    final ActionContext ac) {
	CommandResponse response = executeAction(ActionsManager.GET_TAGS, new HashMap<String, Object>());
	GenericNameValueList list = (GenericNameValueList) response.getValue("result");

	ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setWidth("150px");
	final String paramName = "tag";

	for (int i = 0; i < list.size(); i++) {
	    GenericNameValueContext row = (GenericNameValueContext) list.getValueForIndex(i);
	    select.addItem(row.getValue("tag"));
	}

	String currentFilterValue = (String) entity.getFilterValue(paramName);

	if (currentFilterValue != null) {
	    select.setValue(currentFilterValue);
	}

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		FilterOption fo = new FilterOption(null, paramName, event.getProperty().getValue());
		entity.setFilter(fo);
		entity.getState().resetPageInfoForCurrentAction();
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer(), ac);
	    }
	});

	CssLayout container = new CssLayout();
	Label label = new Label(getMessage("by.tag"));
	container.addComponent(label);
	container.addComponent(select);
	return container;
    }

    private ComponentContainer getStatusFilter(final Entity entity, final UserAction ua,
	    final OpenGroupsApplication app, final ActionContext ac) {
	ComboBox select = getStatusesCombo();
	final String paramName = "status";
	String currentFilterValue = (String) entity.getFilterValue(paramName);

	if (currentFilterValue != null) {
	    select.setValue(currentFilterValue);
	}

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		FilterOption fo = new FilterOption(null, paramName, event.getProperty().getValue());
		entity.setFilter(fo);
		entity.getState().resetPageInfoForCurrentAction();
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer(), ac);
	    }
	});

	CssLayout container = new CssLayout();
	Label label = new Label(getMessage("by.my-status"));
	container.addComponent(label);
	container.addComponent(select);
	return container;
    }

    private ComponentContainer getGlobalStatusFilter(final Entity entity, final UserAction ua,
	    final OpenGroupsApplication app, final ActionContext ac) {
	ComboBox select = getStatusesCombo();
	final String paramName = "globalStatus";
	String currentFilterValue = (String) entity.getFilterValue(paramName);

	if (currentFilterValue != null) {
	    select.setValue(currentFilterValue);
	}

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		FilterOption fo = new FilterOption(null, paramName, event.getProperty().getValue());
		entity.setFilter(fo);
		entity.getState().resetPageInfoForCurrentAction();
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer(), ac);
	    }
	});

	CssLayout container = new CssLayout();
	Label label = new Label(getMessage("by.global-status"));
	label.setSizeUndefined();
	container.addComponent(label);
	container.addComponent(select);
	return container;
    }

    private ComboBox getStatusesCombo() {
	CommandResponse response = executeAction(ActionsManager.GET_STATUSES, new HashMap<String, Object>());
	GenericNameValueList list = (GenericNameValueList) response.getValue("result");

	ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setWidth("110px");

	for (int i = 0; i < list.size(); i++) {
	    GenericNameValueContext row = (GenericNameValueContext) list.getValueForIndex(i);
	    select.addItem(row.getValue("status"));
	}

	return select;
    }

    private void displayNoItemsMessage(String entityType, ComponentContainer container) {
	String message = OpenGroupsResources.getMessage("no.items." + entityType.toLowerCase());
	Label l = new Label(message);
	container.removeAllComponents();
	container.addComponent(l);
    }

}
