package ro.zg.opengroups.constants;

import java.util.HashMap;
import java.util.Map;

public abstract class Defaults {
	public static final Map<String,String> DEFAULT_ACTION_FOR_ENTITY;
	
	static{
		DEFAULT_ACTION_FOR_ENTITY = new HashMap<String, String>();
		DEFAULT_ACTION_FOR_ENTITY.put(EntityType.COMMENT, "/LIST/entity.list.newest");
	}
	
	public static String getDefaultActionForEntityType(String entityType){
		return DEFAULT_ACTION_FOR_ENTITY.get(entityType);
	}
	
}
