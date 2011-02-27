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

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.DataTranslationUtils;
import ro.zg.netcell.vaadin.DefaultForm;
import ro.zg.netcell.vaadin.DefaultForm.FormCommitEvent;
import ro.zg.netcell.vaadin.DefaultForm.FormListener;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.vo.ActionUri;
import ro.zg.opengroups.vo.UserAction;

public class ResetPasswordHandler extends UserHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 2418881064410568630L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	Window w = new Window();
	w.setModal(true);
	OpenGroupsApplication app = actionContext.getApp();
	OpenGroupsMainWindow mainWindow = app.getMainWindow();
	UserAction ua = actionContext.getUserAction();
	w.setWidth("400px");
	w.setHeight("300px");
	w.center();
	w.setCaption(getMessage(ua.getActionName() + ".window.caption"));
	mainWindow.addWindow(w);

	VerticalLayout layout = new VerticalLayout();
	layout.setSizeFull();
	Form form = getForm(actionContext.getUserAction(), app, actionContext.getParams());
	w.setContent(layout);
	layout.addComponent(form);
	form.setWidth("60%");
	// form.setHeight("30%");
	layout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
	
    }

    private Form getForm(final UserAction ua, final OpenGroupsApplication app,final Map<String,Object> params) {
	final DefaultForm form = ua.generateForm();
	form.addListener(new FormListener() {

	    @Override
	    public void onCommit(FormCommitEvent event) {
		form.setComponentError(null);
		resetPassword(event.getForm(), ua, app,params);
	    }
	});
	form.setDescription(getMessage(ua.getActionName()+".header.message"));
	return form;
    }
    
    private void resetPassword(Form form, UserAction ua, OpenGroupsApplication app,Map<String,Object> actionParams) {
	ActionUri au = (ActionUri)actionParams.get("actionUri");
	
	form.setComponentError(null);
	Map<String, Object> paramsMap = DataTranslationUtils.getFormFieldsAsMap(form);
	String password = (String)paramsMap.get("password");
	String passwordCheck = (String)paramsMap.get("password-again");
	if(!password.equals(passwordCheck)) {
	    String message = getMessage("user.register.password.check.failed");
	    form.setComponentError(new UserError(message));
	    return;
	}
	paramsMap.put("password", UsersManager.getInstance().encrypt(password));
	paramsMap.put("userHash", au.getParamUri());
	
	CommandResponse response = executeAction(new ActionContext(ua, app, null), paramsMap);
	
	if(response.isSuccessful()) {
	    displaySuccessfulMessage(form.getWindow(), ua.getActionName()+".success");
	}
    }
    
    private void displaySuccessfulMessage(Window w, String messageKey) {
	VerticalLayout l = new VerticalLayout();
	l.setSizeFull();
	Label message = new Label(getMessage(messageKey));
	message.setWidth("60%");
	l.addComponent(message);
	l.setComponentAlignment(message, Alignment.MIDDLE_CENTER);
	w.setContent(l);
    }
}
