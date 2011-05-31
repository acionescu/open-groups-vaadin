package ro.zg.netcell.vaadin.action.application;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.gui.components.CausalHierarchyContainer;

public class ShowCausalHierarchyHandler extends OpenGroupsActionHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 7786208334443260694L;
    
    @Override
    public void handle(ActionContext actionContext) throws Exception {
	CausalHierarchyContainer hierarchyContainer = actionContext.getWindow().getHierarchyContainer();
	/* set max depth */
	long maxDepth = getModel().getHierarchyMaxDepth(actionContext.getApp().getRootEntity().getId());
	hierarchyContainer.setMaxDepth(maxDepth);
    }
    
}
