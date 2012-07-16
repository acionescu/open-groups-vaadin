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
	cell.setMargin(false,false,false,true);
	cell.addComponent(header);
	container.addComponent(cell);
	
	HorizontalLayout cl = (HorizontalLayout)container;
	cl.setComponentAlignment(cell, Alignment.MIDDLE_LEFT);
	cl.setExpandRatio(cell, 1.3f);
	return cell;
    }

}
