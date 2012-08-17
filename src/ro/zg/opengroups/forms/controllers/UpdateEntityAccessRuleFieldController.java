package ro.zg.opengroups.forms.controllers;

import java.util.Set;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.vaadin.DefaultFormFieldController;
import ro.zg.netcell.vaadin.FormContext;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.opengroups.vo.AccessRule;
import ro.zg.opengroups.vo.Entity;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.ComboBox;

public class UpdateEntityAccessRuleFieldController extends
	DefaultFormFieldController<ComboBox> {

    public void initField(ComboBox field, FormContext formContext) {
	ActionContext ac = formContext.getActionContext();
	OpenGroupsApplication app = formContext.getApp();
	ApplicationConfigManager appCfg = app.getAppConfigManager();

	Set<AccessRule> allowedRules;
	Entity mainEntity = ac.getMainEntity();
	try {

	    allowedRules = app.getModel().getAllowedAccessRulesForEntity(
		    mainEntity.getId());
	} catch (ContextAwareException e) {
	    app.pushError(e);
	    return;
	}
	AccessRule currentRule = appCfg.getAccessRuleById(mainEntity
		.getAccessRuleId());
	allowedRules.add(currentRule);

	field.setImmediate(true);
	field.setTextInputAllowed(false);
	field.setNewItemsAllowed(false);
	field.setNullSelectionAllowed(false);

	field.setItemCaptionPropertyId("description");

	BeanContainer<Long, AccessRule> newDataSource = new BeanContainer<Long, AccessRule>(
		AccessRule.class);
	newDataSource.setBeanIdProperty("id");
	newDataSource.addAll(allowedRules);

	field.setContainerDataSource(newDataSource);
	field.setValue(currentRule.getId());

	if (allowedRules.size() == 1) {
	    field.setEnabled(false);
	}
    }
}
