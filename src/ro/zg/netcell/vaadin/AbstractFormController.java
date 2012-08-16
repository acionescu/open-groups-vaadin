package ro.zg.netcell.vaadin;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;

public abstract class AbstractFormController implements FormController {
    private Map<String, FormFieldController<? extends Field>> fieldControllers = new HashMap<String, FormFieldController<? extends Field>>();
    private FormFieldController<Field> defaultFormFieldController = new DefaultFormFieldController();

    @Override
    public ExtendedForm createForm(FormContext formContext) {
	ExtendedForm form = buildForm(formContext);
	initForm(form, formContext);
	return form;
    }

    protected abstract ExtendedForm buildForm(FormContext formContext);
    
    protected void initForm(ExtendedForm form, FormContext formContext) {
	for(Object fieldId : form.getItemPropertyIds()) {
	    initFormField(fieldId.toString(), form.getField(fieldId),formContext);
	}
    }

    protected void addFieldController(String fieldId,
	    FormFieldController<? extends Field> fieldController) {
	fieldControllers.put(fieldId, fieldController);
    }

    protected Field buildField(String fieldId, Map<String, String> fieldConfig, FormContext formContext) {
	FormFieldController<? extends Field> ffc = fieldControllers
		.get(fieldId);
	if (ffc != null) {
	    return ffc.buildField(fieldConfig,formContext);
	}
	return defaultFormFieldController.buildField(fieldConfig,formContext);
    }
    
    private Object getFieldData(String fieldId, Field field){
	FormFieldController ffc = fieldControllers
		.get(fieldId);
	if (ffc != null) {
	    return ffc.getFieldData(field);
	}
	return defaultFormFieldController.getFieldData(field);
    }
    
    protected void initFormField(String fieldId, Field field, FormContext formContext){
	FormFieldController ffc = fieldControllers
		.get(fieldId);
	if (ffc != null) {
	    ffc.initField(field, formContext);
	    return;
	}
	defaultFormFieldController.initField(field, formContext);
    }

    @Override
    public Map<String, Object> getFormData(Form form) {
	HashMap<String, Object> map = new HashMap<String, Object>();
	for(Object fieldId : form.getItemPropertyIds()) {
	    String fieldIdString = fieldId.toString();
	    map.put(fieldIdString, getFieldData(fieldIdString, form.getField(fieldId)));
	}
	return map;
    }

}
