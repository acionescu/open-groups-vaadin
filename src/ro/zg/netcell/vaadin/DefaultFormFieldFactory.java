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

import ro.zg.netcell.constants.InputParameterUiType;
import ro.zg.netcell.vo.InputParameter;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.RichTextArea;

public class DefaultFormFieldFactory implements FormFieldFactory{
    
    
    private Map<String,InputParameter> paramsUiType = new HashMap<String, InputParameter>();
    
    public DefaultFormFieldFactory(List<InputParameter> params) {
	for(InputParameter p : params) {
	    paramsUiType.put(p.getName(), p);
	}
    }
    
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
	String paramUiType = paramsUiType.get((String)propertyId).getUiType().toString();
	if(paramUiType.startsWith("TEXTAREA_50")) {
	    Field f = new RichTextArea();
	    f.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
	    f.setSizeFull();
	    return f;
	}
	Field f = DefaultFieldFactory.get().createField(item, propertyId, uiContext);
	if(paramUiType.startsWith("TEXTAREA_5")) {
	    f.setSizeFull();
	}
	return f;
    }

}
