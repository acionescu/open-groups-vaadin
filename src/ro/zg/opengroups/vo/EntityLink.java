package ro.zg.opengroups.vo;

import ro.zg.util.data.GenericNameValueContext;

public class EntityLink {
    private long linkId=-1;
    private long entityId=-1;
    private long parentId=-1;
    private String parentTitle;
    
    public EntityLink(long linkId, long entityId, long parentId, String parentTitle) {
	this.linkId=linkId;
	this.entityId=entityId;
	this.parentId=parentId;
	this.parentTitle=parentTitle;
    }
    
    public EntityLink(GenericNameValueContext context) {
	Object linkIdObj = context.getValue("entity_link_id") ;
	if(linkIdObj != null) {
	    linkId = Long.parseLong(linkIdObj.toString());
	}
	
	Object parentIdObj = context.getValue("parent_id");
	if(parentIdObj != null) {
	    parentId = Long.parseLong(parentIdObj.toString());
	}
	
	parentTitle = (String)context.getValue("title");
    }
    
    
    /**
     * @return the linkId
     */
    public long getLinkId() {
        return linkId;
    }
    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }
    /**
     * @return the parentId
     */
    public long getParentId() {
        return parentId;
    }
    /**
     * @return the parentTitle
     */
    public String getParentTitle() {
        return parentTitle;
    }
    /**
     * @param linkId the linkId to set
     */
    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }
    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }
    /**
     * @param parentId the parentId to set
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
    /**
     * @param parentTitle the parentTitle to set
     */
    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (entityId ^ (entityId >>> 32));
	result = prime * result + (int) (linkId ^ (linkId >>> 32));
	result = prime * result + (int) (parentId ^ (parentId >>> 32));
	result = prime * result + ((parentTitle == null) ? 0 : parentTitle.hashCode());
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
	EntityLink other = (EntityLink) obj;
	if (entityId != other.entityId)
	    return false;
	if (linkId != other.linkId)
	    return false;
	if (parentId != other.parentId)
	    return false;
	if (parentTitle == null) {
	    if (other.parentTitle != null)
		return false;
	} else if (!parentTitle.equals(other.parentTitle))
	    return false;
	return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "EntityLink [linkId=" + linkId + ", entityId=" + entityId + ", parentId=" + parentId + ", parentTitle="
		+ parentTitle + "]";
    }
    
    
}
