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
import java.util.Map;

import ro.zg.netcell.vaadin.FormatingUtils;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.constants.OpenGroupsStyles;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.constants.ComplexEntityParam;
import ro.zg.opengroups.util.OpenGroupsUtil;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityState;
import ro.zg.opengroups.vo.Tag;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;
import ro.zg.util.date.DateUtil;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

public class OpenSelectedEntityHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -8991643289191224479L;

    @Override
    public void handle(final ActionContext actionContext) throws Exception {
	final ComponentContainer container = actionContext.getTargetContainer();

	container.removeAllComponents();
	// container.setSizeFull();
	// container.setMargin(false);
	final OpenGroupsApplication app = actionContext.getApp();
	final Entity entity = actionContext.getEntity();
	EntityState entityState = entity.getState();
	boolean isOpened = entityState.isOpened();
	String complexEntityType = entity.getComplexType();
	List<String> subtyesList = getAppConfigManager().getSubtypesForComplexType(complexEntityType);

	// Panel currentContainer = (Panel) container;
	ComponentContainer currentContainer = container;
	currentContainer.setWidth("100%");
	entity.setEntityContainer(container);

	GridLayout headerContainer = new GridLayout(2, 1);
	// headerContainer.setSizeFull();
	headerContainer.setWidth("100%");
	currentContainer.addComponent(headerContainer);

	HorizontalLayout titleContainer = new HorizontalLayout();
	// titleContainer.setSizeFull();
	titleContainer.setWidth("100%");
	// titleContainer.setWidth("800px");
	// titleContainer.setHeight("100%");
	titleContainer.setSpacing(true);

	headerContainer.addComponent(titleContainer, 0, 0);
	headerContainer.setComponentAlignment(titleContainer, Alignment.MIDDLE_LEFT);
	headerContainer.setColumnExpandRatio(0, 4f);
	// titleContainer.setSizeFull();
	// /* show level */
	// String levelMsg = getMessage("level");
	// Label levelLabel = new Label(levelMsg+" "+entity.getDepth());
	// titleContainer.addComponent(levelLabel);

	/* show entity type */
	String entityTypeCaption = "";
	if (entity.getState().isEntityTypeVisible()) {
	    entityTypeCaption = "(" + getMessage(entity.getComplexType()) + ")";
	    Label etl = new Label(entityTypeCaption);
	    etl.addStyleName(OpenGroupsStyles.ENTITY_TYPE);
	    // etl.setSizeFull();
	    etl.setWidth("100%");
	    titleContainer.addComponent(etl);
	    titleContainer.setExpandRatio(etl, 1.2f);
	}

	/* add last action for this entity */
	if (entity.getLastActionType() != null) {
	    String actionType = entity.getLastActionType();
	    String msg = "";
	    // if (actionType.equals(ActionTypes.CREATE)) {
	    msg = getMessage(actionType);
	    Label actionLabel = new Label(msg);
	    // actionLabel.setContentMode(Label.CONTENT_XHTML);
	    titleContainer.addComponent(actionLabel);
	    titleContainer.setExpandRatio(actionLabel, 1f);

	    // } else if (actionType.equals(ActionTypes.UPDATE)) {
	    // msg = getMessage(actionType);
	    // Label actionLabel = new Label(msg);
	    // titleContainer.addComponent(actionLabel);
	    // titleContainer.setExpandRatio(actionLabel, 1f);
	    // }

	}

	final Long parentEntityId = entity.getParentEntityId();
	/* display title */
	/* if the entity is opened or it is a leaf entity, and it is not displayed in the recent activity list */
	if (isOpened || (subtyesList == null && parentEntityId < 0)) {
	    CssLayout vl = new CssLayout();
	    // vl.setSizeFull();
	    vl.setWidth("100%");
	    titleContainer.addComponent(vl);
	    titleContainer.setComponentAlignment(vl, Alignment.MIDDLE_LEFT);
	    titleContainer.setExpandRatio(vl, 10f);

	    Label title = new Label(entity.getTitle());
	    title.setStyleName(OpenGroupsStyles.TITLE_LINK);
	    // title.setSizeFull();
	    title.setWidth("100%");

	    vl.addComponent(title);
	    // vl.setSizeFull();
	    // vl.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

	    if (subtyesList == null) {
		currentContainer.addStyleName("list-item");
	    }

	} else {
	    // Button titleLink = new Button(entity.getTitle());
	    // titleLink.setWidth("100%");
	    // titleLink.addStyleName(BaseTheme.BUTTON_LINK);
	    // titleLink.addStyleName("issue-title");
	    // titleLink.setDescription(entity.getContentPreview());
	    Label titleLink = null;
	    if (subtyesList != null) {
		// titleLink.addListener(new ClickListener() {
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// entity.getState().setEntityTypeVisible(true);
		// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_IN_TAB, entity, app, null,
		// false,actionContext);
		// }
		// });
		titleLink = OpenGroupsUtil.getLinkForEntity(entity, app);
		titleLink.setDescription(entity.getContentPreview());
		titleLink.addStyleName(OpenGroupsStyles.TITLE_LINK);
	    }
	    /* is a leaf entity in the recent activity list */
	    else {

		// titleLink.addListener(new ClickListener() {
		//
		// @Override
		// public void buttonClick(ClickEvent event) {
		// Entity parentEntity = app.getOpenEntityForId(parentEntityId);
		// if (parentEntity == null) {
		// String parentTitle = entity.getParentEntityTitle();
		// parentEntity = new Entity(parentEntityId);
		// parentEntity.setTitle(parentTitle);
		// }
		// parentEntity.getState().setEntityTypeVisible(true);
		// parentEntity.getState().setDesiredActionsPath(entity.getComplexType() + "/LIST");
		//			
		// /* open the parent entity, and the tab where the entities of this type are listed */
		// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_IN_TAB, parentEntity, app, null,
		// false,actionContext);
		// }
		// });
		String parentTitle = entity.getParentEntityTitle();
		Entity parentEntity = new Entity(parentEntityId);
		parentEntity.setTitle(parentTitle);
		parentEntity.getState().setDesiredActionsPath(entity.getComplexType() + "/LIST");
		titleLink = OpenGroupsUtil.getLinkForEntity(parentEntity, app, entity.getTitle());
		titleLink.setDescription(entity.getContentPreview());
		titleLink.addStyleName(OpenGroupsStyles.TITLE_LINK);
	    }
	    titleContainer.addComponent(titleLink);
	    titleContainer.setExpandRatio(titleLink, 10f);
	    currentContainer.addStyleName("list-item");
	}

	/* add parent link */
	if (parentEntityId > 0) {
	    HorizontalLayout parentInfoContainer = new HorizontalLayout();
	    headerContainer.addComponent(parentInfoContainer);
	    headerContainer.setComponentAlignment(parentInfoContainer, Alignment.MIDDLE_RIGHT);
	    /* if parent entity is the current selected entity, say it */
	    Entity mainEntity = actionContext.getMainEntity();
	    if (parentEntityId == mainEntity.getId()) {
		String currentMsg = getMessage(mainEntity.getComplexType() + ".current");
		Label currentEntityLabel = new Label(currentMsg);
//		currentEntityLabel.addStyleName("issue-title");
		parentInfoContainer.addComponent(currentEntityLabel);
		// titleContainer.setExpandRatio(currentEntityLabel, 2f);
	    } else {/* add link to the parent entity */
		String parentTitle = entity.getParentEntityTitle();
		final Entity parentEntity = new Entity(parentEntityId);
		parentEntity.setTitle(parentTitle);
//		Button parentLink = new Button(getMessage("parent"));
//		parentLink.setDescription(parentTitle);
//		parentLink.addStyleName(BaseTheme.BUTTON_LINK);
//		parentLink.addStyleName("issue-title");
//		parentLink.addListener(new ClickListener() {
//
//		    @Override
//		    public void buttonClick(ClickEvent event) {
//			parentEntity.getState().setEntityTypeVisible(true);
//			getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_IN_TAB, parentEntity, app, null,
//				false, actionContext);
//		    }
//		});
		Label parentLink = OpenGroupsUtil.getLinkForEntity(parentEntity, app, getMessage("parent"));
		parentLink.setDescription(parentTitle);
		parentInfoContainer.addComponent(parentLink);
	    }
	}

	/* display header actions */
	if (isOpened) {
	    UserActionList headerActions = getAvailableActions(entity, ActionLocations.HEADER);
	    boolean allowRefresh = getAppConfigManager().getComplexEntityBooleanParam(entity.getComplexType(),
		    ComplexEntityParam.ALLOW_REFRESH);
	    boolean hasHeaderActions = headerActions != null && headerActions.getActions() != null;
	    if (allowRefresh || hasHeaderActions) {
		HorizontalLayout actionsContainer = new HorizontalLayout();
		// actionsContainer.addStyleName("with-left-margin");
		actionsContainer.setSpacing(true);
		// /* add refresh button */
		// if (allowRefresh) {
		// actionsContainer.addComponent(getRefreshButton(entity, app));
		// }
		// if (hasHeaderActions) {
		// for (final UserAction ha : headerActions.getActions().values()) {
		// Button actButton = getButtonForAction(ha, entity, app);
		// actionsContainer.addComponent(actButton);
		// }
		// }
		headerContainer.addComponent(actionsContainer, 1, 0);
		headerContainer.setComponentAlignment(actionsContainer, Alignment.MIDDLE_RIGHT);
		entity.setHeaderActionLinksContainer(actionsContainer);
		refreshHeaderActionLinks(entity, app, actionContext);
	    }

	}

	/* display tags */
	List<Tag> tags = entity.getTags();
	if (tags != null) {
	    if (tags.size() > 0) {
		String tagsList = "";
		for (int i = 0; i < tags.size(); i++) {
		    Tag tag = tags.get(i);
		    tagsList += tag.getTagName();
		    if ((tags.size() - i) > 1) {
			tagsList += ", ";
		    }
		}
		String tagsLabelString = app.getMessage("tags.label") + tagsList;
		currentContainer.addComponent(new Label(tagsLabelString));
	    }
	}

	GridLayout statusPane = new GridLayout(2, 1);
	// statusPane.setSizeFull();
	statusPane.setWidth("100%");
	currentContainer.addComponent(statusPane);

	if (getAppConfigManager().getComplexEntityBooleanParam(complexEntityType, ComplexEntityParam.SHOW_POST_INFO)) {
	    String insertDateString = entity.getInsertDate().toString();
	    Label insertDate = new Label(app.getMessage("posted") + DateUtil.removeNanos(insertDateString));
	    statusPane.addComponent(insertDate, 0, 0);
	    statusPane.setColumnExpandRatio(0, 1f);
	}

	GridLayout summaryLayout = new GridLayout();
	// summaryLayout.setSizeFull();
	summaryLayout.setWidth("100%");
	currentContainer.addComponent(summaryLayout);

	HorizontalLayout statsSummaryPane = new HorizontalLayout();
	// statsSummaryPane.setSizeFull();
	statsSummaryPane.setWidth("100%");

	summaryLayout.addComponent(statsSummaryPane, 0, 0);
	summaryLayout.setColumnExpandRatio(1, 1.8f);

	/* add votes */
	if (getAppConfigManager().getComplexEntityBooleanParam(complexEntityType, ComplexEntityParam.ALLOW_VOTING)) {
	    HorizontalLayout votesPane = new HorizontalLayout();
	    votesPane.setMargin(false);
	    votesPane.addStyleName("stats-summary");
	    statsSummaryPane.addComponent(votesPane);
	    statsSummaryPane.setExpandRatio(votesPane, 1f);
	    statsSummaryPane.setComponentAlignment(votesPane, Alignment.MIDDLE_LEFT);

	    Label votesLabel = new Label(app.getMessage("votes") + ":&nbsp;");
	    votesLabel.setContentMode(Label.CONTENT_XHTML);
	    votesPane.addComponent(votesLabel);
	    Label proVotes = FormatingUtils.coloredLabel(entity.getProVotes(), "336633");
	    votesPane.addComponent(proVotes);
	    votesPane.addComponent(new Label("-"));
	    Label opposedVotes = FormatingUtils.coloredLabel(entity.getOpposedVotes(), "660000");
	    votesPane.addComponent(opposedVotes);
	}

	if (subtyesList != null) {
	    Map<String, Long> firstLevelSubtypesCount = entity.getSubtypeEntitiesCount();
	    Map<String, Long> allSubtypesCount = entity.getRecursiveSubtypeEntitiesCount();

	    for (String s : subtyesList) {
		String subtype = s.toLowerCase();
		String displayName = app.getMessage("subtype." + subtype);
		String displayString = displayName + ": " + firstLevelSubtypesCount.get(subtype);

		if (getAppConfigManager().getComplexEntityBooleanParam(s, ComplexEntityParam.ALLOW_RECURSIVE_LIST)) {
		    Long recCount = allSubtypesCount.get(subtype);
		    if (recCount != null) {
			displayString += "/" + recCount;
		    }
		}
		Label sl = new Label(displayString);
		sl.setWidth(null);
		sl.addStyleName("stats-summary");
		statsSummaryPane.addComponent(sl);
		statsSummaryPane.setExpandRatio(sl, 1f);
		statsSummaryPane.setComponentAlignment(sl, Alignment.MIDDLE_LEFT);
	    }
	}

	displaySummaryActions(entity, app, statsSummaryPane, actionContext);

	// statusPane.addComponent(statsSummaryPane, 1, 0);
	// statusPane.setColumnExpandRatio(1, 1.8f);
	// statusPane.setComponentAlignment(statsSummaryPane, Alignment.MIDDLE_RIGHT);

	// summaryLayout.setComponentAlignment(statsSummaryPane, Alignment.MIDDLE_RIGHT);

	/* add the content */
	Object contentObj = entity.getContent();
	if (contentObj != null) {
	    // content.setSizeFull();
	    Panel contentContainer = new Panel();
	    contentContainer.addStyleName("text-content");
	    currentContainer.addComponent(contentContainer);
	    // contentContainer.setWidth("100%");

	    Label content = new Label(contentObj.toString());
	    content.setContentMode(Label.CONTENT_XHTML);
	    contentContainer.addComponent(content);

	    /* if the content is visible, display footer actions */
	    displayFooterActions(entity, app, currentContainer, actionContext);
	}

    }

    private void displayFooterActions(Entity entity, OpenGroupsApplication app, ComponentContainer currentContainer,
	    ActionContext ac) {
	UserActionList actions = getAvailableActions(entity, ActionLocations.FOOTER);

	if (actions == null || actions.getActions() == null || actions.getActions().size() == 0) {
	    return;
	}
	GridLayout actionsContainer = new GridLayout(1, actions.getActions().size());
	// actionsContainer.setSizeFull();
	actionsContainer.setWidth("100%");
	currentContainer.addComponent(actionsContainer);
	int row = 0;
	for (UserAction ua : actions.getActions().values()) {
	    HorizontalLayout actionContainer = new HorizontalLayout();

	    actionsContainer.addComponent(actionContainer, 0, row++);
	    actionsContainer.setComponentAlignment(actionContainer, Alignment.MIDDLE_RIGHT);
	    // app.setTargetComponent(actionContainer);
	    ua.executeHandler(entity, app, actionContainer, ac);
	}

	// app.setTargetComponent(container);
    }

    private void displaySummaryActions(Entity entity, OpenGroupsApplication app, HorizontalLayout currentContainer,
	    ActionContext ac) {
	UserActionList actions = getAvailableActions(entity, ActionLocations.SUMMARY);
	// ComponentContainer container = app.getTargetComponent();
	if (actions == null || actions.getActions() == null || actions.getActions().size() == 0) {
	    return;
	}

	for (UserAction ua : actions.getActions().values()) {
	    HorizontalLayout actionContainer = new HorizontalLayout();
	    currentContainer.addComponent(actionContainer);
	    currentContainer.setExpandRatio(actionContainer, 2f);
	    currentContainer.setComponentAlignment(actionContainer, Alignment.MIDDLE_RIGHT);
	    ua.executeHandler(entity, app, actionContainer, ac);
	}
    }

    private Button getButtonForAction(final UserAction ha, final Entity entity, final OpenGroupsApplication app,
	    ActionContext ac) {
	if (entity.getState().isActionActive(ha.getActionName())) {
	    return getButtonForActiveAction(ha, entity, app, ac);
	}
	return getButtonForInactiveAction(ha, entity, app, ac);
    }

    private Button getButtonForInactiveAction(final UserAction ha, final Entity entity,
	    final OpenGroupsApplication app, final ActionContext ac) {
	String caption = OpenGroupsResources.getMessage(ha.getActionName() + ".off");
	Button actButton = new Button(caption);
	actButton.addStyleName(BaseTheme.BUTTON_LINK);
	actButton.addListener(new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		// ha.executeHandler(entity,app,app.getTemporaryTab(entity).getContainer());
		CssLayout hac = (CssLayout) entity.getHeaderActionContainer(ha.getActionName());
		ha.executeHandler(entity, app, hac, ac);
		hac.setVisible(true);
		// app.refreshEntity(entity);
		refreshHeaderActionLinks(entity, app, ac);
	    }
	});
	return actButton;
    }

    private Button getButtonForActiveAction(final UserAction ha, final Entity entity, final OpenGroupsApplication app,
	    final ActionContext ac) {
	String caption = OpenGroupsResources.getMessage(ha.getActionName() + ".on");
	Button actButton = new Button(caption);
	actButton.addStyleName(BaseTheme.BUTTON_LINK);
	actButton.addListener(new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		entity.getState().setActionInactive(ha.getActionName());
		// app.getTemporaryTab(entity).setRefreshOn(true);
		// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_IN_TAB, entity,app,null, false);
		CssLayout hac = (CssLayout) entity.getHeaderActionContainer(ha.getActionName());
		hac.setVisible(false);
		// app.refreshEntity(entity);
		refreshHeaderActionLinks(entity, app, ac);
	    }
	});
	return actButton;
    }

    private void refreshHeaderActionLinks(Entity entity, OpenGroupsApplication app, ActionContext ac) {
	// UserActionList headerActions = getAvailableActions(entity, ActionLocations.HEADER);
	// if (headerActions != null && headerActions.getActions() != null) {
	// HorizontalLayout actionsContainer = (HorizontalLayout) entity.getHeaderActionLinksContainer();
	// actionsContainer.removeAllComponents();
	// for (final UserAction ha : headerActions.getActions().values()) {
	// Button actButton = getButtonForAction(ha, entity, app);
	// actionsContainer.addComponent(actButton);
	// }
	// }

	UserActionList headerActions = getAvailableActions(entity, ActionLocations.HEADER);
	boolean allowRefresh = getAppConfigManager().getComplexEntityBooleanParam(entity.getComplexType(),
		ComplexEntityParam.ALLOW_REFRESH);
	boolean hasHeaderActions = headerActions != null && headerActions.getActions() != null;
	if (allowRefresh || hasHeaderActions) {
	    HorizontalLayout actionsContainer = (HorizontalLayout) entity.getHeaderActionLinksContainer();
	    actionsContainer.removeAllComponents();
	    /* add refresh button */
	    if (allowRefresh) {
		actionsContainer.addComponent(getRefreshButton(entity, app, ac));
	    }
	    if (hasHeaderActions) {
		for (final UserAction ha : headerActions.getActions().values()) {
		    Button actButton = getButtonForAction(ha, entity, app, ac);
		    actionsContainer.addComponent(actButton);
		}
	    }

	}

    }

    private Button getRefreshButton(final Entity entity, final OpenGroupsApplication app, final ActionContext ac) {
	Button refreshButton = new Button(getMessage("refresh.entity"));
	refreshButton.addStyleName(BaseTheme.BUTTON_LINK);

	refreshButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		// app.getTemporaryTab(entity).setRefreshOn(true);
		// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_IN_TAB, entity, app, null, false);
		app.fullyRefreshEntity(entity, ac);
	    }
	});
	return refreshButton;
    }
}
