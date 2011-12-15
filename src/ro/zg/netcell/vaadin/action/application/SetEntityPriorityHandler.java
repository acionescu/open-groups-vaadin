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

import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.constants.ApplicationConfigParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityUserData;
import ro.zg.opengroups.vo.Priority;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class SetEntityPriorityHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -3344992478487981373L;

    @Override
    public void handle(final ActionContext actionContext) throws Exception {
	HorizontalLayout parentContainer = (HorizontalLayout) actionContext.getTargetContainer();
	HorizontalLayout localContainer = new HorizontalLayout();
	localContainer.setSpacing(true);
	parentContainer.addComponent(localContainer);

	Entity entity = actionContext.getEntity();
	if (entity.getContent() != null) {
	    parentContainer.setComponentAlignment(localContainer, Alignment.MIDDLE_RIGHT);
	    displayCombo(actionContext, localContainer, actionContext);
	} else {
	    displayLabel(actionContext, localContainer);
	}
	/* display general priority */
	localContainer.addComponent(new Label("/"));
	String valueString = "-";
	if (entity.getGeneralPriority() != null) {
	    valueString = "#" + entity.getGeneralPriority();
	}
	Label generalPriority = new Label(valueString);
	localContainer.addComponent(generalPriority);
    }

    private void displayLabel(ActionContext actionContext, HorizontalLayout container) {
	Entity entity = actionContext.getEntity();
	EntityUserData userData = entity.getUserData();

	String valueString = "-";
	if (userData.getPriority() != null) {
	    valueString = "#" + userData.getPriority();
	}

	String priorityCaption = getMessage("priority");
	Label priorityLabel = new Label(priorityCaption + ":");
	// priorityLabel.addStyleName("stats-summary");
	// HorizontalLayout parentContainer = (HorizontalLayout)actionContext.getTargetContainer();
	container.addComponent(priorityLabel);
	container.addComponent(new Label(valueString));
    }

    private void displayCombo(final ActionContext actionContext, HorizontalLayout targetContainer,
	    final ActionContext ac) {
	long maxPriority = (Long) getAppConfigManager().getApplicationConfigParam(ApplicationConfigParam.MAX_PRIORITY);
	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setWidth("55px");

	for (int i = 1; i <= maxPriority; i++) {
	    Priority p = Priority.getPriority(i);
	    select.addItem(p);
	}

	final Entity entity = actionContext.getEntity();
	final EntityUserData userData = entity.getUserData();

	if (userData.getPriority() != null) {
	    select.setValue(Priority.getPriority(userData.getPriority()));
	}

	final UserAction ua = actionContext.getUserAction();
	final OpenGroupsApplication app = actionContext.getApp();
	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		// boolean actionAllowed = checkUserAllowedToExecuteAction(entity, app, ua);
		
		Priority selectedPriority = (Priority) event.getProperty().getValue();
		Long priority = null;
		if (selectedPriority != null) {
		    priority = selectedPriority.getPriority();
		}
		
		Long currentPriority = userData.getPriority();
		
		if(priority == null) {
		    if(currentPriority == null) {
			return;
		    }
		}
		else if(priority.equals(currentPriority)) {
		    return;
		}
		
		Map<String, Object> params = ua.getActionParams();
		params.put("entityId", entity.getId());
		params.put("userId", app.getCurrentUserId());
		params.put("priority", priority);
		params.put("isRecordCreated", userData.isEntityUserRecordCreated());
		CommandResponse resp = executeAction(actionContext, params);
		if (resp != null) {
		    app.refreshEntity(entity, ac);
		}
		else {
		    select.setValue(userData.getPriority());
		}
	    }
	});

	// GridLayout layout = new GridLayout(1, 1);
	// layout.setSizeFull();
	HorizontalLayout container = new HorizontalLayout();
	container.setSpacing(true);
	container.addComponent(new Label(getMessage("priority") + ":"));
	container.addComponent(select);
	// container.setComponentAlignment(select, Alignment.TOP_LEFT);

	targetContainer.addComponent(container);
	targetContainer.setComponentAlignment(container, Alignment.MIDDLE_RIGHT);

	// targetContainer.addStyleName("stats-summary");
	// targetContainer.addComponent(layout);
    }

}
