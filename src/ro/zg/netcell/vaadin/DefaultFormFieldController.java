package ro.zg.netcell.vaadin;

import java.util.Map;

import ro.zg.netcell.vaadin.action.constants.ActionParamProperties;
import ro.zg.open_groups.OpenGroupsApplication;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

public class DefaultFormFieldController<F extends Field> implements FormFieldController<F> {

    @Override
    public F buildField(Map<String, String> fieldConfig,
	    FormContext formContext) {
	
	String formId=formContext.getFormId();
	OpenGroupsApplication app = formContext.getApp();
	F f = (F)getFieldForType(fieldConfig
		.get(ActionParamProperties.FIELD_UI_TYPE));

	String fieldName = fieldConfig.get(ActionParamProperties.NAME);
	f.setCaption(app.getMessage(formId + "." + fieldName + ".caption"));

	if ("true".equals(fieldConfig.get(ActionParamProperties.IS_REQUIRED))) {
	    f.setRequired(true);
	    f.setRequiredError(app.getMessage(formId + "." + fieldName
		    + ".required.error"));
	}
	if ("true".equals(fieldConfig.get(ActionParamProperties.IS_SENSITIVE))) {
	    ((TextField) f).setSecret(true);
	}
	Object value = fieldConfig.get(ActionParamProperties.VALUE);
	if (value != null) {
	    f.setValue(value);
	}
	String width = fieldConfig.get(ActionParamProperties.FIELD_WIDTH);
	String height = fieldConfig.get(ActionParamProperties.FIELD_HEIGHT);

	if (width != null) {
	    f.setWidth(width);
	}
	if (height != null) {
	    f.setHeight(height);
	}

	String inputRegex = fieldConfig
		.get(ActionParamProperties.FIELD_INPUT_REGEX);
	if (inputRegex != null) {
	    f.addValidator(new RegexpValidator(inputRegex, app.getMessage(formId + "." + fieldName + ".regex.error")));
	}

	return f;
    }

    private Field getFieldForType(String uiType) {
	if (uiType == null) {
	    return new TextField();
	}
	if (uiType.equals("richtextarea")) {
	    return new RichTextArea();
	}
	if (uiType.equals("checkbox")) {
	    return new CheckBox();
	}
	if (uiType.equals("combobox")) {
	    return new ComboBox();
	}
	if (uiType.equals("date")) {
	    final DateField df = new DateField();
	    df.setResolution(DateField.RESOLUTION_DAY);
	    return df;
	}
	return new TextField();
    }

    @Override
    public void initField(Field field, FormContext formContext) {
	// TODO Auto-generated method stub

    }

    @Override
    public Object getFieldData(Field field) {
	return field.getValue();
    }

}
