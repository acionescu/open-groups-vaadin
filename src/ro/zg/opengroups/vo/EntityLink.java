package ro.zg.opengroups.vo;

public class EntityLink {
    private long linkId;
    private long entityId;
    private long parentId;
    private String parentTitle;
    
    public EntityLink(long linkId, long entityId, long parentId, String parentTitle) {
	this.linkId=linkId;
	this.entityId=entityId;
	this.parentId=parentId;
	this.parentTitle=parentTitle;
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
    
    
}
