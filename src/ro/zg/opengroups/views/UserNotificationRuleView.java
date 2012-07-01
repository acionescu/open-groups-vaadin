package ro.zg.opengroups.views;

import java.util.Collection;

import ro.zg.opengroups.vo.ActionType;
import ro.zg.opengroups.vo.DepthValue;
import ro.zg.opengroups.vo.MultitypeNotificationRule;
import ro.zg.opengroups.vo.NotificationMode;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

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

	displayActionTypesCombo(rule);
	displayDepthField(rule);
	displayNotificationModesCombo(rule);
	displayEnabledCheckBox(rule);
    }

    private void displayActionTypesCombo(final MultitypeNotificationRule rule) {

	// final ComboBox select = new ComboBox();
	final OptionGroup select = new OptionGroup();
	select.setImmediate(true);
	select.setMultiSelect(true);
	// select.setWidth("100%");
	select.setWidth("90px");
	CssLayout cell = new CssLayout();
	cell.addStyleName("notification-rules-list-row-cell");
	cell.addComponent(select);

	for (ActionType at : rule.getAvailableActionTypes()) {
	    select.addItem(at);
	}

	select.setValue(rule.getSelectedActionTypes());
	// container.addComponent(cell);
	addToContainer(cell);

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		Collection<ActionType> selectedActions = (Collection<ActionType>) event.getProperty().getValue();
		rule.setSelectedActionTypes(selectedActions);
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
	select.addStyleName("notification-rules-list-row-cell-content");
	select.setWidth("150px");
	CssLayout cell = new CssLayout();
	cell.addStyleName("notification-rules-list-row-cell");
	cell.addComponent(select);

	for (DepthValue d : rule.getAvailableDepths()) {
	    select.addItem(d);
	}

	select.setValue(rule.getCommonFields().getDepth());

	addToContainer(cell);

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		rule.getCommonFields().setDepth((DepthValue) event.getProperty().getValue());
	    }
	});
    }

    private void displayNotificationModesCombo(final MultitypeNotificationRule rule) {
	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setNullSelectionAllowed(false);
	select.addStyleName("notification-rules-list-row-cell-content");
	select.setWidth("110px");
	CssLayout cell = new CssLayout();
	cell.addStyleName("notification-rules-list-row-cell");
	cell.addComponent(select);

	for (NotificationMode nm : rule.getAvailableNotificationModes()) {
	    select.addItem(nm);
	}

	select.setValue(rule.getCommonFields().getNotificationMode());

	addToContainer(cell);

	select.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		rule.getCommonFields().setNotificationMode((NotificationMode) event.getProperty().getValue());
	    }
	});
    }

    private void displayEnabledCheckBox(final MultitypeNotificationRule rule) {
	CheckBox checkBox = new CheckBox();
	checkBox.setImmediate(true);
	checkBox.setValue(rule.getCommonFields().isEnabled());
	checkBox.addStyleName("notification-rules-list-row-cell-content");
	CssLayout cell = new CssLayout();
	cell.addStyleName("notification-rules-list-row-cell");
	cell.addComponent(checkBox);

	// container.addComponent(cell);
	addToContainer(cell);

	checkBox.addListener(new ValueChangeListener() {

	    @Override
	    public void valueChange(ValueChangeEvent event) {
		rule.getCommonFields().setEnabled((Boolean) event.getProperty().getValue());
	    }
	});
    }

    private void addToContainer(ComponentContainer cell) {
	container.addComponent(cell);
	HorizontalLayout hl = (HorizontalLayout) container;

	hl.setComponentAlignment(cell, Alignment.MIDDLE_LEFT);
	hl.setExpandRatio(cell, 1f);

    }
}
