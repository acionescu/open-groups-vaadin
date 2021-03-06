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

import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.vo.NotificationRulesList;
import ro.zg.presentation.utils.UserEvent;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UserNotificationRulesListControls extends OpenGroupsBaseView<NotificationRulesList> {

    @Override
    public void update(NotificationRulesList updateData) {
	container.removeAllComponents();
	container.addStyleName(updateData.getControlsContainerStyle());
	displayAddNewRuleButton(updateData);
	displaySaveRulesButton(updateData);
    }

    private void displayAddNewRuleButton(final NotificationRulesList updateData) {
	Button button = new Button();
	button.addStyleName(updateData.getControlsContainerCellStyle());
	// TODO: add an icon, don't let this hardcoded text
	button.setDescription(OpenGroupsResources.getMessage("notification.rules.list.add.rule"));
	button.setIcon(OpenGroupsResources.getIcon(OpenGroupsIconsSet.ADD, OpenGroupsIconsSet.SMALL));
	if (!updateData.isNewRuleAllowed()) {
	    button.setEnabled(false);
	} else {
	    button.addListener(new ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
		    updateData.createNewRule();
		}
	    });
	}

	container.addComponent(button);
    }
    
    private void displaySaveRulesButton(final NotificationRulesList updateData){
	final Button button = new Button();
	button.addStyleName(updateData.getControlsContainerCellStyle());
	// TODO: add an icon, don't let this hardcoded text
	button.setDescription(OpenGroupsResources.getMessage("notification.rules.list.save"));
	button.setIcon(OpenGroupsResources.getIcon(OpenGroupsIconsSet.ACCEPT, OpenGroupsIconsSet.SMALL));
	if (!updateData.isSaveNeeded()) {
	    button.setEnabled(false);
	} else {
	    button.addListener(new ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
		    handleEvent(new UserEvent(NotificationUserEvents.SAVE_BUTTON_CLICKED, button, updateData));
		}
	    });
	}

	container.addComponent(button);
    }

}
