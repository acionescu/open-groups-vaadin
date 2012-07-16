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
package ro.zg.netcell.vaadin.action.application;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.gui.components.CausalHierarchyContainer;

public class ShowCausalHierarchyHandler extends OpenGroupsActionHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 7786208334443260694L;
    
    @Override
    public void handle(ActionContext actionContext) throws Exception {
	CausalHierarchyContainer hierarchyContainer = actionContext.getWindow().getHierarchyContainer();
	/* set max depth */
	long maxDepth = getModel().getHierarchyMaxDepth(actionContext.getApp().getRootEntity().getId());
	hierarchyContainer.setMaxDepth(maxDepth);
    }
    
}
