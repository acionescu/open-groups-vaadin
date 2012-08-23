package ro.zg.opengroups.forms.controllers;

import ro.zg.netcell.vaadin.DefaultFormController;

public class CreateEntityFormController extends DefaultFormController{

    public CreateEntityFormController() {
	super();
	addFieldController("access_rule_id", new CreateEntityAccessRuleFieldController());
    }
    
    

}
