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
import ro.zg.netcell.vaadin.DefaultForm;
import ro.zg.netcell.vaadin.DefaultForm.FormCommitEvent;
import ro.zg.netcell.vaadin.DefaultForm.FormListener;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RequestPasswordResetHandler extends UserHandler {

    /**
     * 
     */
    private static final long serialVersionUID = -5328135537633934763L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	Window w = new Window();
	w.setModal(true);
	OpenGroupsApplication app = actionContext.getApp();
	OpenGroupsMainWindow mainWindow = actionContext.getWindow();
	UserAction ua = actionContext.getUserAction();
	w.setWidth("400px");
	w.setHeight("300px");
	w.center();
	w.setCaption(getMessage(ua.getActionName() + ".window.caption"));
	mainWindow.addWindow(w);

	VerticalLayout layout = new VerticalLayout();
	layout.setSizeFull();
	Form form = getForm(actionContext.getUserAction(), app);
	w.setContent(layout);
	layout.addComponent(form);
	form.setWidth("60%");
	// form.setHeight("30%");
	layout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
    }

    private Form getForm(final UserAction ua, final OpenGroupsApplication app) {
	final DefaultForm form = ua.generateForm();
	form.addListener(new FormListener() {

	    @Override
	    public void onCommit(FormCommitEvent event) {
		form.setComponentError(null);
		sendPasswordResetRequest(event.getForm(), ua, app);
	    }
	});
	form.setDescription(getMessage(ua.getActionName()+".header.message"));
	return form;
    }

    private void sendPasswordResetRequest(Form form, UserAction ua, OpenGroupsApplication app) {
	form.setComponentError(null);
	Map<String, Object> paramsMap = DataTranslationUtils.getFormFieldsAsMap(form);
	String instanceName = app.getAppConfigManager().getInstanceName();
	paramsMap.put("subject", getMessage(ua.getActionName()+".subject", instanceName));
	paramsMap.put("message", getMessage(ua.getActionName()+".message",instanceName,app.getBaseAppUrl()));
	
	CommandResponse response = executeAction(new ActionContext(ua, app, null), paramsMap);

	if (response.isSuccessful()) {
	    String exit = (String) response.getValue("exit");
	    if (exit.equals("noSuchEmail")) {
		String message = getMessage(ua.getActionName()+".no-such-email");
		form.setComponentError(new UserError(message));
		return;
	    }
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
