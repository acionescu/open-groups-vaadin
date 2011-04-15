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

import ro.zg.open_groups.resources.OpenGroupsResources;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;

public class OpenGroupsMainWindow extends Window {
	/**
     * 
     */
	private static final long serialVersionUID = -8492978667795915161L;
	private GridLayout headerPanel;
	private TabSheet entitiesTabSheet;
	private CssLayout userActionsContainer;
	private TabSheet mainTabSheet;
	private Tab entitiesTab;
	private Tab userActionsTab;
	private VerticalLayout mainContent;
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
		// headerContainer=new VerticalLayout();
		// mainContent.addComponent(headerContainer);

		// Panel contentContainer = new Panel();
		// VerticalLayout hlv = new VerticalLayout();
		// hlv.setMargin(false);
		// contentContainer.setContent(hlv);
		// mainContent.addComponent(contentContainer);

		entitiesTabSheet = new TabSheet();
		// entitiesTabSheet.setSizeFull();
		entitiesTabSheet.addStyleName(Reindeer.TABSHEET_MINIMAL);
		// entitiesTabSheet.setImmediate(true);

		userActionsContainer = new CssLayout();

//		mainTabSheet = new TabSheet();
//		// mainTabSheet.setSizeFull();
//		// mainTabSheet.addStyleName(Reindeer.TABSHEET_MINIMAL);
//		// mainTabSheet.setImmediate(true);
//
//		entitiesTab = mainTabSheet.addTab(entitiesTabSheet, OpenGroupsResources
//				.getMessage("metagovernment.tab.caption"), null);
//		userActionsTab = mainTabSheet.addTab(userActionsContainer,
//				OpenGroupsResources.getMessage("user.tab.caption"), null);
//		userActionsTab.setVisible(false);

		// Panel mainTabSheetContainer = new Panel();
		// mainTabSheetContainer.addComponent(mainTabSheet);
		// mainTabSheetContainer.addStyleName("gray");

		// mainContent.addComponent(mainTabSheet);
		mainContent.addComponent(entitiesTabSheet);
		// mainContent.setExpandRatio(mainTabSheet, 1f);

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

	/**
	 * @return the entitiesTabSheet
	 */
	public TabSheet getEntitiesTabSheet() {
		return entitiesTabSheet;
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
