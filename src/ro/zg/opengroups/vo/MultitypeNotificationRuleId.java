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

import java.util.Collection;
import java.util.TreeSet;

public class MultitypeNotificationRuleId {
    private DepthValue depth;
    private boolean enabled;
    private NotificationMode notificationMode;
    private Collection<ActionType> selectedActionTypes;
    
    public MultitypeNotificationRuleId(DepthValue depth,
	    NotificationMode notificationMode, boolean enabled, Collection<ActionType> selectedActionTypes) {
	super();
	this.depth = depth;
	this.notificationMode = notificationMode;
	this.enabled = enabled;
	this.selectedActionTypes=selectedActionTypes;
    }
    public DepthValue getDepth() {
        return depth;
    }
    public void setDepth(DepthValue depth) {
        this.depth = depth;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public NotificationMode getNotificationMode() {
        return notificationMode;
    }
    public void setNotificationMode(NotificationMode notificationMode) {
        this.notificationMode = notificationMode;
    }
    
    
    
    
    /**
     * @return the selectedActionTypes
     */
    public Collection<ActionType> getSelectedActionTypes() {
        return selectedActionTypes;
    }
    /**
     * @param selectedActionTypes the selectedActionTypes to set
     */
    public void setSelectedActionTypes(Collection<ActionType> selectedActionTypes) {
        this.selectedActionTypes = new TreeSet<ActionType>(selectedActionTypes);
    }
    
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((depth == null) ? 0 : depth.hashCode());
	result = prime * result + (enabled ? 1231 : 1237);
	result = prime * result + ((notificationMode == null) ? 0 : notificationMode.hashCode());
	result = prime * result + ((selectedActionTypes == null) ? 0 : selectedActionTypes.hashCode());
	return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	MultitypeNotificationRuleId other = (MultitypeNotificationRuleId) obj;
	if (depth == null) {
	    if (other.depth != null)
		return false;
	} else if (!depth.equals(other.depth))
	    return false;
	if (enabled != other.enabled)
	    return false;
	if (notificationMode == null) {
	    if (other.notificationMode != null)
		return false;
	} else if (!notificationMode.equals(other.notificationMode))
	    return false;
	if (selectedActionTypes == null) {
	    if (other.selectedActionTypes != null)
		return false;
	} else if (!selectedActionTypes.equals(other.selectedActionTypes))
	    return false;
	return true;
    }
    
    
}
