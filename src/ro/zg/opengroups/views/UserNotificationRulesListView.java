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
