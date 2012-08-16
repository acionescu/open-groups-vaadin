package ro.zg.opengroups.forms.controllers;

import java.util.Set;

import ro.zg.netcell.vaadin.DefaultFormFieldController;
import ro.zg.netcell.vaadin.FormContext;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.opengroups.vo.AccessRule;
import ro.zg.opengroups.vo.Entity;

import com.vaadin.ui.ComboBox;

public class AccessRuleFormFieldController extends
	DefaultFormFieldController<ComboBox> {

    public void initField(ComboBox field, FormContext formContext) {
	ApplicationConfigManager cfgManager = formContext.getApp().getAppConfigManager();
	ActionContext ac = formContext.getActionContext();
	Entity parentEntity = ac.getMainEntity();
	
	Set<AccessRule> allowedRules = cfgManager.getMoreRestrictiveAccessRules(parentEntity.getAccessRuleId());
    }

}
