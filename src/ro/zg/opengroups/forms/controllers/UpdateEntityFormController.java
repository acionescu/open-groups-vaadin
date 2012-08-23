package ro.zg.opengroups.forms.controllers;

import ro.zg.netcell.vaadin.DefaultFormController;

public class UpdateEntityFormController extends DefaultFormController{

    public UpdateEntityFormController() {
	super();
	addFieldController("access_rule_id", new UpdateEntityAccessRuleFieldController());
    }
    
    

}
