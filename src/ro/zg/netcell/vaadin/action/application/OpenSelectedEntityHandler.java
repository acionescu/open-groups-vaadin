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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import ro.zg.netcell.vaadin.FormatingUtils;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.gui.constants.OpenGroupsStyles;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.constants.ComplexEntityParam;
import ro.zg.opengroups.constants.Defaults;
import ro.zg.opengroups.constants.TypeRelationConfigParam;
import ro.zg.opengroups.util.OpenGroupsUtil;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityLink;
import ro.zg.opengroups.vo.EntityState;
import ro.zg.opengroups.vo.Tag;
import ro.zg.opengroups.vo.TypeRelationConfig;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;
import ro.zg.util.date.DateUtil;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
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
	ApplicationConfigManager appConfigManager = app.getAppConfigManager();
	List<String> subtyesList = appConfigManager
		.getSubtypesForComplexType(complexEntityType);

	// Panel currentContainer = (Panel) container;
	ComponentContainer currentContainer = container;
	entity.setEntityContainer(container);

	CssLayout headerContainer = new CssLayout();
	headerContainer.addStyleName("entity-header-container");
	currentContainer.addComponent(headerContainer);

	// CssLayout titleContainer = new CssLayout();
	// titleContainer.setWidth("80%");
	// titleContainer.addStyleName(OpenGroupsStyles.MIDDLE_LEFT);
	// headerContainer.addComponent(titleContainer);

	// titleContainer.setSizeFull();
	// /* show level */
	// String levelMsg = getMessage("level");
	// Label levelLabel = new Label(levelMsg+" "+entity.getDepth());
	// titleContainer.addComponent(levelLabel);

	/* show entity type */
	if (entity.getState().isEntityTypeVisible()) {
	    String entityTypeCaption = getMessage(entity.getComplexType());

	    String size = OpenGroupsIconsSet.MEDIUM;
	    if (entity.getContent() != null) {
		size = OpenGroupsIconsSet.LARGE;
	    }
	    // Label typeIcon = new Label();
	    // typeIcon.setDescription(entityTypeCaption);
	    // typeIcon.setIcon(OpenGroupsResources.getIcon(entity.getComplexType().toLowerCase(),
	    // size));
	    // typeIcon.setSizeUndefined();
	    // typeIcon.addStyleName("top-left right-margin-20");
	    // headerContainer.addComponent(typeIcon);
	    Embedded eicon = new Embedded(null, OpenGroupsResources.getIcon(
		    entity.getComplexType().toLowerCase(), size));
	    eicon.setDescription(entityTypeCaption);
	    eicon.addStyleName("top-left right-margin-20");

	    headerContainer.addComponent(eicon);

	    // Embedded eicon2 = new Embedded("",
	    // OpenGroupsResources.getIcon(entity.getComplexType().toLowerCase(),
	    // size));
	    // eicon2.setDescription(entityTypeCaption);
	    // eicon2.addStyleName("top-left right-margin-20");
	    // headerContainer.addComponent(eicon2);

	}

	// Label test = new Label("test");
	// test.setSizeUndefined();
	// test.addStyleName("top-left");
	// headerContainer.addComponent(test);

	/* add last action for this entity */
	if (entity.getLastActionType() != null) {
	    String actionType = entity.getLastActionType();
	    String msg = getMessage(actionType);
	    // Label actionLabel = new Label();
	    // actionLabel.setDescription(msg);
	    // actionLabel.setIcon(OpenGroupsResources.getIcon(actionType,
	    // OpenGroupsIconsSet.MEDIUM));
	    // actionLabel.setSizeUndefined();
	    // actionLabel.addStyleName(OpenGroupsStyles.MIDDLE_LEFT);
	    // titleContainer.addComponent(actionLabel);
	    Embedded actionIcon = new Embedded(null,
		    OpenGroupsResources.getIcon(actionType,
			    OpenGroupsIconsSet.MEDIUM));
	    actionIcon.setDescription(msg);
	    actionIcon.addStyleName("top-left right-margin-20");
	    headerContainer.addComponent(actionIcon);

	}

	EntityLink selectedCause = entity.getSelectedCause();
	/* display title */
	/* if the entity is open */
	/* treat leafs like all posts 21.08.2012 */
	if (isOpened /*
		      * || (subtyesList == null && entity.getLastActionType() ==
		      * null)
		      */) {
	    Label title = new Label(entity.getTitle());
	    if (isOpened) {
		title.addStyleName(OpenGroupsStyles.TITLE_LINK);
	    } else {
		title.addStyleName("list-issue-title");
	    }
	    // title.addStyleName("top-left");
	    // title.setSizeFull();
	    title.setWidth("80%");
	    // title.setHeight("100%");

	    headerContainer.addComponent(title);

	} else {
	    // Button titleLink = new Button(entity.getTitle());
	    // titleLink.setWidth("100%");
	    // titleLink.addStyleName(BaseTheme.BUTTON_LINK);
	    // titleLink.addStyleName("issue-title");
	    // titleLink.setDescription(entity.getContentPreview());
	    Label titleLink = null;
	    /* treat leafs like all posts 21.08.2012 */
	    // if (subtyesList != null) {
	    titleLink = OpenGroupsUtil.getLinkForEntity(entity, app);
	    titleLink.setDescription(entity.getContentPreview());
	    titleLink.addStyleName("list-issue-title");
	    // }
	    // /* is a leaf entity in the recent activity list */
	    // else {
	    // String parentTitle = selectedCause.getParentTitle();
	    // Entity parentEntity = new Entity(selectedCause.getParentId());
	    // parentEntity.setTitle(parentTitle);
	    //
	    // parentEntity.getState().setDesiredActionsPath(
	    // complexEntityType +
	    // Defaults.getDefaultActionForEntityType(complexEntityType));
	    // titleLink = OpenGroupsUtil.getLinkForEntity(parentEntity, app,
	    // entity.getTitle());
	    // titleLink.setDescription(entity.getContentPreview());
	    // titleLink.addStyleName("list-issue-title");
	    //
	    // }

	    // titleLink.setWidth("75%");
	    titleLink.addStyleName("top-left");
	    headerContainer.addComponent(titleLink);
	    // titleContainer.setExpandRatio(titleLink, 10f);
	    // currentContainer.addStyleName("list-item");
	}

	/* add parent link */
	if (selectedCause != null && !isOpened && entity.getContent() == null) {
	    // CssLayout parentInfoContainer = new CssLayout();
	    // parentInfoContainer.addStyleName(OpenGroupsStyles.TOP_RIGHT);
	    // headerContainer.addComponent(parentInfoContainer);
	    // headerContainer.setColumnExpandRatio(1, 1f);
	    // headerContainer.setComponentAlignment(parentInfoContainer,
	    // Alignment.TOP_RIGHT);
	    /* if parent entity is the current selected entity, say it */
	    Entity mainEntity = actionContext.getMainEntity();
	    if (selectedCause.getParentId() == mainEntity.getId()) {
		// String currentMsg = getMessage(mainEntity.getComplexType() +
		// ".current");
		// Label currentEntityLabel = new Label(currentMsg);
		// currentEntityLabel.setSizeUndefined();
		// currentEntityLabel.addStyleName("top-right");
		// headerContainer.addComponent(currentEntityLabel);
		// titleContainer.setExpandRatio(currentEntityLabel, 2f);
	    } else {/* add link to the parent entity */
		String parentTitle = selectedCause.getParentTitle();
		final Entity parentEntity = new Entity(
			selectedCause.getParentId());
		parentEntity.setTitle(parentTitle);

		Label parentLink = OpenGroupsUtil.getLinkForEntityWithImage(
			parentEntity, app, OpenGroupsResources.getIconPath(
				OpenGroupsIconsSet.PARENT,
				OpenGroupsIconsSet.MEDIUM));
		parentLink.setDescription(parentTitle);
		parentLink.setSizeUndefined();
		parentLink.addStyleName(OpenGroupsStyles.TOP_RIGHT);
		// CssLayout imgContainer = new CssLayout();
		// imgContainer.setWidth("24px");
		// imgContainer.setHeight("24px");
		// imgContainer.addStyleName(OpenGroupsStyles.TOP_RIGHT);
		// imgContainer.addComponent(parentLink);
		// parentInfoContainer.addComponent(imgContainer);
		headerContainer.addComponent(parentLink);
	    }
	}

	/* display header actions */
	if (isOpened) {
	    UserActionList headerActions = getAvailableActions(entity,
		    ActionLocations.HEADER);
	    boolean allowRefresh = appConfigManager
		    .getComplexEntityBooleanParam(entity.getComplexType(),
			    ComplexEntityParam.ALLOW_REFRESH);
	    boolean hasHeaderActions = headerActions != null
		    && headerActions.getActions() != null;
	    if (allowRefresh || hasHeaderActions) {
		HorizontalLayout actionsContainer = new HorizontalLayout();
		actionsContainer.addStyleName(OpenGroupsStyles.TOP_RIGHT);
		actionsContainer.setSpacing(true);
		// /* add refresh button */
		// if (allowRefresh) {
		// actionsContainer.addComponent(getRefreshButton(entity, app));
		// }
		// if (hasHeaderActions) {
		// for (final UserAction ha :
		// headerActions.getActions().values()) {
		// Button actButton = getButtonForAction(ha, entity, app);
		// actionsContainer.addComponent(actButton);
		// }
		// }
		headerContainer.addComponent(actionsContainer);
		// headerContainer.setColumnExpandRatio(1, 1f);
		// headerContainer.setComponentAlignment(actionsContainer,
		// Alignment.TOP_RIGHT);
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
		String tagsLabelString = app.getMessage("tags.label")
			+ tagsList;
		currentContainer.addComponent(new Label(tagsLabelString));
	    }
	}

	CssLayout statusPane = new CssLayout();
	statusPane.addStyleName("entity-status-pane");

	currentContainer.addComponent(statusPane);

	if (appConfigManager.getComplexEntityBooleanParam(complexEntityType,
		ComplexEntityParam.SHOW_POST_INFO)) {
	    String insertDateString = entity.getInsertDate().toString();
	    Label insertDate = new Label(app.getMessage("posted")
		    + DateUtil.removeNanos(insertDateString));
	    statusPane.addComponent(insertDate);
	    // statusPane.setColumnExpandRatio(0, 1f);
	}

	CssLayout statsSummaryPane = new CssLayout();
	statsSummaryPane.addStyleName("entity-stats-summary-pane");
	// statsSummaryPane.setSizeFull();
	// statsSummaryPane.setWidth("100%");

	currentContainer.addComponent(statsSummaryPane);

	/* add votes */
	if (appConfigManager.getComplexEntityBooleanParam(complexEntityType,
		ComplexEntityParam.ALLOW_VOTING)) {
	    // HorizontalLayout votesPane = new HorizontalLayout();
	    // votesPane.setMargin(false);
	    // votesPane.addStyleName("middle-left");
	    // votesPane.addStyleName("margin-right");
	    // statsSummaryPane.addComponent(votesPane);
	    // statsSummaryPane.setExpandRatio(votesPane, 1f);
	    // statsSummaryPane.setComponentAlignment(votesPane,
	    // Alignment.MIDDLE_LEFT);

	    Label votesLabel = new Label(app.getMessage("votes") + ":&nbsp;");
	    votesLabel.setContentMode(Label.CONTENT_XHTML);
	    votesLabel.setSizeUndefined();
	    votesLabel.addStyleName("middle-left");
	    // votesPane.addComponent(votesLabel);
	    Label proVotes = FormatingUtils.coloredLabel(entity.getProVotes(),
		    "336633");
	    proVotes.addStyleName("middle-left");
	    proVotes.setSizeUndefined();
	    // votesPane.addComponent(proVotes);
	    Label dash = new Label("-");
	    dash.addStyleName("middle-left");
	    dash.setSizeUndefined();
	    // votesPane.addComponent(new Label("-"));
	    Label opposedVotes = FormatingUtils.coloredLabel(
		    entity.getOpposedVotes(), "660000");
	    // votesPane.addComponent(opposedVotes);
	    opposedVotes.addStyleName("middle-left");
	    opposedVotes.addStyleName("right-margin-10");
	    opposedVotes.setSizeUndefined();

	    statsSummaryPane.addComponent(votesLabel);
	    statsSummaryPane.addComponent(proVotes);
	    statsSummaryPane.addComponent(dash);
	    statsSummaryPane.addComponent(opposedVotes);

	}
	List<TypeRelationConfig> subtypes = appConfigManager
		.getSubtypesForType(entity.getComplexTypeId());
	// if (subtyesList != null) {
	if (subtypes != null) {
	    Map<String, Long> firstLevelSubtypesCount = entity
		    .getSubtypeEntitiesCount();
	    Map<String, Long> allSubtypesCount = entity
		    .getRecursiveSubtypeEntitiesCount();

	    // for (String s : subtyesList) {
	    // String subtype = s.toLowerCase();
	    for (TypeRelationConfig trc : subtypes) {
		String targetComplexType = trc.getTargetComplexType();
		String subtype = targetComplexType.toLowerCase();
		String displayName = app.getMessage("subtype." + subtype);

		Entity refEntity = new Entity(entity.getId());

		refEntity
			.getState()
			.setDesiredActionsPath(
				targetComplexType
					+ Defaults
						.getDefaultActionForEntityType(targetComplexType));
		Label subtypeLink = OpenGroupsUtil.getLinkForEntity(refEntity,
			app, displayName);

		String valueString = ": "
			+ firstLevelSubtypesCount.get(subtype);

		// if (getAppConfigManager().getComplexEntityBooleanParam(s,
		// ComplexEntityParam.ALLOW_RECURSIVE_LIST)) {
		if (appConfigManager.getTypeRelationBooleanConfigParam(
			trc.getId(),
			TypeRelationConfigParam.ALLOW_RECURSIVE_LIST)) {
		    Long recCount = allSubtypesCount.get(subtype);
		    if (recCount != null) {
			valueString += " / " + recCount;
		    }
		}
		Label sl = new Label(valueString);
		sl.setWidth(null);
		sl.addStyleName("middle-left");
		sl.addStyleName("right-margin-10");

		subtypeLink.setWidth(null);
		subtypeLink.addStyleName("middle-left");

		statsSummaryPane.addComponent(subtypeLink);
		statsSummaryPane.addComponent(sl);
		// statsSummaryPane.setExpandRatio(sl, 1f);
		// statsSummaryPane.setComponentAlignment(sl,
		// Alignment.MIDDLE_LEFT);
	    }
	}

	displaySummaryActions(entity, app, statsSummaryPane, actionContext);

	// statusPane.addComponent(statsSummaryPane, 1, 0);
	// statusPane.setColumnExpandRatio(1, 1.8f);
	// statusPane.setComponentAlignment(statsSummaryPane,
	// Alignment.MIDDLE_RIGHT);

	// summaryLayout.setComponentAlignment(statsSummaryPane,
	// Alignment.MIDDLE_RIGHT);

	/* add the content */
	Object contentObj = entity.getContent();
	if (contentObj != null) {
	    // content.setSizeFull();
	    Panel contentContainer = new Panel();
	    // CssLayout contentContainer = new CssLayout();
	    contentContainer.addStyleName("text-content");
	    currentContainer.addComponent(contentContainer);

	    Label content = new Label(contentObj.toString());
	    content.setContentMode(Label.CONTENT_XHTML);
	    // content.addStyleName("text-label");
	    contentContainer.addComponent(content);

	    /* if the content is visible, display footer actions */
	    displayFooterActions(entity, app, currentContainer, actionContext);
	}

    }

    private void displayFooterActions(Entity entity, OpenGroupsApplication app,
	    ComponentContainer currentContainer, ActionContext ac) {
	UserActionList entityActions = getAvailableActions(entity,
		ActionLocations.FOOTER);
	UserActionList globalActions = getGlobalActions(ActionLocations.FOOTER);

	Collection<UserAction> allActions = getAllFooterActions(entityActions,
		globalActions);

	// if (entityActions == null || entityActions.getActions() == null ||
	// entityActions.getActions().size() == 0) {
	// return;
	// }

	if (allActions.size() == 0) {
	    return;
	}

	CssLayout actionsContainer = new CssLayout();

	boolean changeRow = true;
	int leftCount = 0;
	int rightCount = 0;

	CssLayout leftContainer = null;
	CssLayout rightContainer = null;
	CssLayout actionContainer = null;

	// for (UserAction ua : entityActions.getActions().values()) {
	for (UserAction ua : allActions) {
	    if (!ua.isVisible(ac)) {
		continue;
	    }
	    // HorizontalLayout actionContainer = new HorizontalLayout();
	    // actionContainer.addStyleName(OpenGroupsStyles.TOP_RIGHT);

	    CssLayout cc = new CssLayout();
	    boolean isLeft = false;

	    if (isLeft = "LEFT".equals(ua.getActionPath())) {

	    } else {
		if (rightCount > 0) {
		    changeRow = true;
		}

	    }

	    if (changeRow) {
		/*
		 * add the left/right containers to the previous action
		 * container
		 */
		if (actionContainer != null) {
		    if (leftCount > 0) {
			actionContainer.addComponent(leftContainer);
		    }
		    if (rightCount > 0) {
			actionContainer.addComponent(rightContainer);
		    }
		}

		actionContainer = new CssLayout();
		actionContainer
			.addStyleName(OpenGroupsStyles.FOOTER_ACTION_PANE);
		actionsContainer.addComponent(actionContainer);

		leftContainer = new CssLayout();
		leftContainer.addStyleName("middle-left");
		rightContainer = new CssLayout();
		rightContainer.addStyleName("middle-right");
		rightCount = 0;
		leftCount = 0;
		changeRow = false;
	    }

	    if (isLeft) {
		leftCount++;
		leftContainer.addComponent(cc);
		cc.addStyleName("middle-left");
	    } else {
		rightCount++;
		rightContainer.addComponent(cc);
	    }

	    ua.executeHandler(entity, app, cc, ac);
	}

	/* add these also for the last row */
	if (actionContainer != null) {
	    if (leftCount > 0) {
		actionContainer.addComponent(leftContainer);
	    }
	    if (rightCount > 0) {
		actionContainer.addComponent(rightContainer);
	    }
	}

	currentContainer.addComponent(actionsContainer);
    }

    private Collection<UserAction> getAllFooterActions(UserActionList local,
	    UserActionList global) {
	List<UserAction> result = new ArrayList<UserAction>();
	Deque<UserAction> localStack = new ArrayDeque<UserAction>();
	Deque<UserAction> globalStack = new ArrayDeque<UserAction>();

	if (global == null && local != null) {
	    getSimpleActions(local, localStack);
	    return localStack;
	} else if (local == null && global != null) {
	    getSimpleActions(global, globalStack);
	    return globalStack;
	} else if (local == null) { // both are null
	    return result;
	}

	getSimpleActions(local, localStack);
	getSimpleActions(global, globalStack);

	boolean localNotEmpty = false;
	boolean globalNotEmpty = false;
	while ((localNotEmpty = localStack.size() > 0)
		| (globalNotEmpty = globalStack.size() > 0)) {
	    long localOrder = Integer.MAX_VALUE;
	    long globalOrder = Integer.MAX_VALUE;
	    UserAction localAction = null;
	    UserAction globalAction = null;

	    if (localNotEmpty) {
		localAction = localStack.peek();
		localOrder = localAction.getOrderIndex();
	    }
	    if (globalNotEmpty) {
		globalAction = globalStack.peek();
		globalOrder = globalAction.getOrderIndex();
	    }

	    if (localOrder <= globalOrder) {
		result.add(localAction);
		localStack.pop();
	    } else {
		result.add(globalAction);
		globalStack.pop();
	    }
	}

	return result;
    }

    private void getSimpleActions(UserAction ua, Deque<UserAction> stack) {
	if (ua instanceof UserActionList) {
	    for (UserAction cua : ((UserActionList) ua).getActions().values()) {
		getSimpleActions(cua, stack);
	    }
	} else {
	    stack.add(ua);
	}
    }

    private void displaySummaryActions(Entity entity,
	    OpenGroupsApplication app, CssLayout currentContainer,
	    ActionContext ac) {
	UserActionList actions = getAvailableActions(entity,
		ActionLocations.SUMMARY);
	// ComponentContainer container = app.getTargetComponent();
	if (actions == null || actions.getActions() == null
		|| actions.getActions().size() == 0) {
	    return;
	}

	for (UserAction ua : actions.getActions().values()) {
	    CssLayout actionContainer = new CssLayout();
	    actionContainer.addStyleName("middle-right");
	    actionContainer.addStyleName("left-margin-20");
	    currentContainer.addComponent(actionContainer);
	    // currentContainer.setExpandRatio(actionContainer, 2f);
	    // currentContainer.setComponentAlignment(actionContainer,
	    // Alignment.MIDDLE_RIGHT);
	    ua.executeHandler(entity, app, actionContainer, ac);
	}
    }

    private Button getButtonForAction(final UserAction ha, final Entity entity,
	    final OpenGroupsApplication app, ActionContext ac) {
	if (entity.getState().isActionActive(ha.getActionName())) {
	    return getButtonForActiveAction(ha, entity, app, ac);
	}
	return getButtonForInactiveAction(ha, entity, app, ac);
    }

    private Button getButtonForInactiveAction(final UserAction ha,
	    final Entity entity, final OpenGroupsApplication app,
	    final ActionContext ac) {
	String actionState = ha.getActionName() + ".off";
	String caption = OpenGroupsResources.getMessage(actionState);
	Button actButton = new Button();
	actButton.setDescription(caption);
	actButton.setIcon(OpenGroupsResources.getIcon(actionState,
		OpenGroupsIconsSet.MEDIUM));
	actButton.addStyleName(BaseTheme.BUTTON_LINK);
	actButton.addListener(new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		// ha.executeHandler(entity,app,app.getTemporaryTab(entity).getContainer());
		CssLayout hac = (CssLayout) entity.getHeaderActionContainer(ha
			.getActionName());
		hac.setVisible(true);
		ha.executeHandler(entity, app, hac, ac);
		// app.refreshEntity(entity);
		refreshHeaderActionLinks(entity, app, ac);
	    }
	});
	return actButton;
    }

    private Button getButtonForActiveAction(final UserAction ha,
	    final Entity entity, final OpenGroupsApplication app,
	    final ActionContext ac) {

	String actionState = ha.getActionName() + ".on";
	String caption = OpenGroupsResources.getMessage(actionState);
	Button actButton = new Button();
	actButton.setDescription(caption);
	actButton.setIcon(OpenGroupsResources.getIcon(actionState,
		OpenGroupsIconsSet.MEDIUM));
	actButton.addStyleName(BaseTheme.BUTTON_LINK);
	actButton.addListener(new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		entity.getState().setActionInactive(ha.getActionName());
		// app.getTemporaryTab(entity).setRefreshOn(true);
		// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_IN_TAB,
		// entity,app,null, false);
		CssLayout hac = (CssLayout) entity.getHeaderActionContainer(ha
			.getActionName());
		hac.setVisible(false);
		// app.refreshEntity(entity);
		refreshHeaderActionLinks(entity, app, ac);
	    }
	});
	return actButton;
    }

    private void refreshHeaderActionLinks(Entity entity,
	    OpenGroupsApplication app, ActionContext ac) {
	// UserActionList headerActions = getAvailableActions(entity,
	// ActionLocations.HEADER);
	// if (headerActions != null && headerActions.getActions() != null) {
	// HorizontalLayout actionsContainer = (HorizontalLayout)
	// entity.getHeaderActionLinksContainer();
	// actionsContainer.removeAllComponents();
	// for (final UserAction ha : headerActions.getActions().values()) {
	// Button actButton = getButtonForAction(ha, entity, app);
	// actionsContainer.addComponent(actButton);
	// }
	// }

	UserActionList headerActions = getAvailableActions(entity,
		ActionLocations.HEADER);
	boolean allowRefresh = app.getAppConfigManager()
		.getComplexEntityBooleanParam(entity.getComplexType(),
			ComplexEntityParam.ALLOW_REFRESH);
	boolean hasHeaderActions = headerActions != null
		&& headerActions.getActions() != null;
	if (allowRefresh || hasHeaderActions) {
	    HorizontalLayout actionsContainer = (HorizontalLayout) entity
		    .getHeaderActionLinksContainer();
	    actionsContainer.removeAllComponents();
	    /* add refresh button */
	    if (allowRefresh) {
		actionsContainer
			.addComponent(getRefreshButton(entity, app, ac));
	    }
	    if (hasHeaderActions) {
		for (final UserAction ha : headerActions.getActions().values()) {
		    Button actButton = getButtonForAction(ha, entity, app, ac);
		    actionsContainer.addComponent(actButton);
		}
	    }

	}

    }

    private Button getRefreshButton(final Entity entity,
	    final OpenGroupsApplication app, final ActionContext ac) {
	Button refreshButton = new Button();
	refreshButton.setDescription(getMessage("refresh.entity"));
	refreshButton.setIcon(OpenGroupsResources.getIcon(
		OpenGroupsIconsSet.REFRESH, OpenGroupsIconsSet.MEDIUM));
	refreshButton.addStyleName(BaseTheme.BUTTON_LINK);

	refreshButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		// app.getTemporaryTab(entity).setRefreshOn(true);
		// getActionsManager().executeAction(ActionsManager.OPEN_ENTITY_IN_TAB,
		// entity, app, null, false);
		app.fullyRefreshEntity(entity, ac);
	    }
	});
	return refreshButton;
    }
}
