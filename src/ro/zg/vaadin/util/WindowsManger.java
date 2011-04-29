package ro.zg.vaadin.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.util.data.ListMap;


public class WindowsManger {
    /* keeps the last instance id for the window with the specified name */
    private Map<String, Integer> lastIdForName = new HashMap<String, Integer>();
    private ListMap<String, Integer> activeIdsForName = new ListMap<String, Integer>();

    public Integer getNextWindowId(String name) {
	Integer nextId = getNextId(name);
	activeIdsForName.add(name, nextId);
	return nextId;
    }
    
    /**
     * Returns an active window id for that name, or a new id if no active windows exist
     * @param name
     * @return
     */
    public Integer getWindowId(String name) {
	List<Integer> activeIds = activeIdsForName.get(name);
	if (activeIds != null && activeIds.size() > 0) {
	    /* return first id */
	    return activeIds.get(0);
	}
	return getNextWindowId(name);
    }

    public void removeWindowId(String name, Integer id) {
	List<Integer> activeIds = activeIdsForName.get(name);
	if (activeIds != null) {
	    activeIds.remove(id);
	}
    }

    private Integer getNextId(String name) {
	Integer id = lastIdForName.get(name);
	if (id == null) {
	    id = 0;
	} else {
	    id += 1;
	}
	lastIdForName.put(name, id);
	return id;
    }

}
