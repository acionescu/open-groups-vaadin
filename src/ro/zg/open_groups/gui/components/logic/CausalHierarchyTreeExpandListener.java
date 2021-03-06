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

import java.util.HashMap;
import java.util.Map;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;

import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;

public class CausalHierarchyTreeExpandListener implements ExpandListener{

    /**
     * 
     */
    private static final long serialVersionUID = -7047467220617199044L;

    @Override
    public void nodeExpand(ExpandEvent event) {
	Object itemId = event.getItemId();
	OpenGroupsApplication app = (OpenGroupsApplication)event.getComponent().getApplication();
	Map<String,Object> params = new HashMap<String, Object>();
	params.put("parentId", itemId);
	params.put("refresh", false);
	params.put("startDepth", 0);
	ActionsManager.getInstance().executeAction(ActionsManager.REFRESH_CAUSAL_HIERARCHY, new ActionContext(app,params));
    }

}
