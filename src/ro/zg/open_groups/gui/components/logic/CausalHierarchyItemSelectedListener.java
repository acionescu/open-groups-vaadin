package ro.zg.open_groups.gui.components.logic;

import ro.zg.open_groups.OpenGroupsApplication;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

public class CausalHierarchyItemSelectedListener implements ValueChangeListener {
    /**
     * 
     */
    private static final long serialVersionUID = 5677068202956133395L;
    private OpenGroupsApplication app;

    public CausalHierarchyItemSelectedListener(OpenGroupsApplication app) {
	this.app = app;
    }

    @Override
    public void valueChange(ValueChangeEvent event) {
	Long value = (Long) event.getProperty().getValue();
	/* default to root entity */
	if (value == null) {
//	    value = app.getRootEntity().getId();
	    return;
	}
	/* open entity if not already selected */
	if (app.getActiveEntity().getId() != value) {
	    app.openEntityById(value, null, 1);
	}

    }

}
