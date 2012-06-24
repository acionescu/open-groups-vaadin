package ro.zg.netcell.vaadin.action.application;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;

public class UserNotificationRulesHandler extends OpenGroupsActionHandler{

    /**
     * 
     */
    private static final long serialVersionUID = -8522695335499010027L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	ComponentContainer container =actionContext.getTargetContainer();
	container.removeAllComponents();
	container.addComponent(new Button("Țeapă!"));
    }

}
