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
package ro.zg.netcell.vaadin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.netcell.constants.InputParameterType;
import ro.zg.netcell.vo.InputParameter;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class DataTranslationUtils {

    public static ObjectProperty inputParameterToObjectProperty(InputParameter param) {
	Object value = param.getValue();
	if(value == null) {
	    value = "";
	}
	return new ObjectProperty(value, InputParameterType.toJavaClass(param.getType()));
    }

    public static PropertysetItem inputParameterListToPropertysetItem(List<InputParameter> list) {
	PropertysetItem psi = new PropertysetItem();
	for (InputParameter p : list) {
	    psi.addItemProperty(p.getName(), inputParameterToObjectProperty(p));
	}

	return psi;
    }

    public static Form createFormFromInputParameterList(List<InputParameter> list) {
	final Form form = new Form();
	form.setItemDataSource(inputParameterListToPropertysetItem(list));
	for (InputParameter p : list) {
	    Field f = form.getField(p.getName());
	    f.setRequired(p.isMandatory());
	}
	form.setWidth("40%");
	form.setHeight("30%");
	form.getLayout().setMargin(true);
	Button submitButton = new Button("Submit");
	form.getFooter().addComponent(submitButton);
	submitButton.addListener(new ClickListener() {
	    
	    @Override
	    public void buttonClick(ClickEvent event) {
		form.commit();
	    }
	});
	Button discardButton = new Button("Discard");
	form.getFooter().addComponent(discardButton);
	discardButton.addListener(new ClickListener() {
	    
	    @Override
	    public void buttonClick(ClickEvent event) {
		form.discard();
	    }
	});
	form.setImmediate(true);
	
	return form;
    }

    public static Map<String,Object> getFormFieldsAsMap(Form form){
	HashMap<String, Object> map = new HashMap<String, Object>();
	for(Object fieldId : form.getItemPropertyIds()) {
	    map.put(fieldId.toString(), form.getItemProperty(fieldId).getValue());
	}
	return map;
    }
}
