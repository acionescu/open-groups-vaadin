package ro.zg.opengroups.vo;

import ro.zg.util.data.GenericNameValueContext;

public class TypeRelationConfig extends TypeConfig{
    private long id;
    private long sourceEntityTypeId;
    private long targetEntityTypeId;
    private String sourceComplexType;
    private String targetComplexType;
    
//    Boolean recursiveListAllowed;
    
    
    public TypeRelationConfig() {
	
    }
    
    public TypeRelationConfig(GenericNameValueContext data) {
	sourceEntityTypeId=Long.parseLong(data.removeValue("source_entity_type_id").toString());
	targetEntityTypeId=Long.parseLong(data.removeValue("target_entity_type_id").toString());
	/* emulate an unique id, for now */
	id=Long.parseLong(sourceEntityTypeId+""+targetEntityTypeId);
	sourceComplexType=data.removeValue("source_complex_type").toString();
	targetComplexType=data.removeValue("complex_subtype").toString();
	
//	Object recursiveListFlag=data.getValue(ComplexEntityParam.ALLOW_RECURSIVE_LIST);
//	if(recursiveListFlag != null) {
//	    recursiveListAllowed=(recursiveListFlag.toString().equals("y"));
//	}
	setConfigContext(data);
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the sourceEntityTypeId
     */
    public long getSourceEntityTypeId() {
        return sourceEntityTypeId;
    }

    /**
     * @return the targetEntityTypeId
     */
    public long getTargetEntityTypeId() {
        return targetEntityTypeId;
    }

    /**
     * @return the sourceComplexType
     */
    public String getSourceComplexType() {
        return sourceComplexType;
    }

    /**
     * @return the targetComplexType
     */
    public String getTargetComplexType() {
        return targetComplexType;
    }


    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param sourceEntityTypeId the sourceEntityTypeId to set
     */
    public void setSourceEntityTypeId(long sourceEntityTypeId) {
        this.sourceEntityTypeId = sourceEntityTypeId;
    }

    /**
     * @param targetEntityTypeId the targetEntityTypeId to set
     */
    public void setTargetEntityTypeId(long targetEntityTypeId) {
        this.targetEntityTypeId = targetEntityTypeId;
    }

    /**
     * @param sourceComplexType the sourceComplexType to set
     */
    public void setSourceComplexType(String sourceComplexType) {
        this.sourceComplexType = sourceComplexType;
    }

    /**
     * @param targetComplexType the targetComplexType to set
     */
    public void setTargetComplexType(String targetComplexType) {
        this.targetComplexType = targetComplexType;
    }
    
}
