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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.ComponentContainer.ComponentDetachEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;

public class TabSheetWrapper extends VerticalLayout {

    /**
     * 
     */
    private static final long serialVersionUID = 3698347424423553531L;

    private TabSheet ts;
    private VerticalLayout displayArea;
    /**
     * key-real content, value-fake content
     */
    private Map<Component, Component> fakeContentMap = new HashMap<Component, Component>();
    /**
     * key-fake content, value-real content
     */
    private Map<Component, Component> realContentMap = new HashMap<Component, Component>();
    
    private Component selected;
    
    private boolean initialized;

    private void initialize() {
	ts = new TabSheet();
	ts.addStyleName(Reindeer.TABSHEET_MINIMAL);
	displayArea = new VerticalLayout();

	ts.addListener(new SelectedTabChangeListener() {

	    @Override
	    public void selectedTabChange(SelectedTabChangeEvent event) {
		onNewTabSelected();
	    }
	});
	
	ts.addListener(new ComponentDetachListener() {
	    
	    @Override
	    public void componentDetachedFromContainer(ComponentDetachEvent event) {
		onTabClosed(event.getDetachedComponent());
	    }
	});

	addComponent(ts);
	addComponent(displayArea);
    }

    private void onNewTabSelected() {
	Component selectedFakeContent = ts.getSelectedTab();
	selected = realContentMap.get(selectedFakeContent);
	
	/* display */
	updateDisplayArea();
	fireSelectedTabChange();
    }
    
    private void onTabClosed(Component closedFakeContent) {
	/* clean up */
	Component closedRealContent = realContentMap.remove(closedFakeContent);
	fakeContentMap.remove(closedRealContent);
	
	fireComponentDetachEvent(closedRealContent);
    }
    
    private void updateDisplayArea() {
	displayArea.removeAllComponents();
	displayArea.addComponent(selected);
    }
    
    public Tab addTab(Component tabContent, String caption, Resource resource) {
	if (!initialized) {
	    initialized = true;
	    initialize();
	}
	Component fakeContent = fakeContentMap.get(tabContent);
	if (fakeContent == null) {
	    /* create a new empty content for this tab */
	    fakeContent = new VerticalLayout();
	    fakeContentMap.put(tabContent, fakeContent);
	    realContentMap.put(fakeContent, tabContent);
	}
	return ts.addTab(fakeContent, caption, resource);
    }
    
    public Component getSelectedTab() {
	return selected;
    }
    
    public void setSelectedTab(Component tabContent) {
	if(tabContent != null && selected != tabContent && fakeContentMap.containsKey(tabContent)) {
	    ts.setSelectedTab(fakeContentMap.get(tabContent));
	}
    }
    
    public Tab getTab(Component tabComponent) {
	Component fakeComponent = fakeContentMap.get(tabComponent);
	if(fakeComponent != null) {
	    return ts.getTab(fakeComponent);
	}
	return null;
    }

    /* Events */
    

    private static final Method SELECTED_TAB_CHANGE_METHOD;
    static {
        try {
            SELECTED_TAB_CHANGE_METHOD = SelectedTabChangeListenerWrapper.class
                    .getDeclaredMethod("selectedTabChange",
                            new Class[] { SelectedTabChangeEventWrapper.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in TabSheet");
        }
    }

    /**
     * Selected tab change event. This event is sent when the selected (shown)
     * tab in the tab sheet is changed.
     * 
     * @author IT Mill Ltd.
     * @version
     * 6.4.2
     * @since 3.0
     */
    public class SelectedTabChangeEventWrapper extends Component.Event {

        /**
         * New instance of selected tab change event
         * 
         * @param source
         *            the Source of the event.
         */
        public SelectedTabChangeEventWrapper(Component source) {
            super(source);
        }

        /**
         * TabSheet where the event occurred.
         * 
         * @return the Source of the event.
         */
        public TabSheetWrapper getTabSheetWrapper() {
            return (TabSheetWrapper) getSource();
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
    public interface SelectedTabChangeListenerWrapper extends Serializable {

        /**
         * Selected (shown) tab in tab sheet has has been changed.
         * 
         * @param event
         *            the selected tab change event.
         */
        public void selectedTabChange(SelectedTabChangeEventWrapper event);
    }

    /**
     * Adds a tab selection listener
     * 
     * @param listener
     *            the Listener to be added.
     */
    public void addListener(SelectedTabChangeListenerWrapper listener) {
        addListener(SelectedTabChangeEventWrapper.class, listener,
                SELECTED_TAB_CHANGE_METHOD);
    }

    /**
     * Removes a tab selection listener
     * 
     * @param listener
     *            the Listener to be removed.
     */
    public void removeListener(SelectedTabChangeListenerWrapper listener) {
        removeListener(SelectedTabChangeEventWrapper.class, listener,
                SELECTED_TAB_CHANGE_METHOD);
    }

    /**
     * Sends an event that the currently selected tab has changed.
     */
    protected void fireSelectedTabChange() {
        fireEvent(new SelectedTabChangeEventWrapper(this));
    }

}
