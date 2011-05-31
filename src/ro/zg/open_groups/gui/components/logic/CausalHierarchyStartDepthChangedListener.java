package ro.zg.open_groups.gui.components.logic;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.components.CausalHierarchyContainer;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class CausalHierarchyStartDepthChangedListener implements ValueChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 5671642483629001741L;
    private static final Logger logger = MasterLogManager.getLogger("CausalHierarchyStartDepthChangedListener");
    
    private OpenGroupsApplication app;
    
    public CausalHierarchyStartDepthChangedListener(OpenGroupsApplication app) {
	this.app = app;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
	Object value = event.getProperty().getValue();
	if(value == null) {
	    logger.error("Null value for startDepth");
	    return;
	}
	int startDepth = (Integer)value;
	CausalHierarchyContainer chc = app.getActiveWindow().getHierarchyContainer();
	chc.setStartDepth(startDepth);
	ActionsManager.getInstance().executeAction(ActionsManager.REFRESH_CAUSAL_HIERARCHY, new ActionContext(app));
    }

   

}
