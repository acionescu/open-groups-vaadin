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
package ro.zg.netcell.vaadin.action.user;

import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.DataTranslationUtils;
import ro.zg.netcell.vaadin.ExtendedForm;
import ro.zg.netcell.vaadin.ExtendedForm.FormCommitEvent;
import ro.zg.netcell.vaadin.ExtendedForm.FormListener;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.constants.ApplicationConfigParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.User;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;
import ro.zg.util.data.GenericNameValue;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

public class LoginHandler extends UserHandler {

    /**
     * 
     */
    private static final long serialVersionUID = 2384226053251524497L;

    private static final String LOCAL_LOGIN_TYPE = "local";
    private static final String OPENID_LOGIN_TYPE = "openid";

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	Window w = new Window();
	w.setModal(true);
	OpenGroupsApplication app = actionContext.getApp();
	if(app.getAppConfigManager().isInstancePrivate()){
	    w.setClosable(false);
	}

	OpenGroupsMainWindow mainWindow = actionContext.getWindow();
	
	UserAction ua = actionContext.getUserAction();
	w.setWidth("600px");
	w.setHeight("300px");
	w.center();
	w.setCaption(getMessage(ua.getActionName() + ".window.caption"));
	mainWindow.addWindow(w);

	ComponentContainer loginView = getLoginView(actionContext);

	w.setContent(loginView);

    }

    private ComponentContainer getLoginView(ActionContext actionContext) {
	
	OpenGroupsApplication app = actionContext.getApp();
	Map<String, Map<String, String>> loginTypes = (Map<String, Map<String, String>>) app.getAppConfigManager()
		.getApplicationConfigParam(ApplicationConfigParam.LOGIN_TYPES);

	System.out.println("login types: " + loginTypes);

	HorizontalLayout hl = new HorizontalLayout();
	hl.setSizeFull();

	if (loginTypes == null || loginTypes.containsKey(LOCAL_LOGIN_TYPE)) {
	    Component localView = getLocalLoginView(actionContext);
	    hl.addComponent(localView);
	    hl.setExpandRatio(localView, 2f);
	}

	if (loginTypes != null) {
	    Map<String, String> openIdProviders = loginTypes
		    .get(OPENID_LOGIN_TYPE);

	    if (openIdProviders != null) {
		CssLayout openIdLayout = new CssLayout();
		openIdLayout.setSizeFull();
		openIdLayout.addStyleName("openid-login-pane");
		
		VerticalLayout openIdContainer = new VerticalLayout();
		openIdContainer.setSizeFull();
		openIdContainer.setMargin(true);
		openIdLayout.addComponent(openIdContainer);
		
//		Label openIdLoginMessage = new Label(OpenGroupsResources.getMessage("openid.login.message"));
//		openIdLoginMessage.addStyleName("openid-title");
//		openIdContainer.addComponent(openIdLoginMessage);
//		openIdContainer.setExpandRatio(openIdLoginMessage, 0.1f);
		
		for (Map.Entry<String, String> e : openIdProviders.entrySet()) {
		    
		    Component providerLink = getOpenidLoginComponent(actionContext, e);
		    openIdContainer.addComponent(providerLink);
		    openIdContainer.setComponentAlignment(providerLink, Alignment.MIDDLE_CENTER);
		    openIdContainer.setExpandRatio(providerLink, 1f);

		}

		hl.addComponent(openIdLayout);
		hl.setComponentAlignment(openIdLayout, Alignment.MIDDLE_CENTER);
		hl.setExpandRatio(openIdLayout, 1f);
	    }
	}
	return hl;
    }

    private Component getOpenidLoginComponent(
	    final ActionContext actionContext,
	    final Map.Entry<String, String> entry) {
	final Button lb = new Button();

	lb.setIcon(OpenGroupsResources.getIcon(entry.getKey() + ".png"));
	lb.addStyleName(BaseTheme.BUTTON_LINK);
	String providerName = entry.getKey();
	providerName = Character.toUpperCase(providerName.charAt(0)) + providerName.substring(1);  
	lb.setDescription(OpenGroupsResources.getMessage("openid.login.message",providerName));
	lb.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		actionContext.getApp().openIdLogin(entry.getValue());
		actionContext.getWindow().removeWindow(lb.getWindow());
	    }
	});
	return lb;
    }

    private ComponentContainer getLocalLoginView(ActionContext actionContext) {
	VerticalLayout layout = new VerticalLayout();
	layout.setSizeFull();
	OpenGroupsApplication app = actionContext.getApp();

	Form form = getLocalLoginForm(actionContext.getUserAction(), app,
		actionContext.getWindow(), actionContext.getEntity(),actionContext);

	layout.addComponent(form);
	form.setWidth("60%");
	layout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

	HorizontalLayout footerActionsContainer = new HorizontalLayout();
	footerActionsContainer.setSpacing(true);
	footerActionsContainer.setMargin(true);
	layout.addComponent(footerActionsContainer);
	layout.setComponentAlignment(footerActionsContainer,
		Alignment.MIDDLE_RIGHT);
	addFooterActions(footerActionsContainer, app, actionContext);

	return layout;
    }

    private Form getLocalLoginForm(final UserAction ua,
	    final OpenGroupsApplication app, final Window window,
	    final Entity entity,ActionContext actionContext) {

	ExtendedForm form = ua.generateForm(actionContext);
	// EntityDefinitionSummary loginDef =
	// getActionsManager().getFlowDefinitionSummary(ua.getAction());
	// List<InputParameter> inputParams = loginDef.getInputParameters();
	//
	// form.populateFromInputParameterList(inputParams);
	// form.setCaption("Insert login credentials");
	// form.setWidth("40%");
	// form.setHeight("30%");
	// form.getLayout().setMargin(true);

	form.addListener(new FormListener() {

	    @Override
	    public void onCommit(FormCommitEvent event) {
		Form form = event.getForm();
		doLogin(form, ua, app, window, entity);
	    }
	});

	return form;
    }

    private void doLogin(Form form, UserAction ua, OpenGroupsApplication app,
	    Window window, Entity entity) {
	form.setComponentError(null);
	Map<String, Object> paramsMap = (Map<String, Object>)form.getValue();
	paramsMap.put(
		"password",
		UsersManager.getInstance().encrypt(
			paramsMap.get("password").toString()));
	paramsMap.put("ip", app.getAppContext().getBrowser().getAddress());

	CommandResponse response = executeAction(new ActionContext(ua, app,
		null), paramsMap);
	GenericNameValue result = (GenericNameValue) response.get("result");
	GenericNameValueList list = (GenericNameValueList) result.getValue();
	if (list.size() < 1) {
	    form.setComponentError(new UserError("Invalid username or password"));
	    return;
	}
	/* so we have a user */
	GenericNameValueContext userRow = (GenericNameValueContext) list
		.getValueForIndex(0);
	User user = app.getModel().getUserFromParamsContext(userRow);
	/* close the login window */
	window.removeWindow(form.getWindow());
	app.loginAndShowEntity(user, entity);
    }

    private void addFooterActions(final ComponentContainer container,
	    final OpenGroupsApplication app, final ActionContext ac) {
	UserActionList actionsList = ActionsManager.getInstance()
		.getGlobalActions(ActionLocations.LOGIN_FOOTER);
	if (actionsList != null && actionsList.getActions() != null) {
	    final Window window = ac.getWindow();
	    for (final UserAction ua : actionsList.getActions().values()) {
		Button link = new Button(ua.getDisplayName());
		link.addStyleName(BaseTheme.BUTTON_LINK);
		container.addComponent(link);
		link.addListener(new ClickListener() {

		    @Override
		    public void buttonClick(ClickEvent event) {
			window.removeWindow(container.getWindow());
			ua.executeHandler(null, app, null, ac);
		    }
		});
	    }
	}
    }
}
