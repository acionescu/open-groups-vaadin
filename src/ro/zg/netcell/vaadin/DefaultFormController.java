package ro.zg.netcell.vaadin;

import java.util.Map;

import ro.zg.netcell.vaadin.action.constants.ActionParamProperties;
import ro.zg.open_groups.OpenGroupsApplication;

import com.vaadin.ui.Field;

public class DefaultFormController extends AbstractFormController{
    
    @Override
    protected ExtendedForm buildForm(FormContext formContext){
	String formId=formContext.getFormId();
	OpenGroupsApplication app = formContext.getApp();
	ExtendedForm form = new ExtendedForm(formId,this);
	for(Map<String,String> fieldConfig : formContext.getFieldsConfigList()) {
	    String fieldName = fieldConfig.get(ActionParamProperties.NAME);
	    Field f = buildField(fieldName, fieldConfig,formContext);
	    form.addField(fieldName, f);
	}
	form.setImmediate(true);
	String submitCaption =  app.getMessage(formId+".submit.caption");
	form.addSubmitButton(submitCaption);
	return form;
    }

}
