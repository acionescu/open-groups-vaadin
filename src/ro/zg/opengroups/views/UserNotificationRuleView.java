/*******************************************************************************
 * Copyright 2012 AdrianIonescu
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
package ro.zg.opengroups.views;

import java.util.Collection;

import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.vo.ActionType;
import ro.zg.opengroups.vo.DepthValue;
import ro.zg.opengroups.vo.MultitypeNotificationRule;
import ro.zg.opengroups.vo.NotificationMode;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;

public class UserNotificationRuleView extends OpenGroupsBaseView<MultitypeNotificationRule> {

    /**
     * 
     */
    private static final long serialVersionUID = -7542521734104929869L;

    public UserNotificationRuleView() {
	container = new HorizontalLayout();
	container.setWidth("100%");
    }

    public void update(MultitypeNotificationRule rule) {

	container.removeAllComponents();

	displayActionTypesOptions(rule);
	displayDepthField(rule);
	displayNotificationModesCombo(rule);
	displayEnabledCheckBox(rule);
	displayDeleteRuleButton(rule);
    }

    private void displayActionTypesOptions(final MultitypeNotificationRule rule) {

	// final ComboBox select = new ComboBox();
	final OptionGroup select = new OptionGroup();
	select.setImmediate(true);
	select.setMultiSelect(true);
	// select.setWidth("100%");
	select.setWidth("90px");
//	select.setSizeUndefined();
	

	for (ActionType at : rule.getAvailableActionTypes()) {
	    select.addItem(at);
	}

	select.setValue(rule.getSelectedActionTypes());
	// container.addComponent(cell);
	addToContainer(select);

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		Collection<ActionType> selectedActions = (Collection<ActionType>) event.getProperty().getValue();
		rule.setSelectedActionTypes(selectedActions);
		rule.getRulesList().onRuleUpdated();
	    }
	});
    }

    private void displayDepthField(final MultitypeNotificationRule rule) {

	// TextField field = new TextField();
	// field.setWidth("50px");
	// field.addStyleName("notification-rules-list-row-cell-content");
	// CssLayout cell = new CssLayout();
	// cell.addStyleName("notification-rules-list-row-cell");
	// cell.addComponent(field);
	//
	// field.setValue(rule.getCommonFields().getDepth());
	//
	// addToContainer(cell);

	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setNullSelectionAllowed(false);
//	select.addStyleName("notification-rules-list-row-cell-content");
	select.setWidth("150px");
	

	for (DepthValue d : rule.getAvailableDepths()) {
	    select.addItem(d);
	}

	select.setValue(rule.getCommonFields().getDepth());

	addToContainer(select);

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		rule.getCommonFields().setDepth((DepthValue) event.getProperty().getValue());
		rule.getRulesList().onRuleUpdated();
	    }
	});
    }

    private void displayNotificationModesCombo(final MultitypeNotificationRule rule) {
	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setNullSelectionAllowed(false);
//	select.addStyleName("notification-rules-list-row-cell-content");
	select.setWidth("150px");
	
	for (NotificationMode nm : rule.getAvailableNotificationModes()) {
	    select.addItem(nm);
	}

	select.setValue(rule.getCommonFields().getNotificationMode());

	addToContainer(select);

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		rule.getCommonFields().setNotificationMode((NotificationMode) event.getProperty().getValue());
		rule.getRulesList().onRuleUpdated();
	    }
	});
    }

    private void displayEnabledCheckBox(final MultitypeNotificationRule rule) {
	CheckBox checkBox = new CheckBox();
	checkBox.setImmediate(true);
	checkBox.setValue(rule.getCommonFields().isEnabled());
	checkBox.addStyleName("notification-rules-list-row-cell-content");
	

	// container.addComponent(cell);
	addToContainer(checkBox);

	checkBox.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		rule.getCommonFields().setEnabled((Boolean) event.getProperty().getValue());
		rule.getRulesList().onRuleUpdated();
	    }
	});
    }
    
    private void displayDeleteRuleButton(final MultitypeNotificationRule rule){
	final Button button = new Button();
	button.setDescription(OpenGroupsResources.getMessage("notification.rules.list.delete.rule"));
	button.setIcon(OpenGroupsResources.getIcon(OpenGroupsIconsSet.CANCEL,OpenGroupsIconsSet.SMALL));
	CssLayout cell = addToContainer(button);
	cell.setStyleName("notification-rules-list-row-cell-delete-rule");
	
	button.addListener(new ClickListener() {
	    
	    @Override
	    public void buttonClick(ClickEvent event) {
		rule.delete();
	    }
	});
    }

    private CssLayout addToContainer(Component component) {
	CssLayout cell = new CssLayout();
	cell.addStyleName("notification-rules-list-row-cell");
	cell.setMargin(true);
	cell.addComponent(component);
	container.addComponent(cell);
	HorizontalLayout hl = (HorizontalLayout) container;

	hl.setComponentAlignment(cell, Alignment.MIDDLE_LEFT);
	hl.setExpandRatio(cell, 1f);
	return cell;
    }
}
