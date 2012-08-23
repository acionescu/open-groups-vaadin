/*******************************************************************************
 * Copyright 2012 AdrianIonescu
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

import java.util.HashMap;
import java.util.Map;

public abstract class Defaults {
	public static final Map<String,String> DEFAULT_ACTION_FOR_ENTITY;
	
	static{
		DEFAULT_ACTION_FOR_ENTITY = new HashMap<String, String>();
		DEFAULT_ACTION_FOR_ENTITY.put(EntityType.COMMENT, "/LIST/entity.list.newest");
		DEFAULT_ACTION_FOR_ENTITY.put(EntityType.SOLUTION, "/LIST/entity.list.newest");
		DEFAULT_ACTION_FOR_ENTITY.put(EntityType.ISSUE, "/LIST/entity.list.newest");
	}
	
	public static String getDefaultActionForEntityType(String entityType){
		return DEFAULT_ACTION_FOR_ENTITY.get(entityType);
	}
	
}
