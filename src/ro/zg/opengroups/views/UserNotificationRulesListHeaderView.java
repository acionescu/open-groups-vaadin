package ro.zg.opengroups.views;

import ro.zg.opengroups.vo.NotificationRulesList;
import ro.zg.presentation.utils.ListColumn;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class UserNotificationRulesListHeaderView extends OpenGroupsBaseView<NotificationRulesList>{
    
    public UserNotificationRulesListHeaderView(){
	container = new HorizontalLayout();
	container.setWidth("100%");
	container.setHeight("30px");
    }

    @Override
    public void update(NotificationRulesList updateData) {
	
	container.removeAllComponents();
	container.addStyleName(updateData.getHeaderContainerStyle());
	
	CssLayout lastCell=null;
	for(ListColumn lc : updateData.getColumns()){
	    lastCell = addHeader(lc, updateData);
	}
	
	HorizontalLayout cl = (HorizontalLayout)container;
	cl.setExpandRatio(lastCell, 1.9f);
    }
    
    private CssLayout  addHeader(ListColumn lc, NotificationRulesList updateData){
	Label header = new Label(lc.getDescription());
	header.setSizeUndefined();
	CssLayout cell = new CssLayout();
	cell.addStyleName(updateData.getHeaderCellStyle());
	cell.addComponent(header);
	container.addComponent(cell);
	
	HorizontalLayout cl = (HorizontalLayout)container;
	cl.setComponentAlignment(cell, Alignment.MIDDLE_LEFT);
	cl.setExpandRatio(cell, 1.3f);
	return cell;
    }

}
