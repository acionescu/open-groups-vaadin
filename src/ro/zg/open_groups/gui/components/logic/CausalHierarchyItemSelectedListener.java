/*******************************************************************************
 * Copyright 2012 AdrianIonescu
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
package ro.zg.open_groups.gui.components.logic;

import ro.zg.open_groups.OpenGroupsApplication;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class CausalHierarchyItemSelectedListener implements ValueChangeListener {
    /**
     * 
     */
    private static final long serialVersionUID = 5677068202956133395L;
    private OpenGroupsApplication app;

    public CausalHierarchyItemSelectedListener(OpenGroupsApplication app) {
	this.app = app;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
	Long value = (Long) event.getProperty().getValue();
	/* default to root entity */
	if (value == null) {
//	    value = app.getRootEntity().getId();
	    return;
	}
	/* open entity if not already selected */
	if (app.getActiveEntity().getId() != value) {
	    app.openEntityById(value, null, 1);
	}

    }

}
