package ro.zg.opengroups.views;

import ro.zg.opengroups.vo.NotificationRulesList;
import ro.zg.presentation.utils.ListColumn;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class UserNotificationRulesListHeaderView extends OpenGroupsBaseView<NotificationRulesList>{

    @Override
    public void update(NotificationRulesList updateData) {
	
	container.removeAllComponents();
	container.addStyleName(updateData.getHeaderContainerStyle());
	
	for(ListColumn lc : updateData.getColumns()){
	    addHeader(lc, updateData);
	}
	
    }
    
    private void addHeader(ListColumn lc, NotificationRulesList updateData){
	Label header = new Label(lc.getDescription());
	CssLayout cell = new CssLayout();
	cell.addStyleName(updateData.getHeaderCellStyle());
	cell.addComponent(header);
	container.addComponent(cell);
    }

}
