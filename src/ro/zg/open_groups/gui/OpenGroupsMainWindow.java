/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
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
package ro.zg.open_groups.gui;

import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.components.CausalHierarchyContainer;
import ro.zg.open_groups.gui.constants.OpenGroupsStyles;
import ro.zg.opengroups.util.OpenGroupsUtil;
import ro.zg.opengroups.vo.Entity;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class OpenGroupsMainWindow extends Window {
    /**
     * 
     */
    private static final long serialVersionUID = -8492978667795915161L;
    private static final Logger logger = MasterLogManager.getLogger("WINDOW");
    
    private OpenGroupsApplication app;
    private CssLayout header;
    private CssLayout userActionsContainer;
    private Tab userActionsTab;
    private VerticalLayout mainContent;
    private CssLayout entityContent;
    private UriFragmentUtility uriUtility;
    private CausalHierarchyContainer hierarchyContainer;
    
    public OpenGroupsMainWindow(OpenGroupsApplication app, String name) {
	super.setCaption(name);
	this.app=app;
	
	VerticalLayout winPain = (VerticalLayout)this.getContent();
	winPain.setSizeFull();
	winPain.setMargin(false);
	
	/* add the uri utility */
	uriUtility = new UriFragmentUtility();
	mainContent = new VerticalLayout();
	mainContent.addComponent(uriUtility);
	
	mainContent.setWidth("1100px");
	mainContent.setHeight("100%");
	
	
	winPain.addComponent(mainContent);
	winPain.setComponentAlignment(mainContent, Alignment.TOP_CENTER);
	
	uriUtility.addListener(app.getUriHandler());
	addURIHandler(app.getUriHandler());
    }

    public void createLayout() {
//	mainContent = new CssLayout();
	mainContent.setMargin(false);
//	this.setContent(mainContent);
//	mainContent.setWidth("100%");
//	mainContent.setSizeFull();
//	mainContent.addStyleName(OpenGroupsStyles.MAIN_PANE);
	
	header = new CssLayout();
	header.setWidth("100%");
	mainContent.addComponent(header);
	
	userActionsContainer = new CssLayout();

	entityContent = new CssLayout();
	entityContent.setWidth("100%");
	entityContent.setHeight("100%");
	entityContent.setMargin(true);
	entityContent.addStyleName(OpenGroupsStyles.ENTITY_PANE);
	
	HorizontalLayout frame = new HorizontalLayout();
	frame.addStyleName(OpenGroupsStyles.FRAME_PANE);
//	frame.setSizeFull();
	frame.setWidth("100%");
	frame.setHeight("100%");
	
	hierarchyContainer = new CausalHierarchyContainer();
	hierarchyContainer.addStyleName(OpenGroupsStyles.HIERARCHY_PANE);
//	hierarchyContainer.setMargin(false,true,false,false);
	hierarchyContainer.construct();
	
//	hierarchyContainer.setSizeFull();
	hierarchyContainer.setWidth("100%");
	hierarchyContainer.setHeight("100%");
	frame.addComponent(hierarchyContainer);
	frame.addComponent(entityContent);
	frame.setExpandRatio(hierarchyContainer, 1);
	frame.setExpandRatio(entityContent, 3);
	
	mainContent.addComponent(frame);
	
//	/* footer */
//	CssLayout footer = new CssLayout();
//	footer.setWidth("100%");
//	footer.addComponent(new Label("@Metaguvernare"));
//	mainContent.addComponent(footer);
	
	mainContent.setExpandRatio(frame, 1);
    }
    
    private void initListeners() {
	
	CausalHierarchyContainer chc = getHierarchyContainer();
	chc.setStartDepthChangedListener(app.getHierarchyStartDepthListener());
	chc.setTreeExpandListener(app.getHierarchyTreeExpandListener());
	chc.setSelectionChangedListener(app.getHierarchyItemSelectedListener());

    }
    
    public void init() {
	createLayout();
	initListeners();
    }

    // public void setHeaderContent(Component c) {
    // headerContainer.removeAllComponents();
    // headerContainer.addComponent(c);
    // }

    public void setUserActionsTabVisible(boolean visible) {
	userActionsTab.setVisible(visible);
    }

    public void setFragmentToEntity(Entity entity) {
	if(entity == null) {
	    return;
	}
	String fragment = OpenGroupsUtil.getFragmentForEntity(entity);
	logger.debug("Update fragment to '" + fragment + "'");
	uriUtility.setFragment(fragment, false);
    }
    
    public String getFragment() {
	if(uriUtility != null) {
	    return uriUtility.getFragment();
	}
	return null;
    }


    public ComponentContainer getEntityContent() {
	return entityContent;
    }

    /**
     * @return the userActionsContainer
     */
    public CssLayout getUserActionsContainer() {
	return userActionsContainer;
    }

    /**
     * @return the uriUtility
     */
    public UriFragmentUtility getUriUtility() {
	return uriUtility;
    }

    /**
     * @return the hierarchyContainer
     */
    public CausalHierarchyContainer getHierarchyContainer() {
        return hierarchyContainer;
    }

    /**
     * @return the header
     */
    public CssLayout getHeader() {
        return header;
    }
    
}
