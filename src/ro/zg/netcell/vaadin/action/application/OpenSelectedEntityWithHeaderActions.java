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

import java.util.List;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.netcell.vaadin.action.constants.ActionConstants;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.gui.constants.OpenGroupsStyles;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityLink;
import ro.zg.opengroups.vo.EntityState;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.BaseTheme;

public class OpenSelectedEntityWithHeaderActions extends BaseEntityHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -5472441110834512067L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	OpenGroupsApplication app = actionContext.getApp();
	Entity entity = actionContext.getEntity();
	ComponentContainer container = actionContext.getTargetContainer();
	
	app.getModel().populateCauses(entity);

	getActionsManager().executeAction(ActionsManager.REFRESH_SELECTED_ENTITY, entity, app, container, false,
		actionContext);

	

	if (app.hasErrors()) {
	    return;
	}

	container.removeAllComponents();
//	container.setWidth("100%");

	if (app.getRootEntity().getId() != entity.getId()) {

	    /* create header containers */
	    createHeaderContainers(actionContext);

	    /* add causes combo */
	    addCausesFragment(actionContext);
	}

	// Panel entityContainer = new Panel();
	CssLayout entityContainer = new CssLayout();
	entityContainer.addStyleName("entity-container");
	// entityContainer.setSizeFull();
//	entityContainer.setWidth("100%");
	container.addComponent(entityContainer);
	getActionsManager().executeAction(ActionsManager.OPEN_SELECTED_ENTITY, entity, app, entityContainer, false,
		actionContext);

    }

    private void createHeaderContainers(ActionContext actionContext) {
	Entity entity = actionContext.getEntity();
	ComponentContainer container = actionContext.getTargetContainer();

	/* create a special container where the cause can be shown */
	ComponentContainer cc = createHeaderContainer();
	entity.addHeaderActionContainer(ActionConstants.SHOW_CAUSE, cc);
	container.addComponent(cc);

	UserActionList headerActions = getAvailableActions(entity, ActionLocations.HEADER);
	if (headerActions != null && headerActions.getActions() != null) {
	    // HorizontalLayout actionsContainer = new HorizontalLayout();
	    for (final UserAction ha : headerActions.getActions().values()) {

		/* create container for this action */
		ComponentContainer hac = (CssLayout) entity.getHeaderActionContainer(ha.getActionName());
		if (hac == null) {
		    hac = createHeaderContainer();
		    entity.addHeaderActionContainer(ha.getActionName(), hac);
		    // System.out.println("Adding header action: " + ha.getActionName());
		}
		container.addComponent(hac);
	    }
	}
    }

    private ComponentContainer createHeaderContainer() {
	CssLayout hac = new CssLayout();
//	hac.setWidth("100%");
	hac.addStyleName(OpenGroupsStyles.ENTITY_HEADER_ACTION_PANE);
	hac.setVisible(false);
	return hac;
    }

    private void addCausesFragment(ActionContext actionContext) {
	CssLayout container = (CssLayout) actionContext.getTargetContainer();
	Entity entity = actionContext.getEntity();

	ComboBox causesCombo = getCausesCombo(entity);

	if (causesCombo == null) {
	    return;
	}

	CssLayout causesContainer = new CssLayout();
//	causesContainer.setWidth("100%");
	causesContainer.addStyleName(OpenGroupsStyles.HEADER_BAR);
	container.addComponent(causesContainer);

	Button showCauseButton = getShowCauseButton(actionContext,causesCombo);
	updateCausesFragment(actionContext, showCauseButton);

	showCauseButton.addStyleName("middle-right left-margin-10");
	causesContainer.addComponent(showCauseButton);

//	causesCombo.setWidth("90%");
	causesCombo.addStyleName("middle-left");
	causesContainer.addComponent(causesCombo);

    }

    private ComboBox getCausesCombo(Entity entity) {

	List<EntityLink> causes = entity.getCauses();
	if (causes == null || causes.size() == 0) {
	    return null;
	}

	final ComboBox causesCombo = new ComboBox();
	causesCombo.setImmediate(true);
	causesCombo.setNullSelectionAllowed(true);
	causesCombo.setNewItemsAllowed(false);
	

	long selectedParentLinkId = -1;
	boolean parentLinkSet = false;
	EntityLink selectedCause = entity.getSelectedCause();
	if (selectedCause != null) {
	    selectedParentLinkId = selectedCause.getLinkId();
	}
	for (EntityLink el : causes) {
	    // System.out.println("adding cause " + el);
	    causesCombo.addItem(el);
	    causesCombo.setItemCaption(el, el.getParentTitle());
	    if (selectedParentLinkId > 0 && !parentLinkSet && el.getLinkId() == selectedParentLinkId) {
		causesCombo.select(el);
		parentLinkSet = true;
	    }
	}
	if (causesCombo.getValue() == null && causes.size() == 1) {
	    selectedCause = causes.get(0);
	    causesCombo.setNullSelectionAllowed(false);
	    causesCombo.select(selectedCause);
	    entity.setSelectedCause(selectedCause);

	}

//	causesCombo.setDescription("bla bla bla");
	
	return causesCombo;
    }

    private Button getShowCauseButton(final ActionContext context, final ComboBox causesCombo) {
	final Entity entity = context.getEntity();
	final Button actButton = new Button();
	actButton.addStyleName(BaseTheme.BUTTON_LINK);
	actButton.addListener(new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		EntityState state = entity.getState();
		if (state.isActionActive(ActionConstants.SHOW_CAUSE)) {
		    state.setActionInactive(ActionConstants.SHOW_CAUSE);
		    hideCause(context);
		    causesCombo.setVisible(true);
		} else {
		    state.setActionActive(ActionConstants.SHOW_CAUSE);
		    showCause(context);
		    causesCombo.setVisible(false);
		}
		updateCausesFragment(context, actButton);
	    }
	});
	return actButton;
    }

    private void updateCausesFragment(ActionContext context, Button actButton) {
	Entity entity = context.getEntity();
	EntityState state = entity.getState();
	String actionState = ActionConstants.SHOW_CAUSE;
	if (state.isActionActive(ActionConstants.SHOW_CAUSE)) {
	    actionState += ".on";
	} else {
	    actionState += ".off";
	}

	String caption = OpenGroupsResources.getMessage(actionState);
	actButton.setDescription(caption);
	actButton.setIcon(OpenGroupsResources.getIcon(actionState, OpenGroupsIconsSet.MEDIUM));
    }

    private void showCause(ActionContext context) {
	Entity entity = context.getEntity();
	EntityLink selectedCause = entity.getSelectedCause();
	ComponentContainer causeContainer = entity.getHeaderActionContainer(ActionConstants.SHOW_CAUSE);
	
	Entity causeEntity = new Entity(selectedCause.getParentId());
	causeEntity.getState().setEntityTypeVisible(true);	
	getActionsManager().executeAction(ActionsManager.OPEN_SELECTED_ENTITY_WITH_HEADER_ACTIONS,
		causeEntity, context.getApp(), causeContainer, false, context);
	causeContainer.setVisible(true);
	context.getApp().getActiveWindow().setScrollable(true);
	context.getApp().getActiveWindow().setScrollTop(0);
    }

    private void hideCause(ActionContext context) {
	Entity entity = context.getEntity();
	ComponentContainer causeContainer = entity.getHeaderActionContainer(ActionConstants.SHOW_CAUSE);
	causeContainer.setVisible(false);
	context.getApp().getActiveWindow().scrollIntoView(context.getTargetContainer());
    }

}
