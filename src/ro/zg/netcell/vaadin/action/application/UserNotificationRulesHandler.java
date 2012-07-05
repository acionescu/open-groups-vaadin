package ro.zg.netcell.vaadin.action.application;

import java.util.HashMap;
import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.constants.OpenGroupsExceptions;
import ro.zg.opengroups.views.UserNotificationRulesView;
import ro.zg.opengroups.vo.NotificationRulesList;
import ro.zg.presentation.utils.UserEvent;
import ro.zg.presentation.utils.UserEventHandler;

import com.vaadin.ui.ComponentContainer;

public class UserNotificationRulesHandler extends OpenGroupsActionHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -8522695335499010027L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {

	refreshNotificationsView(actionContext);
    }

    private void refreshNotificationsView(final ActionContext actionContext)
	    throws Exception {
	final NotificationRulesList rulesList = getNotificationRulesList(actionContext);
	final OpenGroupsApplication app = actionContext.getApp();
	ComponentContainer container = actionContext.getTargetContainer();
	container.removeAllComponents();

	UserEventHandler<UserEvent> eventHandler = new UserEventHandler<UserEvent>() {

	    @Override
	    public void handleEvent(UserEvent event) {
		boolean saved = getModel().saveUserNotificationRules(
			app.getCurrentUserId(),
			actionContext.getEntity().getId(), rulesList);
		if (saved) {
		    rulesList.markAsSaved();
		}
		else{
		    app.pushError(OpenGroupsExceptions.getSystemError());
		}
	    }
	};

	UserNotificationRulesView view = getViewsManager().createView(
		UserNotificationRulesView.class, eventHandler);
	rulesList.addView(view);
	view.update(rulesList);
	container.addComponent(view.getContainer());
    }

    private NotificationRulesList getNotificationRulesList(
	    ActionContext actionContext) throws Exception {
	OpenGroupsApplication app = actionContext.getApp();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("userId", app.getCurrentUserId());
	params.put("entityId", actionContext.getEntity().getId());

	CommandResponse response = executeAction(actionContext, params);
	return getModel().buildBeanFromResponse(response,
		NotificationRulesList.class);
    }

}
