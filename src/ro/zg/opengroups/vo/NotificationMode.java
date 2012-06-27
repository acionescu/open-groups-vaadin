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
    
    
    
}
