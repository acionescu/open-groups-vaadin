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

import javax.servlet.http.HttpServletRequest;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityState;
import ro.zg.opengroups.vo.OpenGroupsApplicationState;
import ro.zg.opengroups.vo.TabContainer;
import ro.zg.opengroups.vo.User;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;
import ro.zg.vaadin.util.WindowsManger;

import com.vaadin.Application;
import com.vaadin.Application.WindowAttachEvent;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

public class OpenGroupsApplication extends Application {
	/**
     * 
     */
	private static final long serialVersionUID = 2543794851816120227L;

	private WebApplicationContext appContext;
	private ActionsManager actionsManager = ActionsManager.getInstance();
	// private Deque<Entity> selectedEntityStack = new ArrayDeque<Entity>();
	private TabSheet entitiesTabSheet;
	private Map<Long, TabContainer> tabsForEntities = new HashMap<Long, TabContainer>();
	private User currentUser;
	private Map<Long, Entity> openEntities = new HashMap<Long, Entity>();
	private Deque<Exception> errorsStack = new ArrayDeque<Exception>();
	private static Logger logger = MasterLogManager.getLogger("APP-LOGGER");

	private boolean botClient;
	private String lastFragment;
	private long lastFragmentUpdate;

	private static final String VAADIN_WINDOW_SEPARATOR = "_";
	private static final String MY_WINDOW_SEPARATOR = ":";
	private WindowsManger windowsManager;
	private OpenGroupsApplicationState appState;
	private OpenGroupsUriHandler uriHandler;

	@Override
	public void init() {
		System.out.println("init");
		windowsManager = new WindowsManger();
		appState = new OpenGroupsApplicationState();
		setTheme("open-groupstheme");
		appContext = (WebApplicationContext) getContext();
		uriHandler = new OpenGroupsUriHandler(this);

		printUserAgentInfo();
		initSession();
		// initMainWindow();
		// refreshMainWindow();

		getContext().addTransactionListener(new TransactionListener() {

			@Override
			public void transactionStart(Application application,
					Object transactionData) {
				HttpServletRequest req = (HttpServletRequest) transactionData;
				String fragment = req.getParameter("fr");
				if (fragment != null) {
					fragment = fragment.trim();
					OpenGroupsApplication app = (OpenGroupsApplication) application;
					String oldFragment = app.getActiveWindow().getUriUtility()
							.getFragment();
					logger.debug("Transaction start: new->'" + fragment
							+ "' + old->'" + oldFragment + "'");
					if (oldFragment == null) {
						logger.debug("Process fragment");
						uriHandler.handleFragment(fragment);
//						app.getActiveWindow().getUriUtility().setFragment(
//								fragment, false);
					}
					/* this may be a refresh */
					else if(fragment.equals(oldFragment)){
						uriHandler.handleFragment(fragment);
					}
				}
			}

			@Override
			public void transactionEnd(Application application,
					Object transactionData) {
				// TODO Auto-generated method stub

			}
		});

		Window mainWindow = getWindow("showEntity");
	}

	private boolean checkOptimalBrowser() {
		WebBrowser wb = appContext.getBrowser();
		if (wb.isFirefox()) {
			return false;
		}
		return true;
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

	public void refreshEntity(Entity entity, ActionContext ac) {
		actionsManager.executeAction(ActionsManager.OPEN_AND_REFRESH_ENTITY,
				entity, this, entity.getEntityContainer(), false, ac);
	}

	public void fullyRefreshEntity(Entity entity, ActionContext ac) {
		EntityState state = entity.getState();
		if (state.getDesiredActionTabsQueue().size() == 0) {
			state.setDesiredActionTabsQueue(new ArrayDeque<String>(state
					.getCurrentActionTabsQueue()));
		}
		state.setOpened(true);
		OpenGroupsMainWindow window = ac.getWindow();
		logger.debug("Refresh entity " + entity.getId() + " in window "
				+ window);
		entity.setEntityContainer(window.getEntityContent());
		actionsManager.executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS,
				entity, this, window.getEntityContent(), false, ac);
		if (hasErrors()) {
			handleErrors();
			return;
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

		// actionsManager.executeAction(ActionsManager.OPEN_ENTITY_WITH_ACTIONS,
		// e, this, getMainWindow()
		// .getEntityContent(), false);
		openInActiveWindow(e);

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
	public void login(User currentUser, Entity entity) {
		if (currentUser != null) {
			this.currentUser = currentUser;
			UsersManager.getInstance().addUser(currentUser);

			/* refresh main application window */
			openInActiveWindow(entity);
		}
	}

	public void logout() {
		UsersManager.getInstance().removeUser(currentUser.getUserId());
		this.currentUser = null;
		getAppContext().getHttpSession().invalidate();
	}

	/**
	 * Opens an entity in a new window
	 * 
	 * @param entity
	 */
	public void openInActiveWindow(Entity entity) {
		if (entity == null) {
			entity = getRootEntity();
		} else if (entity.getId() != getRootEntity().getId()) {
			/*
			 * this is stupid, should be handled via the complex_entity_type
			 * table
			 */
			entity.getState().setEntityTypeVisible(true);
		}
		appState.setActiveEntity(entity);
		entity.getState().setOpened(true);
		OpenGroupsMainWindow activeWindow = getActiveWindow();
		entity.setEntityContainer(activeWindow.getEntityContent());
		logger.debug("Open entity " + entity + " in window " + activeWindow);
		actionsManager.executeAction(ActionsManager.OPEN_ENTITY_IN_WINDOW,
				entity, this, activeWindow, false);
	}

	protected OpenGroupsMainWindow createWindow(String name) {
		Integer nextId = windowsManager.getNextWindowId(name);
		name = name + MY_WINDOW_SEPARATOR + nextId;
		System.out.println("->createWindow(" + name + ")");
		OpenGroupsMainWindow w = new OpenGroupsMainWindow("Metaguvernare");
		// w.setName(name);
		w.createLayout();
		System.out.println("createWindow(" + name + ") -> " + w);
		return w;
	}

	@Override
	public Window getWindow(String name) {
		System.out.println("->getWindow(" + name + ")");
		Window w = super.getWindow(name);
		if (w == null && !name.contains("UIDL") /*
												 * &&!name.contains(
												 * VAADIN_WINDOW_SEPARATOR) &&
												 * !name
												 * .contains(MY_WINDOW_SEPARATOR
												 * ) &&
												 * name.equals("showEntity")
												 */) {
			w = createWindow(name);
			initWindow((OpenGroupsMainWindow) w);
			setMainWindow(w);

		}
		System.out.println("getWindow(" + name + ") -> " + w);
		if (w != null) {
			appState.setActiveWindow((OpenGroupsMainWindow) w);
		}
		return w;
	}

	private void initWindow(OpenGroupsMainWindow w) {
		// OpenGroupsUriHandler uriHandler = new OpenGroupsUriHandler(this);
		w.getUriUtility().addListener(uriHandler);
		w.addURIHandler(uriHandler);
		addCloseListener(w);
	}

	public OpenGroupsMainWindow getActiveWindow() {
		return appState.getActiveWindow();
	}

	public Entity getActiveEntity() {
		return appState.getActiveEntity();
	}

	private void addCloseListener(final Window w) {
		w.addListener(new Window.CloseListener() {

			@Override
			public void windowClose(CloseEvent e) {
				if (w == getMainWindow()) {
					return;
				}
				removeWindow(w);
				// String[] args = w.getName().split(MY_WINDOW_SEPARATOR);
				// windowsManager.removeWindowId(args[0], new Integer(args[1]));
				System.out.println("removed " + w.getName());
			}
		});
	}

	/**
	 * @return the rootEntity
	 */
	public Entity getRootEntity() {
		return appState.getRootEntity();
	}
	
	public boolean isRootSelected(){
		return appState.isRootSelected();
	}
	
	public String getCurrentUri() {
		return appState.getCurrentUri();
	}

	public void setCurrentUri(String currentUri) {
		appState.setCurrentUri(currentUri);
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
