package ro.zg.netcell.vaadin.action.application;

import java.util.HashMap;
import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.views.UserNotificationRulesView;
import ro.zg.opengroups.vo.NotificationRulesList;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;

public class UserNotificationRulesHandler extends OpenGroupsActionHandler{

    /**
     * 
     */
    private static final long serialVersionUID = -8522695335499010027L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	
	refreshNotificationsView(actionContext);
    }

    private void refreshNotificationsView(ActionContext actionContext) throws Exception{
	NotificationRulesList rulesList = getNotificationRulesList(actionContext);
	
	ComponentContainer container =actionContext.getTargetContainer();
	container.removeAllComponents();
	
	UserNotificationRulesView view = new UserNotificationRulesView();
	rulesList.addView(view);
	view.update(rulesList);
	
	container.addComponent(view.getContainer());
    }
    
    private NotificationRulesList getNotificationRulesList(ActionContext actionContext) throws Exception{
	OpenGroupsApplication app =actionContext.getApp();
	Map<String,Object> params = new HashMap<String, Object>();
	params.put("userId", app.getCurrentUserId());
	params.put("entityId", actionContext.getEntity().getId());
	
	CommandResponse response = executeAction(actionContext, params);
	return getModel().buildBeanFromResponse(response, NotificationRulesList.class);
    }
    
}
