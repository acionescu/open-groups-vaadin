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
import ro.zg.open_groups.user.UsersManager;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.User;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RegisterUserHandler extends UserHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 5264090280343643862L;

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
	w.setCaption(getMessage(ua.getActionName()+".window.caption"));
	mainWindow.addWindow(w);
	
	VerticalLayout layout = new VerticalLayout();
	layout.setSizeFull();
	Form form = getRegisterForm(actionContext.getUserAction(), app, actionContext.getWindow(),actionContext.getEntity());
	w.setContent(layout);
	layout.addComponent(form);
	form.setWidth("60%");
//	form.setHeight("30%");
	layout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
	
	
    }

    private Form getRegisterForm(final UserAction ua, final OpenGroupsApplication app, final Window window, final Entity entity) {
	DefaultForm form =  ua.generateForm();
	
	form.addListener(new FormListener() {
	    
	    @Override
	    public void onCommit(FormCommitEvent event) {
		Form form = event.getForm();
		doRegister(form,ua,app,window,entity);
	    }
	});
	
	return form;
    }
    
    private void doRegister(Form form, UserAction ua, OpenGroupsApplication app, Window window, Entity entity) {
	form.setComponentError(null);
	Map<String,Object> paramsMap = DataTranslationUtils.getFormFieldsAsMap(form);
	String password = (String)paramsMap.get("password");
	String passwordCheck = (String)paramsMap.remove("password-again");
	if(!password.equals(passwordCheck)) {
	    String message = getMessage("user.register.password.check.failed");
	    form.setComponentError(new UserError(message));
	    return;
	}
	paramsMap.put("password", UsersManager.getInstance().encrypt(password));
	paramsMap.put("ip",app.getAppContext().getBrowser().getAddress());
	CommandResponse response = executeAction(new ActionContext(ua, app, null), paramsMap);
	String exitCode = (String)response.getValue("exit");
	if("userExists".equals(exitCode)) {
	    String message = getMessage("user.register.user.exists.error");
	    form.setComponentError(new UserError(message));
	    return;
	}
	if("emailExists".equals(exitCode)) {
	    String message = getMessage("user.register.email.exists.error");
	    form.setComponentError(new UserError(message));
	    return;
	}
	/* user successfully registered, probably */
	GenericNameValueList list = (GenericNameValueList)response.getValue("result");
	GenericNameValueContext userRow = (GenericNameValueContext)list.getValueForIndex(0);
	User user = getUserFromParamsContext(userRow);
	app.login(user,entity);
	/* close the login window */
	window.removeWindow(form.getWindow());
	/* refresh main application window */
	app.openInActiveWindow(entity);
    }
}
