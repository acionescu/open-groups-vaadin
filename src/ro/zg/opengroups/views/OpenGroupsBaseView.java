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
package ro.zg.opengroups.views;

import ro.zg.presentation.utils.BaseView;
import ro.zg.presentation.utils.UserEvent;
import ro.zg.presentation.utils.UserEventHandler;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public abstract class OpenGroupsBaseView<T> extends BaseView<T,UserEvent>{
    protected ComponentContainer container;
    
    public OpenGroupsBaseView(){
	container = new CssLayout();
    }
    
    public ComponentContainer getContainer() {
        return container;
    }

    public void setContainer(ComponentContainer container) {
        this.container = container;
    }
    
    
    
}
