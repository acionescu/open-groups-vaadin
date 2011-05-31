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

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.gui.constants.OpenGroupsStyles;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.VoteType;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityUserData;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

public class VoteEntityHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -3409894199633395023L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	// TODO Auto-generated method stub

	ComponentContainer container = actionContext.getTargetContainer();
	container.removeAllComponents();
	/*
	 * Entity selectedEntity = application.getSelectedEntity(); String entityType =
	 * selectedEntity.getSimpleType().toLowerCase(); String voteCaption = application.getMessage(entityType +
	 * ".vote");
	 * 
	 * final Form votesForm = new Form(); final OptionGroup votesChoices = new OptionGroup(null,
	 * VoteType.valuesList(entityType)); votesChoices.setRequired(true);
	 * votesChoices.setRequiredError(application.getMessage("vote.required.error")); votesForm.addField("vote",
	 * votesChoices);
	 * 
	 * container.addComponent(votesForm);
	 * 
	 * Button voteButton = new Button(voteCaption); container.addComponent(voteButton); voteButton.addListener(new
	 * ClickListener() {
	 * 
	 * @Override public void buttonClick(ClickEvent event) { try { votesForm.commit(); System.out.println("Vote: " +
	 * votesChoices.getValue()); } catch (Validator.InvalidValueException e) {
	 * 
	 * } } });
	 */
	Entity selectedEntity = actionContext.getEntity();
	EntityUserData userData = selectedEntity.getUserData();
	if (userData.getVote() != null) {
	    showAlreadyVotedFragment(container, selectedEntity, actionContext.getApp(), actionContext.getUserAction(),
		    actionContext);
	} else {
	    showVotesFragment(container, selectedEntity, actionContext.getApp(), actionContext.getUserAction(),
		    actionContext);
	}

    }

    private void showAlreadyVotedFragment(final ComponentContainer container, final Entity selectedEntity,
	    final OpenGroupsApplication app, final UserAction ua, final ActionContext ac) {
	String entityType = selectedEntity.getSimpleType().toLowerCase();
	String currentUserVote = selectedEntity.getUserData().getVote();
	String messageKey = entityType + ".already.voted." + currentUserVote;
	HorizontalLayout hl = new HorizontalLayout();
	hl.addStyleName(OpenGroupsStyles.TOP_RIGHT);
	hl.setSpacing(true);
	hl.addComponent(new Label(OpenGroupsResources.getMessage(messageKey)));

	final VoteType opposedVote = VoteType.opposteVoteForValue(currentUserVote, entityType);
	
	Button changeVoteButton = new Button(/*OpenGroupsResources.getMessage("change.vote")*/);
	changeVoteButton.setCaption(opposedVote.getCaption());
	changeVoteButton.setIcon(opposedVote.getIcon());
	changeVoteButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
//		container.removeAllComponents();
//		showVotesFragment(container, selectedEntity, app, ua, ac);
		Map<String, Object> params = ua.getActionParams();
		params.put("entityId", selectedEntity.getId());
		params.put("userId", app.getCurrentUserId());
		params.put("isRecordCreated", selectedEntity.getUserData().isRecordCreated());
		params.put("vote", opposedVote.getValue());
		CommandResponse response = executeAction(new ActionContext(ua, app, selectedEntity), params);
		/* refresh the entity only if the user was actually able to vote */
		if (response != null) {
		    app.refreshEntity(selectedEntity, ac);
		}
	    }
	});
	hl.addComponent(changeVoteButton);

	Button recallVoteButton = new Button(OpenGroupsResources.getMessage("recall.vote"));
	recallVoteButton.setIcon(OpenGroupsResources.getIcon(OpenGroupsIconsSet.CANCEL, OpenGroupsIconsSet.SMALL));
	recallVoteButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		Map<String, Object> params = ua.getActionParams();
		params.put("entityId", selectedEntity.getId());
		params.put("userId", app.getCurrentUserId());
		params.put("isRecordCreated", true);

		CommandResponse response = executeAction(new ActionContext(ua, app, selectedEntity), params);
		/* refresh the entity only if the user was actually able to vote */
		if (response != null) {
		    app.refreshEntity(selectedEntity, ac);
		}
	    }
	});
	hl.addComponent(recallVoteButton);
	container.addComponent(hl);
    }

    private void showVotesFragment(ComponentContainer container, Entity selectedEntity,
	    final OpenGroupsApplication app, final UserAction ua, final ActionContext ac) {
	String entityType = selectedEntity.getSimpleType().toLowerCase();
	List<VoteType> voteTypes = VoteType.valuesList(entityType);
	HorizontalLayout votesContainer = new HorizontalLayout();
	votesContainer.addStyleName(OpenGroupsStyles.TOP_RIGHT);
	votesContainer.setSpacing(true);
	for (VoteType vt : voteTypes) {
	    votesContainer.addComponent(getButtonForVoteType(vt, container, selectedEntity, app, ua, ac));
	}
	container.addComponent(votesContainer);
    }

    private Button getButtonForVoteType(final VoteType voteType, final ComponentContainer container,
	    final Entity selectedEntity, final OpenGroupsApplication app, final UserAction ua, final ActionContext ac) {
	Button button = new Button();
	button.setCaption(voteType.getCaption());
	button.setIcon(voteType.getIcon());
//	button.setDescription(voteType.getCaption());
	button.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {

		Map<String, Object> params = ua.getActionParams();
		params.put("entityId", selectedEntity.getId());
		params.put("userId", app.getCurrentUserId());
		params.put("isRecordCreated", selectedEntity.getUserData().isRecordCreated());
		params.put("vote", voteType.getValue());
		CommandResponse response = executeAction(new ActionContext(ua, app, selectedEntity), params);
		/* refresh the entity only if the user was actually able to vote */
		if (response != null) {
		    app.refreshEntity(selectedEntity, ac);
		}
	    }
	});
	return button;
    }
}
