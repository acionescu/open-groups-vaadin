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
package ro.zg.open_groups.gui.components;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

public class CustomTabSheet extends VerticalLayout {

    /**
     * 
     */
    private static final long serialVersionUID = -5331446610981425434L;

    private Map<Component, Button> tabs = new HashMap<Component, Button>();
    private HorizontalLayout tabsArea = new HorizontalLayout();
    private VerticalLayout displayArea = new VerticalLayout();
    private Component currentSelectedTabContent;
    boolean initialized;

    private void initialize() {
	tabsArea.setSpacing(true);
	addComponent(tabsArea);
	addComponent(displayArea);
    }

    public void addTab(final Component tabContent, String caption) {
	Button tab = tabs.get(tabContent);
	if (tab == null) {
	    tab = new Button(caption);
	    tab.addStyleName(BaseTheme.BUTTON_LINK);
	    tabs.put(tabContent, tab);
	    /* if this is the first tab in this tabsheet select it */
	    if (tabs.size() == 1) {
		if (!initialized) {
		    /* laizy initialization when the first tab is added */
		    initialize();
		    initialized = true;
		}
		tabsArea.addComponent(tab);
		selectTab(tabContent, tab);
	    }
	    else {
		tabsArea.addComponent(tab);
	    }
	    /* add click listener for this tab */
	    tab.addListener(new ClickListener() {
	        
	        @Override
	        public void buttonClick(ClickEvent event) {
	            selectTab(tabContent, event.getButton());
	        }
	    });
	    
	} else {
	    tab.setCaption(caption);
	}
    }

    private void selectTab(Component tabContent, Button tab) {
	if (currentSelectedTabContent == tabContent) {
	    return;
	}
	displayArea.removeAllComponents();
	displayArea.addComponent(tabContent);
	currentSelectedTabContent = tabContent;
	/* mark the tab as selected */
	// TODO
	/* fire tab change */
	fireSelectedTabChange();
    }

    public void setSelectedTab(Component tabContent) {
	Button tab = tabs.get(tabContent);
	if (tab != null) {
	    selectTab(tabContent, tab);
	}
    }

    public Component getSelectedTab() {
	return currentSelectedTabContent;
    }
    
    /* listeners and stuff */
    
    private static final Method SELECTED_TAB_CHANGE_METHOD;
    static {
        try {
            SELECTED_TAB_CHANGE_METHOD = CustomSelectedTabChangeListener.class
                    .getDeclaredMethod("selectedTabChange",
                            new Class[] { CustomSelectedTabChangeEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in TabSheet");
        }
    }
    
    public class CustomSelectedTabChangeEvent extends Component.Event {

        /**
         * New instance of selected tab change event
         * 
         * @param source
         *            the Source of the event.
         */
        public CustomSelectedTabChangeEvent(Component source) {
            super(source);
        }

        /**
         * TabSheet where the event occurred.
         * 
         * @return the Source of the event.
         */
        public CustomTabSheet getTabSheet() {
            return (CustomTabSheet) getSource();
        }
    }

    /**
     * Selected tab change event listener. The listener is called whenever
     * another tab is selected, including when adding the first tab to a
     * tabsheet.
     * 
     * @author IT Mill Ltd.
     * 
     * @version
     * 6.4.2
     * @since 3.0
     */
    public interface CustomSelectedTabChangeListener extends Serializable {

        /**
         * Selected (shown) tab in tab sheet has has been changed.
         * 
         * @param event
         *            the selected tab change event.
         */
        public void selectedTabChange(CustomSelectedTabChangeEvent event);
    }

    /**
     * Adds a tab selection listener
     * 
     * @param listener
     *            the Listener to be added.
     */
    public void addListener(CustomSelectedTabChangeListener listener) {
        addListener(CustomSelectedTabChangeEvent.class, listener,
                SELECTED_TAB_CHANGE_METHOD);
    }

    /**
     * Removes a tab selection listener
     * 
     * @param listener
     *            the Listener to be removed.
     */
    public void removeListener(CustomSelectedTabChangeListener listener) {
        removeListener(CustomSelectedTabChangeEvent.class, listener,
                SELECTED_TAB_CHANGE_METHOD);
    }

    /**
     * Sends an event that the currently selected tab has changed.
     */
    protected void fireSelectedTabChange() {
        fireEvent(new CustomSelectedTabChangeEvent(this));
    }

}
