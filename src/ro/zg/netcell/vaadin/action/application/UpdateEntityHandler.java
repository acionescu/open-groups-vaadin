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
package ro.zg.netcell.vaadin.action.application;

import java.util.Map;

import ro.zg.netcell.control.CommandResponse;
import ro.zg.netcell.vaadin.DataTranslationUtils;
import ro.zg.netcell.vaadin.DefaultForm;
import ro.zg.netcell.vaadin.DefaultForm.FormCommitEvent;
import ro.zg.netcell.vaadin.DefaultForm.FormListener;
import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.constants.ComplexEntityParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.UserAction;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UpdateEntityHandler extends OpenGroupsActionHandler {

	/**
     * 
     */
	private static final long serialVersionUID = -6381955444237800871L;

	@Override
	public void handle(ActionContext actionContext) throws Exception {
		ComponentContainer targetContainer = actionContext.getTargetContainer();
		Entity entity = actionContext.getEntity();
		targetContainer.removeAllComponents();
		DefaultForm form = getForm(entity, actionContext.getUserAction(),
				actionContext.getApp(), targetContainer, actionContext);
		targetContainer.addComponent(form);

	}

	private DefaultForm getForm(final Entity entity, final UserAction ua,
			final OpenGroupsApplication application,
			final ComponentContainer targetContainer, final ActionContext ac) {
		final DefaultForm form = ua.generateForm();
		form.getField("title").setValue(entity.getTitle());
		form.getField("content").setValue(entity.getContent());
		// EntityDefinitionSummary actionDef =
		// getActionsManager().getFlowDefinitionSummary(ua.getAction());
		// List<InputParameter> actionInputParams =
		// actionDef.getInputParameters();
		// Map<String,Object> values = new HashMap<String, Object>();
		// Entity selectedEntity = application.getSelectedEntity();
		// values.put("title", selectedEntity.getTitle());
		// values.put("content", selectedEntity.getContent());
		//	
		// List<InputParameter> userInputParams =
		// ua.getUserInputParamsList(actionInputParams,values);
		//	
		// form.setFormFieldFactory(new
		// DefaultFormFieldFactory(userInputParams));
		// form.populateFromInputParameterList(userInputParams);
		form.addListener(new FormListener() {

			@Override
			public void onCommit(FormCommitEvent event) {

				Map<String, Object> paramsMap = DataTranslationUtils
						.getFormFieldsAsMap(event.getForm());
				Entity selectedEntity = entity;

				paramsMap.put("userId", application.getCurrentUserId());
				paramsMap.put("entityId", selectedEntity.getId());
				String complexType = ua.getTargetEntityComplexType();
				paramsMap.put("allowDuplicateTitle", getAppConfigManager()
						.getComplexEntityBooleanParam(complexType,
								ComplexEntityParam.ALLOW_DUPLICATE_TITLE));
				paramsMap.put("titleChanged", !paramsMap.get("title").equals(
						selectedEntity.getTitle()));
				CommandResponse response = executeAction(new ActionContext(ua,
						application, entity), paramsMap);
				if (response.isSuccessful()) {
					if ("titleExists".equals(response.getValue("exit"))) {
						String message = application.getMessage(ua
								.getTargetEntityType().toLowerCase()
								+ ".already.exists.with.title");
						form.setComponentError(new UserError(message));
					} else {
						application.refreshEntity(entity, ac);
						displaySuccessfulMessage(entity, ua, application,
								targetContainer, ac);
					}
				}
				/* refresh after update */
				application.refreshEntity(entity, ac);
			}
		});

		return form;
	}

	private void displaySuccessfulMessage(final Entity entity,
			final UserAction ua, final OpenGroupsApplication app,
			final ComponentContainer targetComponent, final ActionContext ac) {
		/* store current target component */
		// final ComponentContainer targetComponent = app.getTargetComponent();
		targetComponent.removeAllComponents();
		String entityTypeLowerCase = ua.getTargetEntityType().toLowerCase();
		String createdSuccessfullyMessage = app.getMessage(entityTypeLowerCase
				+ ".updated.successfully");
		String newUpdateMessage = app.getMessage("new.update");

		HorizontalLayout container = new HorizontalLayout();
		container.setSizeFull();
		container.setSpacing(true);
		targetComponent.addComponent(container);

		Label success = new Label(createdSuccessfullyMessage);
		container.addComponent(success);

		Button newUpdate = new Button(newUpdateMessage);
		container.addComponent(newUpdate);
		newUpdate.addListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// Entity entity = new Entity(entityId);
				// app.pushSelectedEntity(entity);
				// app.executeAction(ActionsManager.OPEN_ENTITY_IN_WINDOW);
				ua.executeHandler(entity, app, targetComponent, ac);
			}
		});

	}

}
