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
package ro.zg.opengroups.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.resources.OpenGroupsResources;

import com.vaadin.terminal.Resource;

public class VoteType {
    public static String YES="y";
    public static String NO="n";
    
    private static String[] values = new String[]{YES,NO};
    private static Map<String,Resource> valuesIcons=new HashMap<String, Resource>();
    static {
	valuesIcons.put(YES, OpenGroupsResources.getIcon(OpenGroupsIconsSet.VOTE_UP, OpenGroupsIconsSet.SMALL));
	valuesIcons.put(NO, OpenGroupsResources.getIcon(OpenGroupsIconsSet.VOTE_DOWN, OpenGroupsIconsSet.SMALL));
    }
    
    private String caption;
    private Resource icon;
    private String value;
    
    private VoteType(String value,String caption, Resource icon) {
	this.caption = caption;
	this.icon = icon;
	this.value=value;
    }
    
    public static List<VoteType> valuesList(String entityType){
	List<VoteType> list = new ArrayList<VoteType>();
	for(String voteType : values) {
	    VoteType vt = new VoteType(voteType,getCaptionForEntityType(entityType, voteType), valuesIcons.get(voteType));
	    list.add(vt);
	}
	
	return list;
    }
    
    public static VoteType opposteVoteForValue(String value,String entityType) {
	String otherVal = opposedValue(value);
	return new VoteType(otherVal,getCaptionForEntityType(entityType, otherVal), valuesIcons.get(otherVal));
    }
    
    private static String opposedValue(String value) {
	if(NO.equals(value)) {
	    return YES; 
	}
	return NO;
    }
    
    private static String getCaptionForEntityType(String entityType, String voteType) {
	String key = entityType+".vote."+voteType;
	return OpenGroupsResources.getMessage(key);
    }

    /**
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }
    
    /**
     * @return the icon
     */
    public Resource getIcon() {
        return icon;
    }

    public String toString() {
	return caption;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
}
