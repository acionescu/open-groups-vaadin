package ro.zg.opengroups.views;

import ro.zg.opengroups.vo.ActionType;
import ro.zg.opengroups.vo.MultitypeNotificationRule;
import ro.zg.opengroups.vo.NotificationMode;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

public class UserNotificationRuleView extends OpenGroupsBaseView<MultitypeNotificationRule>{

    /**
     * 
     */
    private static final long serialVersionUID = -7542521734104929869L;
    
    
    public void update(MultitypeNotificationRule rule){
	
	container.removeAllComponents();
	
	displayActionTypesCombo(rule);
	displayDepthField(rule);
	displayNotificationModesCombo(rule);
	displayEnabledCheckBox(rule);
    }
    
    private void displayActionTypesCombo(MultitypeNotificationRule rule){
	
//	final ComboBox select = new ComboBox();
	OptionGroup select = new OptionGroup();
	select.setImmediate(true);
	select.setMultiSelect(true);
	select.setWidth("110px");
	select.addStyleName("middle-left right-margin-10");
	
	for(ActionType at : rule.getAvailableActionTypes()){
	    select.addItem(at);
	}
	
	select.setValue(rule.getSelectedActionTypes());
    }
    
    private void displayDepthField(MultitypeNotificationRule rule){
	
	TextField field = new TextField();
	field.setWidth("50px");
	field.addStyleName("middle-left right-margin-10");
	
	field.setValue(rule.getCommonFields().getDepth());
    }

    private void displayNotificationModesCombo(MultitypeNotificationRule rule){
	final ComboBox select = new ComboBox();
	select.setImmediate(true);
	select.setMultiSelect(true);
	select.setWidth("110px");
	select.addStyleName("middle-left right-margin-10");
	
	for(NotificationMode nm : rule.getAvailableNotificationModes()){
	    select.addItem(nm);
	}
	
	select.setValue(rule.getCommonFields().getNotificationMode());
    }
    
    private void displayEnabledCheckBox(MultitypeNotificationRule rule){
	CheckBox checkBox = new CheckBox();
	checkBox.addStyleName("middle-left right-margin-10");
	checkBox.setValue(rule.getCommonFields().isEnabled());
    }
}
