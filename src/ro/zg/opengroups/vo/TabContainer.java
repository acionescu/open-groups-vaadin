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
package ro.zg.opengroups.vo;

import java.io.Serializable;

import com.vaadin.ui.ComponentContainer;

public class TabContainer implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -6304793139182908440L;
    private ComponentContainer container;
    private boolean newlyCreated;
    private boolean refreshOn;
    
    public TabContainer() {
	
    }
    
    public TabContainer(ComponentContainer c, boolean isNew) {
	container = c;
	newlyCreated = isNew;
    }
        
    
    /**
     * @return the container
     */
    public ComponentContainer getContainer() {
        return container;
    }
    /**
     * @return the newlyCreated
     */
    public boolean isNewlyCreated() {
        return newlyCreated;
    }
    /**
     * @param container the container to set
     */
    public void setContainer(ComponentContainer container) {
        this.container = container;
    }
    /**
     * @param newlyCreated the newlyCreated to set
     */
    public void setNewlyCreated(boolean newlyCreated) {
        this.newlyCreated = newlyCreated;
    }

    /**
     * @return the refreshOn
     */
    public boolean isRefreshOn() {
        return refreshOn;
    }

    /**
     * @param refreshOn the refreshOn to set
     */
    public void setRefreshOn(boolean refreshOn) {
        this.refreshOn = refreshOn;
    }
    
}
