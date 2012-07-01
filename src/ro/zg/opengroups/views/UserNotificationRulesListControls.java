package ro.zg.opengroups.views;

import ro.zg.opengroups.vo.NotificationRulesList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UserNotificationRulesListControls extends OpenGroupsBaseView<NotificationRulesList> {

    @Override
    public void update(NotificationRulesList updateData) {
	container.removeAllComponents();
	container.addStyleName(updateData.getControlsContainerStyle());
	displayAddNewRuleButton(updateData);
    }

    private void displayAddNewRuleButton(final NotificationRulesList updateData) {
	Button button = new Button();
	// TODO: add an icon, don't let this hardcoded text
	button.setCaption("Adauga");
	if (updateData.getAvailableActionTypes().size() == 0) {
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

}
