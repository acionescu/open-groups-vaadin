package ro.zg.opengroups.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.action.constants.MessagesConstants;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.util.NetcellBean;
import ro.zg.presentation.utils.AbstractList;
import ro.zg.presentation.utils.ListColumn;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

public class NotificationRulesList extends AbstractList implements NetcellBean {
    private List<NotificationRule> rulesList;
    private Map<Long, NotificationMode> notificationModes;
    private Map<Long, ActionType> actionTypes;
    /* keeps the action types for which there is no rule defined yet */
    private Map<Long, ActionType> availableActionTypes;

    /*
     * Groups the rules by the fields depth, notificationMode and enabled <br/>
     * This exists to allow the user to define more rules with a minimum amount
     * of input
     */
    private Map<MultitypeNotificationRuleId, MultitypeNotificationRule> multitypeRules;
    
    
    public NotificationRulesList(){
	initColumns();
    }
    
    private void initColumns(){
	setName("notification-rules-list");
	
	addColumn(new ListColumn("25%", OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_RULES_LIST_PREFIX+"action.type")));
	addColumn(new ListColumn("25%", OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_RULES_LIST_PREFIX+"depth")));
	addColumn(new ListColumn("25%", OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_RULES_LIST_PREFIX+"mode")));
	addColumn(new ListColumn("25%", OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_RULES_LIST_PREFIX+"enabled")));
    }

    @Override
    public void update(CommandResponse response) {
	System.out
		.println("updating notificatoin rules list from: " + response);

	/*
	 * this order is important, because the actionTypes need to be defined
	 * before the notification rules
	 */
	updateActionTypes((GenericNameValueList) response
		.getValue("actionTypes"));
	updateNotificationModes((GenericNameValueList) response
		.getValue("notificationModes"));
	updateRulesList((GenericNameValueList) response.getValue("userRules"));
	
	buildMultitypeRules();

    }

    private void updateRulesList(GenericNameValueList list) {
	rulesList = new ArrayList<NotificationRule>();

	for (int i = 0; i < list.size(); i++) {
	    NotificationRule rule = new NotificationRule(
		    (GenericNameValueContext) list.getValueForIndex(i));
	    rulesList.add(rule);
	    /*
	     * remove the action type of the defined rules from the available
	     * action types
	     */
	    availableActionTypes.remove(rule.getActionTypeId());
	}
    }

    private void updateNotificationModes(GenericNameValueList list) {
	notificationModes = new HashMap<Long, NotificationMode>();

	for (int i = 0; i < list.size(); i++) {
	    NotificationMode mode = new NotificationMode(
		    (GenericNameValueContext) list.getValueForIndex(i));
	    notificationModes.put(mode.getNotificationModeId(), mode);
	}
    }

    private void updateActionTypes(GenericNameValueList list) {
	actionTypes = new HashMap<Long, ActionType>();
	availableActionTypes = new HashMap<Long, ActionType>();

	for (int i = 0; i < list.size(); i++) {
	    ActionType actionType = new ActionType(
		    (GenericNameValueContext) list.getValueForIndex(i));
	    actionTypes.put(actionType.getActionTypeId(),actionType);
	    availableActionTypes.put(actionType.getActionTypeId(), actionType);
	}
    }

    private void buildMultitypeRules(){
	multitypeRules = new LinkedHashMap<MultitypeNotificationRuleId, MultitypeNotificationRule>();
	
	for(NotificationRule rule : rulesList ){
	    addMultitypeRule(rule);
	}
    }
    
    private void addMultitypeRule(NotificationRule rule){
	MultitypeNotificationRuleId rid = new MultitypeNotificationRuleId(rule.getDepth(), notificationModes.get(rule.getNotificationModeId()), rule.isEnabled());
	MultitypeNotificationRule mRule = getMultitypeRuleById(rid);
	mRule.addSelectedActionType(actionTypes.get(rule.getActionTypeId()));
    }
    
    private MultitypeNotificationRule getMultitypeRuleById(MultitypeNotificationRuleId rid){
	MultitypeNotificationRule mRule = multitypeRules.get(rid);
	if(mRule == null){
	    mRule = new MultitypeNotificationRule(rid,notificationModes.values(),new TreeSet<ActionType>(availableActionTypes.values()));
	    multitypeRules.put(rid, mRule);
	}
	return mRule;
    }

    public Map<MultitypeNotificationRuleId, MultitypeNotificationRule> getMultitypeRules() {
        return multitypeRules;
    }

    public Map<Long, ActionType> getAvailableActionTypes() {
        return availableActionTypes;
    }
    
    
}
