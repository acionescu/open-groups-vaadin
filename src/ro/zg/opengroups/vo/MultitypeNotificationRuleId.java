package ro.zg.opengroups.vo;

public class MultitypeNotificationRuleId {
    private int depth;
    private boolean enabled;
    private NotificationMode notificationMode;
    
    public MultitypeNotificationRuleId(int depth,
	    NotificationMode notificationMode, boolean enabled) {
	super();
	this.depth = depth;
	this.notificationMode = notificationMode;
	this.enabled = enabled;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
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
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + depth;
	result = prime * result + (enabled ? 1231 : 1237);
	result = prime
		* result
		+ ((notificationMode == null) ? 0 : notificationMode.hashCode());
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
	MultitypeNotificationRuleId other = (MultitypeNotificationRuleId) obj;
	if (depth != other.depth)
	    return false;
	if (enabled != other.enabled)
	    return false;
	if (notificationMode == null) {
	    if (other.notificationMode != null)
		return false;
	} else if (!notificationMode.equals(other.notificationMode))
	    return false;
	return true;
    }
    
    
}
