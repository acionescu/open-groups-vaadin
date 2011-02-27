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
import java.util.ResourceBundle;

import ro.zg.open_groups.resources.OpenGroupsResources;

public class VoteType {
    public static String YES="yes";
    public static String NO="no";
    
    private static String[] values = new String[]{YES,NO};
    private static Map<String,String> dbValues = new HashMap<String, String>();
    static {
	dbValues.put(YES,"y");
	dbValues.put(NO,"n");
    }
    
    private String dbValue;
    private String caption;
    
    private VoteType(String caption, String dbValue) {
	this.dbValue = dbValue;
	this.caption = caption;
    }
    
    public String getDbValue() {
	return dbValue;
    }
    
    public static List<VoteType> valuesList(String entityType){
	List<VoteType> list = new ArrayList<VoteType>();
	for(String voteType : values) {
	    VoteType vt = new VoteType(getCaptionForEntityType(entityType, voteType), dbValues.get(voteType));
	    list.add(vt);
	}
	
	return list;
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
    
    
    public String toString() {
	return caption;
    }
    
}
