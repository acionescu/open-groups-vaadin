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
    private static final Map<Integer, DepthValue> depthValues;
    
    static {
	depthValues = new LinkedHashMap<Integer, DepthValue>();
	depthValues.put(0, new DepthValue(0, OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_DEPTH_PREFIX+0)));
	depthValues.put(3, new DepthValue(3, OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_DEPTH_PREFIX+3)));
	depthValues.put(5, new DepthValue(5, OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_DEPTH_PREFIX+5)));
	depthValues.put(10, new DepthValue(10, OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_DEPTH_PREFIX+10)));
	depthValues.put(Integer.MAX_VALUE, new DepthValue(Integer.MAX_VALUE, OpenGroupsResources.getMessage(MessagesConstants.NOTIFICATION_DEPTH_PREFIX+Integer.MAX_VALUE)));
	
    }
    
    private List<NotificationRule> rulesList;
    private Map<Long, NotificationMode> notificationModes;
    private Map<Long, ActionType> actionTypes;
    /* keeps the action types for which there is no rule defined yet */
    private Map<Long, ActionType> availableActionTypes;
    private NotificationMode defaultNotificationMode;

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
	    if(defaultNotificationMode == null) {
		defaultNotificationMode=mode;
	    }
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
	MultitypeNotificationRuleId rid = new MultitypeNotificationRuleId(depthValues.get(rule.getDepth()), notificationModes.get(rule.getNotificationModeId()), rule.isEnabled(),null);
	MultitypeNotificationRule mRule = getMultitypeRuleById(rid);
	mRule.addSelectedActionType(actionTypes.get(rule.getActionTypeId()));
    }
    
    private MultitypeNotificationRule getMultitypeRuleById(MultitypeNotificationRuleId rid){
	MultitypeNotificationRule mRule = multitypeRules.get(rid);
	if(mRule == null){
	    mRule = new MultitypeNotificationRule(rid,notificationModes.values(), new TreeSet<ActionType>(availableActionTypes.values()),depthValues.values(), this);
	    multitypeRules.put(rid, mRule);
	}
	return mRule;
    }
    
    public void createNewRule() {
	MultitypeNotificationRuleId rid = new MultitypeNotificationRuleId(depthValues.get(0), defaultNotificationMode, true,new ArrayList<ActionType>(availableActionTypes.values()));
	MultitypeNotificationRule mRule = getMultitypeRuleById(rid);
	mRule.setAvailableActionTypes(availableActionTypes.values());
	
	availableActionTypes.clear();
	updateMultitypeRules();
	
	dispatchUpdate(this);
    }
    
    public void onRuleUpdated() {
	updateAvailableActions();
	updateMultitypeRules();
	
	dispatchUpdate(this);
    }
    
    private void updateAvailableActions() {
	availableActionTypes = new HashMap<Long, ActionType>(actionTypes);
	for(MultitypeNotificationRule mRule : multitypeRules.values()) {
	    for(ActionType at : mRule.getSelectedActionTypes()) {
		availableActionTypes.remove(at.getActionTypeId());
	    }
	}
    }
    
    private void updateMultitypeRules() {
	for(MultitypeNotificationRule mRule : multitypeRules.values()) {
	    mRule.setAvailableActionTypes(availableActionTypes.values());
	}
    }
    

    public Map<MultitypeNotificationRuleId, MultitypeNotificationRule> getMultitypeRules() {
        return multitypeRules;
    }

    public Map<Long, ActionType> getAvailableActionTypes() {
        return availableActionTypes;
    }
    
    
}
