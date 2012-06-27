package ro.zg.opengroups.vo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MultitypeNotificationRule {
    private MultitypeNotificationRuleId commonFields;

    private Collection<ActionType> selectedActionTypes;
    private Collection<NotificationMode> availableNotificationModes;
    private Collection<ActionType> availableActionTypes;

    public MultitypeNotificationRule(MultitypeNotificationRuleId commonFields,
	    Collection<NotificationMode> availableNotificationModes,
	    Collection<ActionType> availableActionTypes) {
	super();
	this.commonFields = commonFields;
	this.availableNotificationModes = availableNotificationModes;
	this.availableActionTypes = availableActionTypes;
    }
    
    public void addSelectedActionType(ActionType at){
	selectedActionTypes.add(at);
	availableActionTypes.add(at);
    }

    public MultitypeNotificationRuleId getCommonFields() {
        return commonFields;
    }

    public Collection<ActionType> getSelectedActionTypes() {
        return selectedActionTypes;
    }

    public Collection<NotificationMode> getAvailableNotificationModes() {
        return availableNotificationModes;
    }

    public Collection<ActionType> getAvailableActionTypes() {
        return availableActionTypes;
    }

    
}
