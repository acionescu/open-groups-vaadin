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
package ro.zg.opengroups.vo;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.open_groups.model.OpenGroupsModel;

public class OpenGroupsApplicationState {

    private Entity rootEntity;
    private OpenGroupsMainWindow activeWindow;
    private Entity activeEntity;
    private String currentUri;
    private Entity desiredEntity;

    public OpenGroupsApplicationState() throws ContextAwareException {
	OpenGroupsModel model = OpenGroupsModel.getInstance();
	rootEntity = model.getRootEntity();

    }

    public boolean isRootSelected() {
	return (activeEntity != null && rootEntity.getId() == activeEntity
		.getId());
    }

    /**
     * @return the rootEntity
     */
    public Entity getRootEntity() {
	return rootEntity;
    }

    /**
     * @return the activeWindow
     */
    public OpenGroupsMainWindow getActiveWindow() {
	return activeWindow;
    }

    /**
     * @param activeWindow
     *            the activeWindow to set
     */
    public void setActiveWindow(OpenGroupsMainWindow activeWindow) {
	this.activeWindow = activeWindow;
    }

    /**
     * @return the activeEntity
     */
    public Entity getActiveEntity() {
	if (activeEntity == null) {
	    return rootEntity;
	}
	return activeEntity;
    }

    /**
     * @param activeEntity
     *            the activeEntity to set
     */
    public void setActiveEntity(Entity activeEntity) {
	this.activeEntity = activeEntity;
    }

    public String getCurrentUri() {
	return currentUri;
    }

    public void setCurrentUri(String currentUri) {
	this.currentUri = currentUri;
    }

    public Entity getDesiredEntity() {
	if (desiredEntity == null) {
	    return getActiveEntity();
	}
	return desiredEntity;
    }

    public void setDesiredEntity(Entity desiredEntity) {
	this.desiredEntity = desiredEntity;
    }

}
