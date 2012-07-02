package ro.zg.opengroups.views;

import ro.zg.opengroups.vo.MultitypeNotificationRule;
import ro.zg.opengroups.vo.NotificationRulesList;

public class UserNotificationRulesListView extends
	OpenGroupsBaseView<NotificationRulesList> {
    
    private UserNotificationRulesListHeaderView headerView;
    
    @Override
    protected void init() {
	super.init();
	
	headerView=createView(UserNotificationRulesListHeaderView.class);

    }

    @Override
    public void update(NotificationRulesList updateData) {
	container.removeAllComponents();
	container.addStyleName(updateData.getListContainerStyle());
	
	displayHeaders(updateData);
	displayRows(updateData);
    }
    
    private void displayHeaders(NotificationRulesList updateData){
	headerView.update(updateData);
	container.addComponent(headerView.getContainer());
    }

    private void displayRows(NotificationRulesList updateData){
	for(MultitypeNotificationRule rule : updateData.getMultitypeRules().values()){
	    UserNotificationRuleView ruleView = createView(UserNotificationRuleView.class);
	    ruleView.update(rule);
	    ruleView.getContainer().addStyleName(updateData.getRowContainerStyle());
	    container.addComponent(ruleView.getContainer());
	}
    }
    
}
