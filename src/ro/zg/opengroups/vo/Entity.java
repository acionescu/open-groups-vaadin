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

import com.vaadin.ui.ComponentContainer;

import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;

public class Entity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6835136872340648047L;

    private long id;
    private String complexType;
    private String simpleType;
    private String title;
    private String content;
    private String contentPreview;
    private Timestamp insertDate;
    private int subtypesCount;
    private Map<String, Long> subtypeEntitiesCount = new LinkedHashMap<String, Long>();
    private Map<String, Long> recursiveSubtypeEntitiesCount = new LinkedHashMap<String, Long>();

    private long proVotes;
    private long opposedVotes;
    private long totalVotes;
    private long creatorId;
    private Long generalPriority;
    private String generalStatus;
    private List<Tag> tags = new ArrayList<Tag>();
    /* these are populated when the entity is displayed in the recent activity list */
    private Long parentEntityId = -1L;
    private String parentEntityTitle;
    private long lastActionId = -1;
    private String lastActionType;

    private ComponentContainer entityContainer;
    private Map<String, Object> filterValues = new HashMap<String, Object>();
    private Map<String, FilterOption> filters = new HashMap<String, FilterOption>();
    private Map<String, ComponentContainer> headerActionsContainers = new HashMap<String, ComponentContainer>();
    private ComponentContainer headerActionLinksContainer;

    private EntityUserData userData;
    private EntityState state = new EntityState();

    public Entity() {

    }

    public Entity(long id) {
	this.id = id;
    }

    public Entity(long id, String complexType) {
	this.id = id;
	setComplexType(complexType);
    }

    public Entity(GenericNameValueContext dataMap) {
	update(dataMap);
    }

    public void update(GenericNameValueContext dataMap) {
	// System.out.println(dataMap);
	this.id = (Long) dataMap.getValue("id");
	setComplexType((String) dataMap.getValue("complex_type"));
	this.title = (String) dataMap.getValue("title");
	this.content = (String) dataMap.getValue("content");
	this.contentPreview = (String)dataMap.getValue("content_preview");
	this.insertDate = (Timestamp) dataMap.getValue("insert_date");
	this.subtypesCount = (Integer) dataMap.getValue("subtypes_count");
	this.proVotes = (Long) dataMap.getValue("pro_votes");
	this.opposedVotes = (Long) dataMap.getValue("opposed_votes");
	this.totalVotes = (Long) dataMap.getValue("total_votes");
	this.creatorId = (Long) dataMap.getValue("creator_id");
	if (dataMap.getValue("general_priority") != null) {
	    this.generalPriority = (Long) dataMap.getValue("general_priority");
	}
	else {
	    this.generalPriority=null;
	}
	if (dataMap.getValue("general_status") != null) {
	    this.generalStatus = (String) dataMap.getValue("general_status");
	}
	else {
	    this.generalStatus=null;
	}

	this.userData = new EntityUserData(dataMap);
	initSubtypeEntitiesInfo(dataMap);
	/* if displayed in the recent activity list */
	this.parentEntityTitle = (String) dataMap.getValue("parent_title");
	if (this.parentEntityTitle != null) {
	    this.parentEntityId = (Long) dataMap.getValue("parent_entity_id");
	    this.lastActionId = (Long) dataMap.getValue("action_type_id");
	    this.lastActionType = (String) dataMap.getValue("action_type");
	} else {
	    this.parentEntityId = -1L;
	    this.lastActionId = -1;
	    this.lastActionType = null;
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
     * @return the parentEntityId
     */
    public Long getParentEntityId() {
	return parentEntityId;
    }

    /**
     * @return the parentEntityTitle
     */
    public String getParentEntityTitle() {
	return parentEntityTitle;
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
     * @param parentEntityId
     *            the parentEntityId to set
     */
    public void setParentEntityId(long parentEntityId) {
	this.parentEntityId = parentEntityId;
    }

    /**
     * @param parentEntityTitle
     *            the parentEntityTitle to set
     */
    public void setParentEntityTitle(String parentEntityTitle) {
	this.parentEntityTitle = parentEntityTitle;
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
     * @param contentPreview the contentPreview to set
     */
    public void setContentPreview(String contentPreview) {
        this.contentPreview = contentPreview;
    }

}
