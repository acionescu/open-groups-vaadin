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

import ro.zg.netcell.vaadin.action.constants.MessagesConstants;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.util.data.GenericNameValueContext;

public class NotificationMode {
    private long notificationModeId;
    private String mode;
    private String description;
    
    
    public NotificationMode(GenericNameValueContext data){
	notificationModeId = (Long)data.getValue("id");
	mode = (String)data.getValue("mode");
	description=OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_MODE_PREFIX+mode);
    }
    
    
    
    /**
     * @return the notificationModeId
     */
    public long getNotificationModeId() {
        return notificationModeId;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param notificationModeId the notificationModeId to set
     */
    public void setNotificationModeId(long notificationModeId) {
        this.notificationModeId = notificationModeId;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }



    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ (int) (notificationModeId ^ (notificationModeId >>> 32));
	return result;
    }



    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	NotificationMode other = (NotificationMode) obj;
	if (notificationModeId != other.notificationModeId)
	    return false;
	return true;
    }



    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return description;
    }
    
    
    
}
