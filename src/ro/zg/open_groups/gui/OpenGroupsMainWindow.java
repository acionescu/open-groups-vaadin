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

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.UriFragmentUtility;
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
    private CssLayout mainContent;
    private AbstractLayout entityContent;
    private UriFragmentUtility uriUtility;
    private CausalHierarchyContainer hierarchyContainer;
    private CssLayout frameContent;

    public OpenGroupsMainWindow(OpenGroupsApplication app, String name) {
	super.setCaption(name);
	this.app = app;

    }

    private void createLayout() {
	/* add the uri utility */
	uriUtility = new UriFragmentUtility();
	uriUtility.setWidth("0px");
	uriUtility.setHeight("0px");
	mainContent = new CssLayout();
	mainContent.addComponent(uriUtility);

	mainContent.setWidth("90%");
	mainContent.setHeight("100%");
	this.setContent(mainContent);

	uriUtility.addListener(app.getUriHandler());
	addURIHandler(app.getUriHandler());

	mainContent.setMargin(false);
	mainContent.addStyleName(OpenGroupsStyles.MAIN_PANE);

	header = new CssLayout();
	header.setWidth("100%");
	mainContent.addComponent(header);

	userActionsContainer = new CssLayout();

	CssLayout entityFrame = new CssLayout();
	entityFrame.setHeight("96%");
	// entityFrame.setWidth("100%"); uncomment this and you will have
	// problems
	entityFrame.addStyleName("entity-frame");
	entityFrame.setMargin(true);

	entityContent = new CssLayout();
	entityContent.addStyleName(OpenGroupsStyles.ENTITY_PANE);

	entityFrame.addComponent(entityContent);

	frameContent = new CssLayout();
	frameContent.setWidth("100%");
	frameContent.setHeight("100%");
	frameContent.addStyleName(OpenGroupsStyles.FRAME_PANE);

	hierarchyContainer = new CausalHierarchyContainer();
	hierarchyContainer.addStyleName(OpenGroupsStyles.HIERARCHY_PANE);
	hierarchyContainer.setWidth("350px");
	hierarchyContainer.setHeight("96%");
	hierarchyContainer.construct();

	frameContent.addComponent(hierarchyContainer);
	frameContent.addComponent(entityFrame);

	mainContent.addComponent(frameContent);
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
	if (entity == null) {
	    return;
	}
	String fragment = OpenGroupsUtil.getFragmentForEntity(entity);
	logger.debug("Update fragment to '" + fragment + "'");
	uriUtility.setFragment(fragment, false);
    }

    public String getFragment() {
	if (uriUtility != null) {
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

    public void setContentVisible(boolean visible) {
	header.setVisible(visible);
	frameContent.setVisible(visible);
    }
}
