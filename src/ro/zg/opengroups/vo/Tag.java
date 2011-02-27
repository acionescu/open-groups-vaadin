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
import java.util.ArrayList;
import java.util.List;

import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

public class Tag implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 5475917180480292515L;
    private long id;
    private String tagName;

    public Tag() {

    }

    public Tag(GenericNameValueContext dataMap) {
	this.id = (Long) dataMap.getValue("id");
	this.tagName = (String) dataMap.getValue("tag");
    }

    public static List<Tag> getTagsList(GenericNameValueList list) {
	List<Tag> tags = new ArrayList<Tag>();
	if (list != null) {
	    for (int i = 0; i < list.size(); i++) {
		tags.add(new Tag((GenericNameValueContext) list.getValueForIndex(i)));
	    }
	}
	return tags;
    }

    /**
     * @return the id
     */
    public long getId() {
	return id;
    }

    /**
     * @return the tagName
     */
    public String getTagName() {
	return tagName;
    }

}
