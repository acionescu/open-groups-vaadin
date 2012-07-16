/*******************************************************************************
 * Copyright 2012 AdrianIonescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.opengroups.vo;

import java.util.Collection;
import java.util.TreeSet;

public class MultitypeNotificationRule {
    private MultitypeNotificationRuleId commonFields;

    private Collection<NotificationMode> availableNotificationModes;
    private Collection<ActionType> availableActionTypes;
    private Collection<DepthValue> availableDepths;
    private NotificationRulesList rulesList;

    public MultitypeNotificationRule(MultitypeNotificationRuleId commonFields,
	    Collection<NotificationMode> availableNotificationModes,
	    Collection<ActionType> availableActionTypes, Collection<DepthValue> availableDepths, NotificationRulesList rulesList) {
	super();
	this.commonFields = commonFields;
	this.availableNotificationModes = availableNotificationModes;
	this.rulesList=rulesList;
	this.availableDepths=availableDepths;
	setAvailableActionTypes(availableActionTypes);
    }
    
    


    public void addSelectedActionType(ActionType at){
	commonFields.getSelectedActionTypes().add(at);
	availableActionTypes.add(at);
    }

    public MultitypeNotificationRuleId getCommonFields() {
        return commonFields;
    }

    public Collection<ActionType> getSelectedActionTypes() {
        return commonFields.getSelectedActionTypes();
    }

    public Collection<NotificationMode> getAvailableNotificationModes() {
        return availableNotificationModes;
    }

    public Collection<ActionType> getAvailableActionTypes() {
        return availableActionTypes;
    }



    /**
     * @param commonFields the commonFields to set
     */
    public void setCommonFields(MultitypeNotificationRuleId commonFields) {
        this.commonFields = commonFields;
    }



    /**
     * @param selectedActionTypes the selectedActionTypes to set
     */
    public void setSelectedActionTypes(Collection<ActionType> selectedActionTypes) {
        commonFields.setSelectedActionTypes(selectedActionTypes);
    }



    /**
     * @param availableNotificationModes the availableNotificationModes to set
     */
    public void setAvailableNotificationModes(Collection<NotificationMode> availableNotificationModes) {
        this.availableNotificationModes = availableNotificationModes;
    }



    /**
     * @param availableActionTypes the availableActionTypes to set
     */
    public void setAvailableActionTypes(Collection<ActionType> availableActionTypes) {
        this.availableActionTypes = new TreeSet<ActionType>(availableActionTypes);
        
        addSelectedActionsToAvailableActions();
    }

    private void addSelectedActionsToAvailableActions() {
	Collection<ActionType> selectedActionTypes = commonFields.getSelectedActionTypes();
	if(selectedActionTypes != null) {
	    availableActionTypes.addAll(selectedActionTypes);
	}
    }

    /**
     * @return the rulesList
     */
    public NotificationRulesList getRulesList() {
        return rulesList;
    }



    /**
     * @param rulesList the rulesList to set
     */
    public void setRulesList(NotificationRulesList rulesList) {
        this.rulesList = rulesList;
    }




    /**
     * @return the availableDepths
     */
    public Collection<DepthValue> getAvailableDepths() {
        return availableDepths;
    }

    /**
     * @param availableDepths the availableDepths to set
     */
    public void setAvailableDepths(Collection<DepthValue> availableDepths) {
        this.availableDepths = availableDepths;
    }

    public void delete(){
	rulesList.deleteRule(this);
    }
}
