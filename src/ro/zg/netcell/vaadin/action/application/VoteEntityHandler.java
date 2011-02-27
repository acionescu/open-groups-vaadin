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

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
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
	    showAlreadyVotedFragment(container, selectedEntity,actionContext.getApp(),actionContext.getUserAction());
	} else {
	    showVotesFragment(container, selectedEntity,actionContext.getApp(),actionContext.getUserAction());
	}

    }

    private void showAlreadyVotedFragment(final ComponentContainer container, final Entity selectedEntity, final OpenGroupsApplication app, final UserAction ua) {
	String entityType = selectedEntity.getSimpleType().toLowerCase();
	String currentUserVote = selectedEntity.getUserData().getVote();
	String messageKey = entityType + ".already.voted." + currentUserVote;
	HorizontalLayout hl = new HorizontalLayout();
	hl.setSpacing(true);
	hl.addComponent(new Label(OpenGroupsResources.getMessage(messageKey)));

	Button changeVoteButton = new Button(OpenGroupsResources.getMessage("change.vote"));
	changeVoteButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		container.removeAllComponents();
		showVotesFragment(container, selectedEntity,app,ua);
	    }
	});
	hl.addComponent(changeVoteButton);

	Button recallVoteButton = new Button(OpenGroupsResources.getMessage("recall.vote"));
	recallVoteButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		Map<String, Object> params = ua.getActionParams();
		params.put("entityId", selectedEntity.getId());
		params.put("userId", app.getCurrentUserId());
		params.put("isRecordCreated",true);
		
		executeAction(new ActionContext(ua, app, selectedEntity), params);
//		app.refreshCurrentSelectedEntity();
		app.refreshEntity(selectedEntity);
//		try {
//		    handle(ua, app);
//		} catch (Exception e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
	    }
	});
	hl.addComponent(recallVoteButton);
	container.addComponent(hl);
    }

    private void showVotesFragment(ComponentContainer container, Entity selectedEntity,final OpenGroupsApplication app, final UserAction ua) {
	String entityType = selectedEntity.getSimpleType().toLowerCase();
	List<VoteType> voteTypes = VoteType.valuesList(entityType);
	HorizontalLayout votesContainer = new HorizontalLayout();
	votesContainer.setSpacing(true);
	for (VoteType vt : voteTypes) {
	    votesContainer.addComponent(getButtonForVoteType(vt, container,selectedEntity,app,ua));
	}
	container.addComponent(votesContainer);
    }

    private Button getButtonForVoteType(final VoteType voteType, final ComponentContainer container, final Entity selectedEntity, final OpenGroupsApplication app, final UserAction ua) {
	Button button = new Button(voteType.getCaption());
	button.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		
		
		Map<String, Object> params = ua.getActionParams();
		params.put("entityId", selectedEntity.getId());
		params.put("userId", app.getCurrentUserId());
		params.put("isRecordCreated",selectedEntity.getUserData().isRecordCreated());
		params.put("vote", voteType.getDbValue());
		executeAction(new ActionContext(ua, app, selectedEntity), params);
//		app.refreshCurrentSelectedEntity();
		app.refreshEntity(selectedEntity);
//		try {
//		    handle(ua, app);
//		} catch (Exception e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
		
	    }
	});
	return button;
    }
}
