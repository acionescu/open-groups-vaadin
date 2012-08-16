package ro.zg.netcell.vaadin;

import java.util.List;
import java.util.Map;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.open_groups.OpenGroupsApplication;

public class FormContext {
    private String formId;
    private List<Map<String,String>> fieldsConfigList;
    private ActionContext actionContext;
    
    public FormContext(String formId,
	    List<Map<String, String>> fieldsConfigList,
	    ActionContext actionContext) {
	super();
	this.formId = formId;
	this.fieldsConfigList = fieldsConfigList;
	this.actionContext = actionContext;
    }
    public String getFormId() {
        return formId;
    }
    public void setFormId(String formId) {
        this.formId = formId;
    }
    public List<Map<String, String>> getFieldsConfigList() {
        return fieldsConfigList;
    }
    public void setFieldsConfigList(List<Map<String, String>> fieldsConfigList) {
        this.fieldsConfigList = fieldsConfigList;
    }
    public ActionContext getActionContext() {
        return actionContext;
    }
    public void setActionContext(ActionContext actionContext) {
        this.actionContext = actionContext;
    }
    
    public OpenGroupsApplication getApp(){
	return actionContext.getApp();
    }
}
