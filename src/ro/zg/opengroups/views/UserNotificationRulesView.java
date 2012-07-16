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
