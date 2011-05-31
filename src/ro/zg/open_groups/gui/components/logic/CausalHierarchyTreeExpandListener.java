package ro.zg.open_groups.gui.components.logic;

import java.util.HashMap;
import java.util.Map;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;

import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;

public class CausalHierarchyTreeExpandListener implements ExpandListener{

    /**
     * 
     */
    private static final long serialVersionUID = -7047467220617199044L;

    @Override
    public void nodeExpand(ExpandEvent event) {
	Object itemId = event.getItemId();
	OpenGroupsApplication app = (OpenGroupsApplication)event.getComponent().getApplication();
	Map<String,Object> params = new HashMap<String, Object>();
	params.put("parentId", itemId);
	params.put("refresh", false);
	params.put("startDepth", 0);
	ActionsManager.getInstance().executeAction(ActionsManager.REFRESH_CAUSAL_HIERARCHY, new ActionContext(app,params));
    }

}
