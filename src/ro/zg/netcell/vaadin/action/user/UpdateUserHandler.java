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
import ro.zg.opengroups.vo.User;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UpdateUserHandler extends UserHandler{

    /**
     * 
     */
    private static final long serialVersionUID = -8361387640203388261L;

    @Override
    public void handle(ActionContext actionContext) throws Exception {
	ComponentContainer targetContainer = actionContext.getTargetContainer();
	targetContainer.removeAllComponents();
	Form form = getUpdateForm(actionContext.getUserAction(), actionContext.getApp(), actionContext.getTargetContainer(),actionContext);
	targetContainer.addComponent(form);
    }
    
    private Form getUpdateForm(final UserAction ua, final OpenGroupsApplication app, final ComponentContainer container, final ActionContext ac) {
	DefaultForm form =  ua.generateForm();
	User currentUser = app.getCurrentUser();
//	form.getField("email").setValue(currentUser.getEmail());
	form.getField("email").setPropertyDataSource(new ObjectProperty(currentUser.getEmail()));
	form.addListener(new FormListener() {
	    
	    @Override
	    public void onCommit(FormCommitEvent event) {
		Form form = event.getForm();
		doUpdate(form,ua,app,container,ac);
	    }
	});
	
	return form;
    }
    
    private void doUpdate(Form form, UserAction ua, OpenGroupsApplication app,ComponentContainer container, final ActionContext ac) {
	form.setComponentError(null);
	User user = app.getCurrentUser();
	Map<String,Object> paramsMap = DataTranslationUtils.getFormFieldsAsMap(form);
	if(!isDataMofified(user, paramsMap)) {
	    app.showNotification("data.not.changed");
	    return;
	}
	paramsMap.put("userId", app.getCurrentUserId());
	CommandResponse response = executeAction(new ActionContext(ua, app, null), paramsMap);
	String exitCode = (String)response.getValue("exit");
	
	if("emailExists".equals(exitCode)) {
	    String message = getMessage("user.register.email.exists.error");
	    form.setComponentError(new UserError(message));
	    return;
	}
	
	user.setEmail((String)paramsMap.get("email"));
	showSuccessMessage(app, container, ua,ac);
    }

    private boolean isDataMofified(User u, Map<String,Object> params) {
	return !u.getEmail().equals(params.get("email"));
    }
    
    private void showSuccessMessage(final OpenGroupsApplication app, final ComponentContainer container,final UserAction ua, final ActionContext ac) {
	container.removeAllComponents();
	Label msg = new Label(getMessage("user.data.succesfully.updated"));
	Button button = new Button(getMessage("new.user.data.update"));
	
	button.addListener(new ClickListener() {
	    
	    @Override
	    public void buttonClick(ClickEvent event) {
		ua.executeHandler(null, app, container,ac);
	    }
	});
	
	container.addComponent(msg);
	container.addComponent(button);
    }
}
