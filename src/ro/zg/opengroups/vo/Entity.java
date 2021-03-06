/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;

import com.vaadin.ui.ComponentContainer;

public class Entity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6835136872340648047L;

    private long id;
    private long complexTypeId;
    private String complexType;
    private String simpleType;
    private String title;
    private String content;
    private String contentPreview;
    private Timestamp insertDate;
    private int subtypesCount;
    private Map<String, Long> subtypeEntitiesCount = new LinkedHashMap<String, Long>();
    private Map<String, Long> recursiveSubtypeEntitiesCount = new LinkedHashMap<String, Long>();
    private Long accessRuleId;
    private Long groupId;
    private long proVotes;
    private long opposedVotes;
    private long totalVotes;
    private long creatorId;
    private Long generalPriority;
    private String generalStatus;
    private List<Tag> tags = new ArrayList<Tag>();
    /*
     * these are populated when the entity is displayed in the recent activity list
     */
    // private Long selectedParentId = -1L;
    // private Long selectedParentLinkId;
    // private String parentEntityTitle;
    private EntityLink selectedCause;
    private long lastActionId = -1;
    private String lastActionType;
    private int depth;
    /* the minimal depth in the entities_links graph */
    private int absoluteDepth;
    private List<EntityLink> causes = new ArrayList<EntityLink>();

    private ComponentContainer entityContainer;
    private Map<String, Object> filterValues = new HashMap<String, Object>();
    private Map<String, FilterOption> filters = new HashMap<String, FilterOption>();
    private Map<String, ComponentContainer> headerActionsContainers = new HashMap<String, ComponentContainer>();
    private ComponentContainer headerActionLinksContainer;

    private EntityUserData userData;
    private EntityState state; 

    public Entity() {
	state= new EntityState(this);
    }

    public Entity(long id) {
	this();
	this.id = id;
    }

    public Entity(long id, String complexType) {
	this(id);
	setComplexType(complexType);
    }

    public Entity(GenericNameValueContext dataMap) {
	this();
	update(dataMap);
    }


    public void update(GenericNameValueContext dataMap) {
	// System.out.println(dataMap);
	this.id = (Long) dataMap.getValue("id");
	setComplexType((String) dataMap.getValue("complex_type"));
	this.complexTypeId = (Long) dataMap.getValue("complex_type_id");
	this.title = (String) dataMap.getValue("title");
	this.content = (String) dataMap.getValue("content");
	this.contentPreview = (String) dataMap.getValue("content_preview");
	this.insertDate = (Timestamp) dataMap.getValue("insert_date");
	this.subtypesCount = (Integer) dataMap.getValue("subtypes_count");
	this.proVotes = (Long) dataMap.getValue("pro_votes");
	this.opposedVotes = (Long) dataMap.getValue("opposed_votes");
	this.totalVotes = (Long) dataMap.getValue("total_votes");
	this.creatorId = (Long) dataMap.getValue("creator_id");
	this.accessRuleId=(Long)dataMap.getValue("access_rule_id");
	this.groupId=(Long)dataMap.getValue("group_id");
	if (dataMap.getValue("general_priority") != null) {
	    this.generalPriority = (Long) dataMap.getValue("general_priority");
	} else {
	    this.generalPriority = null;
	}
	if (dataMap.getValue("general_status") != null) {
	    this.generalStatus = (String) dataMap.getValue("general_status");
	} else {
	    this.generalStatus = null;
	}

	this.userData = new EntityUserData(dataMap);
	initSubtypeEntitiesInfo(dataMap);

	/* if displayed in a list */

	Long selectedParentLinkId = (Long) dataMap.getValue("selected_parent_link_id");
	if (selectedParentLinkId != null) {
	    String parentEntityTitle = (String) dataMap.getValue("parent_title");
	    this.selectedCause = new EntityLink(selectedParentLinkId, this.id,
		    (Long) dataMap.getValue("selected_parent_id"), parentEntityTitle);

	    if (dataMap.getValue("action_type_id") != null) {
		this.lastActionId = (Long) dataMap.getValue("action_type_id");
		this.lastActionType = (String) dataMap.getValue("action_type");
	    }
	} else {
	    this.selectedCause = null;
	    this.lastActionId = -1;
	    this.lastActionType = null;
	}
	// if (dataMap.getValue("selected_parent_link_id") != null) {
	//
	// }

	if (dataMap.getValue("depth") != null) {
	    depth = (Integer) dataMap.getValue("depth");
	}
	if (dataMap.getValue("absolute_depth") != null) {
	    absoluteDepth = (Integer) dataMap.getValue("absolute_depth");
	}
    }

    public void setFilter(FilterOption fo) {
	filters.put(fo.getParamName(), fo);
	filterValues.put(fo.getParamName(), fo.getValue());
    }

    public FilterOption getFilter(String paramName) {
	return filters.get(paramName);
    }

    public Object getFilterValue(String paramName) {
	return filterValues.get(paramName);
    }

    public void resetFilters() {
	filters.clear();
	filterValues.clear();
    }

    public void addHeaderActionContainer(String actionName, ComponentContainer container) {
	headerActionsContainers.put(actionName, container);
    }

    public ComponentContainer getHeaderActionContainer(String actionName) {
	return headerActionsContainers.get(actionName);
    }

    private void initSubtypeEntitiesInfo(GenericNameValueContext dataMap) {
	String suffix = "_subtype_count";
	String recursiveSuffinx = "_recursive" + suffix;
	for (Object o : dataMap.getParametersAsList()) {
	    GenericNameValue p = (GenericNameValue) o;
	    String name = p.getName();
	    /* get first level subtypes count */
	    int index = name.indexOf(suffix);
	    if (index > 0) {
		String subtype = name.substring(0, index);
		subtypeEntitiesCount.put(subtype, (Long) dataMap.getValue(name));
	    }
	    /* get all subtypes count */
	    index = name.indexOf(recursiveSuffinx);
	    if (index > 0) {
		String subtype = name.substring(0, index);
		recursiveSubtypeEntitiesCount.put(subtype, (Long) dataMap.getValue(name));
	    }
	}
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return the id
     */
    public long getId() {
	return id;
    }

    /**
     * @return the complexType
     */
    public String getComplexType() {
	return complexType;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
	this.id = id;
    }

    /**
     * @param complexType
     *            the complexType to set
     */
    public void setComplexType(String complexType) {
	this.complexType = complexType;
	if (complexType == null) {
	    simpleType = null;
	} else {
	    // simpleType=complexType.substring(complexType.lastIndexOf("/")+1);
	    simpleType = complexType;
	}
    }

    /**
     * @return the simpleType
     */
    public String getSimpleType() {
	return simpleType;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @return the content
     */
    public String getContent() {
	return content;
    }

    /**
     * @return the insertDate
     */
    public Timestamp getInsertDate() {
	return insertDate;
    }

    /**
     * @return the subtypesCount
     */
    public int getSubtypesCount() {
	return subtypesCount;
    }

    /**
     * @return the subtypeEntitiesCount
     */
    public Map<String, Long> getSubtypeEntitiesCount() {
	return subtypeEntitiesCount;
    }

    /**
     * @return the recursiveSubtypeEntitiesCount
     */
    public Map<String, Long> getRecursiveSubtypeEntitiesCount() {
	return recursiveSubtypeEntitiesCount;
    }

    /**
     * @return the proVotes
     */
    public long getProVotes() {
	return proVotes;
    }

    /**
     * @return the opposedVotes
     */
    public long getOpposedVotes() {
	return opposedVotes;
    }

    /**
     * @return the totalVotes
     */
    public long getTotalVotes() {
	return totalVotes;
    }

    /**
     * @return the tags
     */
    public List<Tag> getTags() {
	return tags;
    }

    /**
     * @param tags
     *            the tags to set
     */
    public void setTags(List<Tag> tags) {
	this.tags = tags;
    }

    /**
     * @return the entityContainer
     */
    public ComponentContainer getEntityContainer() {
	return entityContainer;
    }

    /**
     * @param entityContainer
     *            the entityContainer to set
     */
    public void setEntityContainer(ComponentContainer entityContainer) {
	this.entityContainer = entityContainer;
    }

    /**
     * @return the creatorId
     */
    public long getCreatorId() {
	return creatorId;
    }

    /**
     * @return the filterValues
     */
    public Map<String, Object> getFilterValues() {
	return filterValues;
    }

    public String toString() {
	return title;
    }

    /**
     * @return the state
     */
    public EntityState getState() {
	return state;
    }

    public String superToString() {
	return super.toString();
    }

    /**
     * @return the headerActionLinksContainer
     */
    public ComponentContainer getHeaderActionLinksContainer() {
	return headerActionLinksContainer;
    }

    /**
     * @param headerActionLinksContainer
     *            the headerActionLinksContainer to set
     */
    public void setHeaderActionLinksContainer(ComponentContainer headerActionLinksContainer) {
	this.headerActionLinksContainer = headerActionLinksContainer;
    }

    /**
     * @return the headerActionsContainers
     */
    public Map<String, ComponentContainer> getHeaderActionsContainers() {
	return headerActionsContainers;
    }

    /**
     * @return the lastActionId
     */
    public long getLastActionId() {
	return lastActionId;
    }

    /**
     * @return the lastActionType
     */
    public String getLastActionType() {
	return lastActionType;
    }

    /**
     * @param lastActionId
     *            the lastActionId to set
     */
    public void setLastActionId(long lastActionId) {
	this.lastActionId = lastActionId;
    }

    /**
     * @param lastActionType
     *            the lastActionType to set
     */
    public void setLastActionType(String lastActionType) {
	this.lastActionType = lastActionType;
    }

    /**
     * @return the userData
     */
    public EntityUserData getUserData() {
	return userData;
    }

    /**
     * @return the generalPriority
     */
    public Long getGeneralPriority() {
	return generalPriority;
    }

    /**
     * @return the generalStatus
     */
    public String getGeneralStatus() {
	return generalStatus;
    }

    /**
     * @param generalPriority
     *            the generalPriority to set
     */
    public void setGeneralPriority(Long generalPriority) {
	this.generalPriority = generalPriority;
    }

    /**
     * @param generalStatus
     *            the generalStatus to set
     */
    public void setGeneralStatus(String generalStatus) {
	this.generalStatus = generalStatus;
    }

    /**
     * @return the contentPreview
     */
    public String getContentPreview() {
	return contentPreview;
    }

    /**
     * @param contentPreview
     *            the contentPreview to set
     */
    public void setContentPreview(String contentPreview) {
	this.contentPreview = contentPreview;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
	return depth;
    }

    /**
     * @return the complexTypeId
     */
    public long getComplexTypeId() {
	return complexTypeId;
    }

    /**
     * @param complexTypeId
     *            the complexTypeId to set
     */
    public void setComplexTypeId(long complexTypeId) {
	this.complexTypeId = complexTypeId;
    }

    /**
     * @return the absoluteDepth
     */
    public int getAbsoluteDepth() {
	return absoluteDepth;
    }

    /**
     * @param absoluteDepth
     *            the absoluteDepth to set
     */
    public void setAbsoluteDepth(int absoluteDepth) {
	this.absoluteDepth = absoluteDepth;
    }

    /**
     * @return the causes
     */
    public List<EntityLink> getCauses() {
	return causes;
    }

    /**
     * @param causes
     *            the causes to set
     */
    public void setCauses(List<EntityLink> causes) {
	this.causes = causes;
    }

    /**
     * @return the selectedCause
     */
    public EntityLink getSelectedCause() {
	return selectedCause;
    }

    /**
     * @param selectedCause
     *            the selectedCause to set
     */
    public void setSelectedCause(EntityLink selectedCause) {
	this.selectedCause = selectedCause;
    }

    public Long getAccessRuleId() {
        return accessRuleId;
    }

    public void setAccessRuleId(Long accessRuleId) {
        this.accessRuleId = accessRuleId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
