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

import ro.zg.opengroups.util.OpenGroupsUtil;
import ro.zg.opengroups.vo.Entity;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.TabSheet.Tab;

public class OpenGroupsMainWindow extends Window {
    /**
     * 
     */
    private static final long serialVersionUID = -8492978667795915161L;
    private static final Logger logger = MasterLogManager.getLogger("WINDOW");
    private GridLayout headerPanel;
    private CssLayout userActionsContainer;
    private Tab userActionsTab;
    private VerticalLayout mainContent;
    private CssLayout entityContent;
    private UriFragmentUtility uriUtility;

    public OpenGroupsMainWindow(String caption) {
	super(caption);
	// createLayout();
    }

    public OpenGroupsMainWindow() {
	// createLayout();
    }

    public void createLayout() {
	// mainContent = new VerticalLayout();
	// mainContent.setSizeFull();
	// mainContent.setMargin(true);
	//		
	// setContent(mainContent);

	mainContent = (VerticalLayout) this.getContent();

	uriUtility = new UriFragmentUtility();
	mainContent.addComponent(uriUtility);

	headerPanel = new GridLayout(2, 1);
	headerPanel.setWidth("100%");
	mainContent.addComponent(headerPanel);

	userActionsContainer = new CssLayout();

	entityContent = new CssLayout();
	entityContent.setSizeFull();
	mainContent.addComponent(entityContent);

    }

    public void clear() {
	removeAllComponents();
	createLayout();
    }

    // public void setHeaderContent(Component c) {
    // headerContainer.removeAllComponents();
    // headerContainer.addComponent(c);
    // }

    public void setUserActionsTabVisible(boolean visible) {
	userActionsTab.setVisible(visible);
    }

    public void setFragmentToEntity(Entity entity) {
	String fragment = OpenGroupsUtil.getFragmentForEntity(entity);
	logger.debug("Update fragment to '"+fragment+"'");
	uriUtility.setFragment(fragment, false);
    }

    /**
     * @return the headerPanel
     */
    public GridLayout getHeaderPanel() {
	return headerPanel;
    }

    /**
     * @param headerPanel
     *            the headerPanel to set
     */
    public void setHeaderPanel(GridLayout headerPanel) {
	this.headerPanel = headerPanel;
    }

    public CssLayout getEntityContent() {
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
}
