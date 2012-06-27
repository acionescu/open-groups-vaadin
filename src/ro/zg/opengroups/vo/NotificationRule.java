package ro.zg.opengroups.vo;

import ro.zg.util.data.GenericNameValueContext;

public class NotificationRule {
    private long actionTypeId;
    private int depth;
    private long notificationModeId;
    private boolean enabled;
    
    public NotificationRule() {
	super();
    }
    
    
    public NotificationRule(GenericNameValueContext data){
	actionTypeId = (Long)data.getValue("action_type_id");
	depth = Integer.parseInt(data.getValue("depth").toString());
	notificationModeId = (Long)data.getValue("notification_mode_id");
	enabled = ("y".equals(data.getValue("enabled")))?true:false;
    }
    
    
    /**
     * @return the actionTypeId
     */
    public long getActionTypeId() {
        return actionTypeId;
    }
    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }
    /**
     * @return the notificationModeId
     */
    public long getNotificationModeId() {
        return notificationModeId;
    }
    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    /**
     * @param actionTypeId the actionTypeId to set
     */
    public void setActionTypeId(long actionTypeId) {
        this.actionTypeId = actionTypeId;
    }
    /**
     * @param depth the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }
    /**
     * @param notificationModeId the notificationModeId to set
     */
    public void setNotificationModeId(long notificationModeId) {
        this.notificationModeId = notificationModeId;
    }
    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
