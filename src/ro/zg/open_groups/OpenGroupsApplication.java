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
package ro.zg.open_groups;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.open_groups.gui.constants.UriFragments;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityState;
import ro.zg.opengroups.vo.TabContainer;
import ro.zg.opengroups.vo.User;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

import com.vaadin.Application;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer.ComponentDetachEvent;
import com.vaadin.ui.ComponentContainer.ComponentDetachListener;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.Notification;

public class OpenGroupsApplication extends Application {
	/**
     * 
     */
	private static final long serialVersionUID = 2543794851816120227L;

	private WebApplicationContext appContext;
	private ActionsManager actionsManager = ActionsManager.getInstance();
	private Deque<Entity> selectedEntityStack = new ArrayDeque<Entity>();
	private TabSheet entitiesTabSheet;
	private Map<Long, TabContainer> tabsForEntities = new HashMap<Long, TabContainer>();
	private User currentUser;
	private Entity rootEntity;
	private Map<Long, Entity> openEntities = new HashMap<Long, Entity>();
	private UriFragmentUtility uriUtility;
	private Deque<Exception> errorsStack = new ArrayDeque<Exception>();
	private static Logger logger = MasterLogManager.getLogger("APP-LOGGER");

	private boolean botClient;
	private String lastFragment;
	private long lastFragmentUpdate;

	@Override
	public void init() {
		System.out.println("init");
		setTheme("open-groupstheme");
		appContext = (WebApplicationContext) getContext();

		WebBrowser wb = appContext.getBrowser();
		
		// if (wb.getBrowserApplication().contains("Googlebot")) {
		// setBotClient(true);
		// } else {
		printUserAgentInfo();
		initSession();
		initMainWindow();
		refreshMainWindow();
		// }
	}

	private boolean checkOptimalBrowser() {
		WebBrowser wb = appContext.getBrowser();
		if (wb.isFirefox()) {
			return false;
		}
		return true;
	}

	private void displayBrowserWarning() {
		System.out.println("display warning");
		final Window w = new Window(getMessage("browser.warning.caption"));
		w.setWidth("400px");
		w.setHeight("300px");
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		Label l = new Label(getMessage("browser.warning"));
		l.setContentMode(Label.CONTENT_XHTML);
		l.setWidth("60%");
		vl.addComponent(l);
		vl.setComponentAlignment(l, Alignment.MIDDLE_CENTER);

		Button continueBtn = new Button(getMessage("continue.anyway"));
		continueBtn.addListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getMainWindow().removeWindow(w);
				refreshMainWindow();
			}
		});
		vl.addComponent(continueBtn);
		vl.setComponentAlignment(continueBtn, Alignment.MIDDLE_CENTER);

		w.setContent(vl);
		w.center();
		getMainWindow().addWindow(w);
	}

	private void printUserAgentInfo() {
		WebBrowser wb = appContext.getBrowser();
		StringBuffer sb = new StringBuffer();
		sb.append("Session initialized from ip: ").append(wb.getAddress());
		sb.append(", user agent: ").append(wb.getBrowserApplication());
		sb.append(", version: ").append(wb.getBrowserMajorVersion());
		logger.info(sb.toString());
	}

	@Override
	public void close() {
		super.close();
		logger.info("Session ended from ip "
				+ appContext.getBrowser().getAddress());
	}

	private void initSession() {
		getAppContext().getHttpSession().setMaxInactiveInterval(-1);
	}

	private void initMainWindow() {
		OpenGroupsMainWindow mainWindow = getMainWindow();
		if (mainWindow == null) {
			mainWindow = new OpenGroupsMainWindow("Metaguvernare");

			addWindow(mainWindow);
			mainWindow.createLayout();

			entitiesTabSheet = mainWindow.getEntitiesTabSheet();
			OpenGroupsUriHandler uriHandler = new OpenGroupsUriHandler(this);
			mainWindow.addURIHandler(uriHandler);
			uriUtility = mainWindow.getUriUtility();
			uriUtility.addListener(uriHandler);
			// mainWindow.addParameterHandler(new OpenGroupsParamsHandler());

		} else {
			mainWindow.clear();
		}

		entitiesTabSheet.addListener(new SelectedTabChangeListener() {

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				/*
				 * when the tab is changed, set the associated entity as
				 * selected entity, and set the tab last used container as
				 * targetComponent
				 */
				AbstractComponent container = (AbstractComponent) entitiesTabSheet
						.getSelectedTab();
				if (container != null) {
					Entity newEntity = (Entity) container.getData();
					popSelectedEntity();
					pushSelectedEntity(newEntity);

					/* set uri fragment */
					// uriUtility.setFragment(UriFragments.SHOW_ENTITY_FRAGMENT
					// + newEntity.getId(), false);
					setFragmentToCurrentEntity();

					TabContainer tabContainer = tabsForEntities.get(newEntity
							.getId());

					if (tabContainer.isRefreshOn()) {
						System.out.println("refreshing tab");
						fullyRefreshEntity(newEntity);
						tabContainer.setRefreshOn(false);
					}
					// System.out.println("current selected entity: " +
					// getSelectedEntity());
					// System.out.println("tab changed " +
					// newEntity.getTitle());
					// selectedEntityStack.push(newEntity);
					// System.out.println("stack:" +
					// selectedEntityStack.size());
				}
			}
		});

		entitiesTabSheet.addListener(new ComponentDetachListener() {

			@Override
			public void componentDetachedFromContainer(
					ComponentDetachEvent event) {
				AbstractComponentContainer c = (AbstractComponentContainer) event
						.getDetachedComponent();
				Entity entity = (Entity) c.getData();
				openEntities.remove(entity.getId());
			}
		});
	}

	public void setFragmentToCurrentEntity() {
		Entity currentEntity = getSelectedEntity();
		EntityState currentEntityState = currentEntity.getState();
		uriUtility.setFragment(UriFragments.SHOW_ENTITY_FRAGMENT
				+ currentEntity.getId() + "/"
				+ currentEntityState.getCurrentPageForCurrentAction()
				+ currentEntityState.getCurrentActionPath(), false);
	}

	public String getFullUrl() {
		return getURL() + "#" + uriUtility.getFragment();
	}

	public String getFragment() {
		return uriUtility.getFragment();
	}

	private void setDummyUser() {
		User u = new User();
		u.setUserId(3);
		u.setUsername("first_user");
		login(u);
	}

	public void refreshMainWindow() {
		// System.out.println("Refreshing main window");
		actionsManager.executeAction(ActionsManager.LOAD_MAIN_WINDOW, null,
				this, null, false);
	}

	public void refreshCurrentSelectedEntity() {
		// System.out.println("refreshing current selected entity. stack " +
		// selectedEntityStack);
		// // if (selectedEntityStack.size() == 1) {
		// // refreshMainWindow();
		// // } else {
		// actionsManager.executeAction(ActionsManager.OPEN_AND_REFRESH_ENTITY,
		// getSelectedEntity(), this,
		// getSelectedEntity().getEntityContainer(), false);
		// // }
		refreshEntity(getSelectedEntity());
	}

	public void refreshEntity(Entity entity) {
		actionsManager.executeAction(ActionsManager.OPEN_AND_REFRESH_ENTITY,
				entity, this, entity.getEntityContainer(), false);
	}

	public void fullyRefreshEntity(Entity entity) {
		EntityState state = entity.getState();
		TabContainer tabContainer = getTemporaryTab(entity);
		tabContainer.setRefreshOn(true);
		if (state.getDesiredActionTabsQueue().size() == 0) {
			state.setDesiredActionTabsQueue(new ArrayDeque<String>(state
					.getCurrentActionTabsQueue()));
		}
		ComponentContainer cc = tabContainer.getContainer();
		actionsManager.executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS,
				entity, this, cc, false);
		if (hasErrors()) {
			handleErrors();
		}
	}

	public void fullyRefreshCurrentSelectedEntity() {
		Entity selectedEntity = getSelectedEntity();
		if (selectedEntity != null) {
			fullyRefreshEntity(selectedEntity);
		} else {
			refreshMainWindow();
		}
	}

	public void showNotification(String messageKey) {
		String message = OpenGroupsResources.getMessage(messageKey);
		Notification notification = new Notification(message);
		getMainWindow().showNotification(notification);
	}

	public void showNotification(String messageKey, int type) {
		String message = OpenGroupsResources.getMessage(messageKey);
		Notification notification = new Notification(message, type);
		getMainWindow().showNotification(notification);
	}

	public String getMessage(String msgKey) {
		// return messagesBundle.getString(msgKey);
		return OpenGroupsResources.getMessage(msgKey);
	}

	public TabContainer getTemporaryTab(Entity entity) {
		boolean tabCloseable = (getSelectedEntity() != null);
		return getTab(entity, tabCloseable);
	}

	public TabContainer getTab(Entity entity, boolean closable) {

		TabContainer tabContainer = tabsForEntities.get(entity.getId());
		boolean isNew = false;

		Panel tabContent = null;
		if (tabContainer == null) {
			tabContent = new Panel();
			tabContent.setScrollable(false);
			tabContainer = new TabContainer(tabContent, true);
			tabsForEntities.put(entity.getId(), tabContainer);
			isNew = true;
		} else {
			tabContent = (Panel) tabContainer.getContainer();
		}
		Tab tab = entitiesTabSheet.getTab(tabContent);
		if (tab == null) {
			tabContent.setData(entity);
			openEntities.put(entity.getId(), entity);
			tab = entitiesTabSheet.addTab(tabContent, shortenCaption(entity
					.getTitle(), 20), null);
			tab.setClosable(closable);
			/* if this is a closed tab */
			if (!isNew) {
				/* clean entity state */
				entity.getState().getActiveActions().clear();
				entity.getHeaderActionsContainers().clear();
				entity.getState().setOpened(false);
				isNew = true;
			}
		}

		tabContainer.setNewlyCreated(isNew);
		return tabContainer;
	}

	public TabContainer openTemporaryTab(Entity entity) {
		boolean tabCloseable = (getSelectedEntity() != null);
		return openTab(entity, tabCloseable);
	}

	public TabContainer openTab(Entity entity, boolean closable) {
		Entity prevEntity = popSelectedEntity();
		pushSelectedEntity(entity);
		entity.getState().setOpened(true);
		if (entity.getId() != rootEntity.getId()) {
			entity.getState().setEntityTypeVisible(true);
		}
		boolean isNew = false;
		Panel tabContent = null;
		/* check if a tab container for this entity already exists */
		TabContainer tabContainer = tabsForEntities.get(entity.getId());
		/* if it does not, create it, mark as new */
		if (tabContainer == null) {
			tabContent = new Panel();
			tabContent.setScrollable(false);
			tabContainer = new TabContainer(tabContent, true);
			tabsForEntities.put(entity.getId(), tabContainer);
			isNew = true;
		} else {
			tabContent = (Panel) tabContainer.getContainer();
		}
		Tab tab = entitiesTabSheet.getTab(tabContent);
		if (tab == null) {
			tabContent.setData(entity);
			openEntities.put(entity.getId(), entity);

			tab = entitiesTabSheet.addTab(tabContent, shortenCaption(entity
					.getTitle(), 20), null);
			tab.setClosable(closable);
			actionsManager.executeAction(
					ActionsManager.OPEN_ENTITY_WITH_ACTIONS, entity, this,
					tabContent, false);
			tab.setCaption(shortenCaption(entity.getTitle(), 20));
			/* if this is a closed tab */
			if (!isNew) {
				/* clean entity state */
				entity.getState().getActiveActions().clear();
				entity.getHeaderActionsContainers().clear();
				entity.getState().setOpened(false);
				entity.getState().resetPageInfo();
				isNew = true;
			}
		} else if (tabContainer.isRefreshOn()) {
			actionsManager.executeAction(
					ActionsManager.OPEN_ENTITY_WITH_ACTIONS, entity, this,
					tabContent, false);
			tabContainer.setRefreshOn(false);
		}

		tabContainer.setNewlyCreated(isNew);
		entitiesTabSheet.setSelectedTab(tabContent);
		if (hasErrors()) {
			handleErrors();
			entitiesTabSheet.setSelectedTab(tabsForEntities.get(
					prevEntity.getId()).getContainer());
			entitiesTabSheet.removeTab(tab);
		}
		return tabContainer;
	}

	public void openEntityById(Long id, String actionPath, int pageNumber) {
		Entity e = openEntities.get(id);
		if (e == null) {
			e = new Entity(id);
		} else {
			if (!e.getState().getCurrentActionPath().equals(actionPath)) {
				tabsForEntities.get(e.getId()).setRefreshOn(true);
			}
		}
		e.getState().setDesiredActionsPath(actionPath);
		e.getState().setCurrentPageForAction("/" + actionPath, pageNumber);
		openTemporaryTab(e);
	}

	public TabContainer addTab(Entity entity, boolean closable) {
		Panel tabContent = null;
		/* check if a tab container for this entity already exists */
		TabContainer tabContainer = tabsForEntities.get(entity.getId());
		/* if it does not, create it, mark as new */
		if (tabContainer == null) {
			tabContent = new Panel();
			tabContent.setScrollable(false);
			tabContainer = new TabContainer(tabContent, true);
			tabsForEntities.put(entity.getId(), tabContainer);
		} else {
			tabContent = (Panel) tabContainer.getContainer();
		}

		Tab tab = entitiesTabSheet.getTab(tabContent);
		if (tab == null) {
			tabContent.setData(entity);
			openEntities.put(entity.getId(), entity);
			tab = entitiesTabSheet.addTab(tabContent, shortenCaption(entity
					.getTitle(), 20), null);
			tab.setClosable(closable);
		}

		return tabContainer;
	}

	public void setSelectedTab(ComponentContainer c) {
		entitiesTabSheet.setSelectedTab(c);
	}

	public Entity getOpenEntityForId(long id) {
		return openEntities.get(id);
	}

	private String shortenCaption(String c, int length) {
		if (c == null) {
			return null;
		}
		if (c.length() > length) {
			return c.substring(0, length - 3) + "...";
		}
		return c;
	}

	public void removeTabContainer(Entity e) {
		tabsForEntities.remove(e.getId());
	}

	public void pushSelectedEntity(Entity e) {
		selectedEntityStack.push(e);
		// System.out.println("pushed " + e);
	}

	public Entity popSelectedEntity() {
		if (selectedEntityStack.size() > 0) {
			return selectedEntityStack.pop();
		}
		return null;
	}

	public Entity getSelectedEntity() {
		return selectedEntityStack.peek();
	}

	public void pushError(Exception e) {
		errorsStack.push(e);
	}

	public boolean hasErrors() {
		return !errorsStack.isEmpty();
	}

	public void handleErrors() {
		String errorMessage = "";
		while (errorsStack.size() > 0) {
			Exception e = errorsStack.pop();
			if (e instanceof ContextAwareException) {
				ContextAwareException cae = (ContextAwareException) e;
				errorMessage += "<br>- "
						+ getMessage(((ContextAwareException) e).getType());
			} else {
				errorMessage += "<br>- " + getMessage("system.error");
			}
		}
		Notification notification = new Notification(
				getMessage("error.notification.caption"), errorMessage,
				Notification.TYPE_ERROR_MESSAGE);
		getMainWindow().showNotification(notification);

		/* to be sure */
		errorsStack.clear();
	}

	/**
	 * @return the appContext
	 */
	public WebApplicationContext getAppContext() {
		return appContext;
	}

	/**
	 * @return the mainWindow
	 */
	public OpenGroupsMainWindow getMainWindow() {
		return (OpenGroupsMainWindow) super.getMainWindow();
	}

	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	public Long getCurrentUserId() {
		if (currentUser != null) {
			return currentUser.getUserId();
		}
		return null;
	}

	/**
	 * @param currentUser
	 *            the currentUser to set
	 */
	public void login(User currentUser) {
		if (currentUser != null) {
			this.currentUser = currentUser;
			UsersManager.getInstance().addUser(currentUser);

			/* refresh main application window */
			tabsForEntities.get(getRootEntity().getId()).setRefreshOn(true);
			refreshMainWindow();
			fullyRefreshCurrentSelectedEntity();
		}
	}

	public void logout() {
		UsersManager.getInstance().removeUser(currentUser.getUserId());
		this.currentUser = null;
		getAppContext().getHttpSession().invalidate();
	}

	/**
	 * @return the rootEntity
	 */
	public Entity getRootEntity() {
		return rootEntity;
	}

	/**
	 * @param rootEntity
	 *            the rootEntity to set
	 */
	public void setRootEntity(Entity rootEntity) {
		this.rootEntity = rootEntity;
	}

	@Override
	public void terminalError(Terminal.ErrorEvent event) {
		// Call the default implementation.
		super.terminalError(event);
		// Some custom behaviour.
		if (getMainWindow() != null) {
			Notification notification = new Notification(
					getMessage("error.notification.caption"), "<br>- "
							+ getMessage("system.error"),
					Notification.TYPE_ERROR_MESSAGE);
			getMainWindow().showNotification(notification);
		}
	}

	/**
	 * @return the botClient
	 */
	public boolean isBotClient() {
		return botClient;
	}

	/**
	 * @param botClient
	 *            the botClient to set
	 */
	public void setBotClient(boolean botClient) {
		this.botClient = botClient;
	}

	/**
	 * @return the lastFragment
	 */
	public String getLastFragment() {
		return lastFragment;
	}

	/**
	 * @param lastFragment
	 *            the lastFragment to set
	 */
	public void setLastFragment(String lastFragment) {
		this.lastFragment = lastFragment;
	}

	/**
	 * @return the lastFragmentUpdate
	 */
	public long getLastFragmentUpdate() {
		return lastFragmentUpdate;
	}

	/**
	 * @param lastFragmentUpdate
	 *            the lastFragmentUpdate to set
	 */
	public void setLastFragmentUpdate(long lastFragmentUpdate) {
		this.lastFragmentUpdate = lastFragmentUpdate;
	}

}
