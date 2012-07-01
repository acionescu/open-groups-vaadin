package ro.zg.opengroups.vo;

import ro.zg.netcell.vaadin.action.constants.MessagesConstants;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.util.data.GenericNameValueContext;

public class ActionType implements Comparable{
    private long actionTypeId;
    private String description;
    private String type;
    
    
    public ActionType(GenericNameValueContext data){
	actionTypeId = (Long)data.getValue("id");
	type = (String)data.getValue("type");
	description = OpenGroupsResources.getMessage(MessagesConstants.ACTION_TYPE_PREFIX+type);
    }
    
    
    /**
     * @return the actionTypeId
     */
    public long getActionTypeId() {
        return actionTypeId;
    }
  
    /**
     * @param actionTypeId the actionTypeId to set
     */
    public void setActionTypeId(long actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (actionTypeId ^ (actionTypeId >>> 32));
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
	ActionType other = (ActionType) obj;
	if (actionTypeId != other.actionTypeId)
	    return false;
	return true;
    }


    @Override
    public int compareTo(Object o) {
	ActionType other = (ActionType)o;
	return new Long(actionTypeId).compareTo(new Long(other.getActionTypeId()));
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return description;
    }
    
    
}
