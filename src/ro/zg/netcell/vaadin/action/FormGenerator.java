/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
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
package ro.zg.netcell.vaadin.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import ro.zg.netcell.vaadin.ExtendedForm;
import ro.zg.netcell.vaadin.action.constants.ActionParamProperties;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

public class FormGenerator implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2306330779022587201L;
    private List<Map<String,String>> paramsConfigList;
    private ResourceBundle messages;
    private String formId;

    public FormGenerator(String formId, List<Map<String,String>> paramsConfigList, ResourceBundle messages) {
	this.formId = formId;
	this.paramsConfigList = paramsConfigList;
	this.messages = messages;
    }
    
    public ExtendedForm generate() {
	ExtendedForm form = new ExtendedForm();
	for(Map<String,String> paramConfig : paramsConfigList) {
	    String fieldName = paramConfig.get(ActionParamProperties.NAME);
	    Field f = getFieldFromParamsMap(paramConfig);
	    form.addField(fieldName, f);
	}
	form.setImmediate(true);
	String submitCaption = messages.getString(formId+".submit.caption");
	form.addSubmitButton(submitCaption);
	return form;
    }
    
    private Field getFieldFromParamsMap(Map<String,String> fieldConfig) {
	Field f = getFieldForType(fieldConfig.get(ActionParamProperties.FIELD_UI_TYPE));
	
	String fieldName = fieldConfig.get(ActionParamProperties.NAME);
	f.setCaption(messages.getString(formId+"."+fieldName+".caption"));
	
	if("true".equals(fieldConfig.get(ActionParamProperties.IS_REQUIRED))) {
	    f.setRequired(true);
	    f.setRequiredError(messages.getString(formId+"."+fieldName+".required.error"));
	}
	if("true".equals(fieldConfig.get(ActionParamProperties.IS_SENSITIVE))) {
	    ((TextField)f).setSecret(true);
	}
	Object value = fieldConfig.get(ActionParamProperties.VALUE);
	if(value != null) {
	    f.setValue(value);
	}
	String width = fieldConfig.get(ActionParamProperties.FIELD_WIDTH);
	String height = fieldConfig.get(ActionParamProperties.FIELD_HEIGHT);
	
	if(width != null) {
	    f.setWidth(width);
	}
	if(height != null) {
	    f.setHeight(height);
	}
	
	String inputRegex = fieldConfig.get(ActionParamProperties.FIELD_INPUT_REGEX);
	if(inputRegex != null) {
	    f.addValidator(new RegexpValidator(inputRegex, messages.getString(formId+"."+fieldName+".regex.error")));
	}
	
	return f;
    }
    
    private Field getFieldForType(String uiType) {
	if(uiType == null) {
	    return new TextField();
	}
	if(uiType.equals("richtextarea")) {
	    return new RichTextArea();
	}
	if(uiType.equals("checkbox")) {
	    return new CheckBox();
	}
	if(uiType.equals("combobox")) {
	    return new ComboBox();
	}
	if(uiType.equals("date")) {
	    final DateField df = new DateField();
            df.setResolution(DateField.RESOLUTION_DAY);
            return df;
	}
	return new TextField();
    }
}
