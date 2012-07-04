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
    
    public NotificationRule(long actionTypeId, int depth,
	    long notificationModeId, boolean enabled) {
	super();
	this.actionTypeId = actionTypeId;
	this.depth = depth;
	this.notificationModeId = notificationModeId;
	this.enabled = enabled;
    }

    public GenericNameValueContext getRawData(){
	GenericNameValueContext context = new GenericNameValueContext();
	context.put("action_type_id", actionTypeId);
	context.put("depth",depth);
	context.put("notification_mode_id",notificationModeId);
	context.put("enabled",(enabled)?"y":"n");
	
	return context;
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


    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (actionTypeId ^ (actionTypeId >>> 32));
	result = prime * result + depth;
	result = prime * result + (enabled ? 1231 : 1237);
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
	NotificationRule other = (NotificationRule) obj;
	if (actionTypeId != other.actionTypeId)
	    return false;
	if (depth != other.depth)
	    return false;
	if (enabled != other.enabled)
	    return false;
	if (notificationModeId != other.notificationModeId)
	    return false;
	return true;
    }
    
    
}
