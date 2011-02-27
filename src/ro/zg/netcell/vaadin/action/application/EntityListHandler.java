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
import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.constants.IconsPaths;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ComplexEntityParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityList;
import ro.zg.opengroups.vo.FilterOption;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

public class EntityListHandler extends BaseListHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -9150832279012100967L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {

	final UserAction ua = actionContext.getUserAction();
	final OpenGroupsApplication app = actionContext.getApp();

	ComponentContainer targetContainer = actionContext.getTargetContainer();
	targetContainer.removeAllComponents();

	final Entity entity = actionContext.getEntity();
	/* reset filters */
	entity.resetFilters();

	// Panel listContainer = new Panel();
	// ((VerticalLayout)listContainer.getContent()).setMargin(false);

	HorizontalLayout refreshButtonContainer = new HorizontalLayout();
	refreshButtonContainer.setSizeFull();

	// final VerticalLayout listContainer = new VerticalLayout();

	final CssLayout listAndPageControlsContainer = new CssLayout();
	listAndPageControlsContainer.addStyleName("list-container");
	listAndPageControlsContainer.setSizeFull();

	Button refreshButton = new Button(getMessage("refresh.list"));
	refreshButton.addStyleName(BaseTheme.BUTTON_LINK);
	refreshButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		refreshList(entity, ua, app, listAndPageControlsContainer);
	    }
	});

	targetContainer.addComponent(refreshButtonContainer);
	refreshButtonContainer.addComponent(refreshButton);
	refreshButtonContainer.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);

	// if (itemsCount > 0) {
	ComponentContainer filtersContainer = initFilters(entity, ua, app);
	targetContainer.addComponent(filtersContainer);
	// }
	targetContainer.addComponent(listAndPageControlsContainer);
	int itemsCount = refreshList(entity, ua, app, listAndPageControlsContainer);
    }

    private int refreshList(Entity entity, UserAction ua, OpenGroupsApplication app, ComponentContainer displayArea) {
	displayArea.removeAllComponents();
	app.setFragmentToCurrentEntity();
	
	EntityList list = getModel().getChildrenListForEntity(entity, ua, app.getCurrentUserId());
	int listSize = list.getItemsList().size();
	if ( listSize == 0) {
	    displayNoItemsMessage(ua.getTargetEntityComplexType(), displayArea);
	} else {
	    // final Table listContainer = new Table();
	    // listContainer.setSizeFull();
	    // listContainer.setPageLength(0);
	    // listContainer.addStyleName("components-inside");
	    // listContainer.addStyleName("list-table");
	    // listContainer.addContainerProperty("", CssLayout.class, null);
	    // displayArea.addComponent(listContainer);

	    displayList(ua, app, displayArea, list);

	    /* add page controls */
	    HorizontalLayout pageControlsContainer = new HorizontalLayout();
	    pageControlsContainer.setSpacing(true);
	    refreshPageControls(entity, ua, app, pageControlsContainer);
	    GridLayout gl = new GridLayout(1, 1);
	    gl.setSizeFull();
	    displayArea.addComponent(gl);
	    gl.addComponent(pageControlsContainer, 0, 0);
	    gl.setComponentAlignment(pageControlsContainer, Alignment.MIDDLE_RIGHT);
	}

	/* set current target component as lastUsedContainer on selected entity */
	entity.getState().setLastUsedContainer(displayArea);

	return listSize;

    }

    private void refreshPageControls(final Entity entity, final UserAction ua, final OpenGroupsApplication app,
	    final ComponentContainer container) {
	final int currentPage = entity.getState().getCurrentPageForCurrentAction();
	final int itemsPerPage = entity.getState().getItemsPerPage();

	Button prevButton = new Button();
	prevButton.setIcon(new ThemeResource(IconsPaths.LEFT_ARROW));

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
	    if (getAppConfigManager().getComplexEntityBooleanParam(ua.getTargetEntityComplexType(),
		    ComplexEntityParam.ALLOW_RECURSIVE_LIST)) {
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
	/* populate the currentpage combobox with the number of pages */
	for (int i = 1; i <= numberOfPages; i++) {
	    currentPageSelect.addItem(i + "/" + numberOfPages);
	}
	String currentPageString = currentPage + "/" + numberOfPages;
	currentPageSelect.setValue(currentPageString);
	int width = currentPageString.length() * 20;
	currentPageSelect.setWidth(width + "px");

	Button nextButton = new Button();
	nextButton.setIcon(new ThemeResource(IconsPaths.RIGHT_ARROW));

	if (currentPage == 1) {
	    prevButton.setEnabled(false);
	}
	if (currentPage == numberOfPages) {
	    nextButton.setEnabled(false);
	}

	container.addComponent(prevButton);
	container.addComponent(currentPageSelect);
	container.addComponent(nextButton);

	/* listeners */

	prevButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		entity.getState().setCurrentPageForCurrentAction(currentPage - 1);
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	nextButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		entity.getState().setCurrentPageForCurrentAction(currentPage + 1);
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	currentPageSelect.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		String value = (String) event.getProperty().getValue();
		String page = value.substring(0, value.indexOf("/"));
		entity.getState().setCurrentPageForCurrentAction(Integer.parseInt(page));
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

    }

    private ComponentContainer initFilters(Entity entity, UserAction ua, OpenGroupsApplication app) {

	VerticalLayout filtersLayoutWithCaption = new VerticalLayout();
	filtersLayoutWithCaption.setSizeFull();

	Label filterLabel = new Label(getMessage("filter") + ":");
	filtersLayoutWithCaption.addComponent(filterLabel);

	boolean hasFilters = false;
	HorizontalLayout filtersLayout = new HorizontalLayout();
	filtersLayout.setSpacing(true);

	filtersLayoutWithCaption.addComponent(filtersLayout);

	String complexType = ua.getTargetEntityComplexType();

	/* add depth filter */
	boolean allowRecursiveList = getAppConfigManager().getComplexEntityBooleanParam(
		ua.getTargetEntityComplexType(), ComplexEntityParam.ALLOW_RECURSIVE_LIST);
	if (allowRecursiveList) {
	    filtersLayout.addComponent(getListByDepthFilter(entity, ua, app));
	    hasFilters = true;
	}

	/* add status filter */
	if (app.getCurrentUser() != null
		&& getAppConfigManager().getComplexEntityBooleanParam(complexType, ComplexEntityParam.ALLOW_STATUS)) {
	    filtersLayout.addComponent(getStatusFilter(entity, ua, app));
	    hasFilters = true;
	}

	/* add global status filter */
	if (getAppConfigManager().getComplexEntityBooleanParam(complexType, ComplexEntityParam.ALLOW_STATUS)) {
	    filtersLayout.addComponent(getGlobalStatusFilter(entity, ua, app));
	    hasFilters = true;
	}

	/* add tag filter */
	if (getAppConfigManager().getComplexEntityBooleanParam(complexType, ComplexEntityParam.ALLOW_TAG)) {
	    filtersLayout.addComponent(getTagsFilter(entity, ua, app));
	    hasFilters = true;
	}

	GridLayout filtersContainer = new GridLayout(1, 1);
	if (hasFilters) {
	    filtersContainer = new GridLayout(2, 1);
	}
	filtersContainer.setMargin(false);
	filtersContainer.setSizeFull();
	filtersContainer.setSpacing(true);

	if (hasFilters) {
	    filtersContainer.addComponent(filtersLayoutWithCaption, 0, 0);
	    filtersContainer.setColumnExpandRatio(0, 1f);
	    filtersContainer.setComponentAlignment(filtersLayoutWithCaption, Alignment.MIDDLE_LEFT);
	}

	/* add search filter */
	Component searchFilter = getSearchFilter(entity, ua, app);
	filtersContainer.addComponent(searchFilter);
	filtersContainer.setComponentAlignment(searchFilter, Alignment.MIDDLE_RIGHT);

	return filtersContainer;
    }

    private ComponentContainer getListByDepthFilter(final Entity entity, final UserAction ua,
	    final OpenGroupsApplication app) {
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
		refreshList(entity, ua, app, selectedEntity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	HorizontalLayout container = new HorizontalLayout();
	container.setSpacing(true);
	Label label = new Label(getMessage("by.depth"));
	container.addComponent(label);
	container.addComponent(select);
	return container;

    }

    private ComponentContainer getSearchFilter(final Entity entity, final UserAction ua, final OpenGroupsApplication app) {
	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setNullSelectionAllowed(true);
	select.setNewItemsAllowed(true);
	String stringToBigError = OpenGroupsResources.getMessage("search.string.to.big.error");
	select.addValidator(new StringLengthValidator(stringToBigError, 0, 1000, true));
	select.setWidth("400px");

	final String paramName = "searchString";
	String searchValue = (String) entity.getFilterValue(paramName);
	select.addItem(searchValue);
	select.setValue(searchValue);

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
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	VerticalLayout container = new VerticalLayout();
	// container.setSpacing(true);
	container.setSizeUndefined();
	Label label = new Label(getMessage("search") + ":");
	container.addComponent(label);
	container.addComponent(select);
	return container;
    }

    private ComponentContainer getTagsFilter(final Entity entity, final UserAction ua, final OpenGroupsApplication app) {
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
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	HorizontalLayout container = new HorizontalLayout();
	container.setSpacing(true);
	Label label = new Label(getMessage("by.tag"));
	label.addStyleName("with-left-margin");
	container.addComponent(label);
	container.addComponent(select);
	return container;
    }

    private ComponentContainer getStatusFilter(final Entity entity, final UserAction ua, final OpenGroupsApplication app) {
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
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	HorizontalLayout container = new HorizontalLayout();
	container.setSpacing(true);
	Label label = new Label(getMessage("by.my-status"));
	label.addStyleName("with-left-margin");
	container.addComponent(label);
	container.addComponent(select);
	return container;
    }

    private ComponentContainer getGlobalStatusFilter(final Entity entity, final UserAction ua,
	    final OpenGroupsApplication app) {
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
		refreshList(entity, ua, app, entity.getState().getLastUsedContainer());
		// refreshList(entity, ua, app, entity.getState().getChildrenListContainer());
	    }
	});

	HorizontalLayout container = new HorizontalLayout();
	container.setSpacing(true);
	Label label = new Label(getMessage("by.global-status"));
	label.addStyleName("with-left-margin");
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
