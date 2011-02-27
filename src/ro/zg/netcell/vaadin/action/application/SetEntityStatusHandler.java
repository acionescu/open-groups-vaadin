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
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityUserData;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class SetEntityStatusHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 7471747418439820053L;

    @Override
    public void handle(final ActionContext actionContext) throws Exception {
	HorizontalLayout parentContainer = (HorizontalLayout) actionContext.getTargetContainer();
	HorizontalLayout localContainer = new HorizontalLayout();
	localContainer.setSpacing(true);
	parentContainer.addComponent(localContainer);

	Entity entity = actionContext.getEntity();
	if (entity.getContent() != null) {
	    parentContainer.setComponentAlignment(localContainer, Alignment.MIDDLE_RIGHT);
	    displayCombo(actionContext, localContainer);
	} else {
	    displayLabel(actionContext, localContainer);
	}

	/* display most used status */
	localContainer.addComponent(new Label("/"));
	String valueString = "-";
	if (entity.getGeneralStatus() != null) {
	    valueString = entity.getGeneralStatus();
	}
	Label generalStatus = new Label(valueString);
	generalStatus.setSizeFull();
	localContainer.addComponent(generalStatus);
    }

    private void displayLabel(ActionContext actionContext, HorizontalLayout container) {
	Entity entity = actionContext.getEntity();
	EntityUserData userData = entity.getUserData();

	String valueString = ": -";
	if (userData.getStatus() != null) {
	    valueString = ": " + userData.getStatus();
	}
	String statusCaption = getMessage("status");
	Label statusLabel = new Label(statusCaption + valueString);
	statusLabel.addStyleName("stats-summary");
	// actionContext.getTargetContainer().addComponent(statusLabel);
	container.addComponent(statusLabel);
    }

    private void displayCombo(final ActionContext actionContext, HorizontalLayout targetContainer) {
	CommandResponse response = executeAction(ActionsManager.GET_STATUSES, new HashMap<String, Object>());
	GenericNameValueList list = (GenericNameValueList) response.getValue("result");

	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setNewItemsAllowed(true);
	select.setWidth("110px");

	for (int i = 0; i < list.size(); i++) {
	    GenericNameValueContext row = (GenericNameValueContext) list.getValueForIndex(i);
	    select.addItem(row.getValue("status"));
	}

	final Entity entity = actionContext.getEntity();
	final EntityUserData userData = entity.getUserData();
	final UserAction ua = actionContext.getUserAction();
	final OpenGroupsApplication app = actionContext.getApp();

	if (userData.getStatus() != null) {
	    select.setValue(userData.getStatus());
	}

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {

		String value = ((String) select.getValue());
		if (value != null) {
		    value=value.trim();
		    if ("".equals(value)) {
			value = null;
		    }
		    if (value.length() > 0) {
			value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
		    }
		}
		Map<String, Object> params = ua.getActionParams();
		params.put("entityId", entity.getId());
		params.put("userId", app.getCurrentUserId());
		params.put("status", value);
		params.put("isRecordCreated", userData.isRecordCreated());
		executeAction(actionContext, params);
		userData.setRecordCreated(true);
		app.refreshEntity(entity);
	    }
	});

	// GridLayout layout = new GridLayout(1, 1);
	// layout.setSizeFull();
	HorizontalLayout container = new HorizontalLayout();
	container.setSpacing(true);
	container.addComponent(new Label(getMessage("status") + ":"));
	container.addComponent(select);

	targetContainer.addComponent(container);
	// targetContainer.setComponentAlignment(container, Alignment.MIDDLE_RIGHT);

	// ComponentContainer parentContainer = actionContext.getTargetContainer();
	// parentContainer.removeAllComponents();
	// layout.addStyleName("stats-summary");
	// parentContainer.addComponent(layout);

    }

}
