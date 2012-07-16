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

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.components.CausalHierarchyContainer;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class CausalHierarchyStartDepthChangedListener implements ValueChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 5671642483629001741L;
    private static final Logger logger = MasterLogManager.getLogger("CausalHierarchyStartDepthChangedListener");
    
    private OpenGroupsApplication app;
    
    public CausalHierarchyStartDepthChangedListener(OpenGroupsApplication app) {
	this.app = app;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
	Object value = event.getProperty().getValue();
	if(value == null) {
	    logger.error("Null value for startDepth");
	    return;
	}
	int startDepth = (Integer)value;
	CausalHierarchyContainer chc = app.getActiveWindow().getHierarchyContainer();
	chc.setStartDepth(startDepth);
	ActionsManager.getInstance().executeAction(ActionsManager.REFRESH_CAUSAL_HIERARCHY, new ActionContext(app));
    }

   

}
