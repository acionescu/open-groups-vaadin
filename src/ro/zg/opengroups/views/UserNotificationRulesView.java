package ro.zg.opengroups.views;

import ro.zg.opengroups.vo.NotificationRulesList;

public class UserNotificationRulesView extends OpenGroupsBaseView<NotificationRulesList>{
    
    private UserNotificationRulesListView listView;
    private UserNotificationRulesListControls controlsView;
    
    

    @Override
    protected void init() {
	super.init();
	
	listView = createView(UserNotificationRulesListView.class);
	controlsView = createView(UserNotificationRulesListControls.class);
    }

    public void update(NotificationRulesList nrl){
	container.removeAllComponents();
	container.addStyleName("notification-rules-container");
	updateList(nrl);
	updateControls(nrl);
    }
    
    private void updateList(NotificationRulesList nrl){
	listView.update(nrl);
	container.addComponent(listView.getContainer());
    }
    
    private void updateControls(NotificationRulesList nrl){
	controlsView.update(nrl);
	container.addComponent(controlsView.getContainer());
    }

}
