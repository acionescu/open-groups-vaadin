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
package ro.zg.netcell.vaadin;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import ro.zg.netcell.vo.InputParameter;

import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ExtendedForm extends Form {

    private static Method FORM_COMMIT_METHOD;
    
    private String formId;
    private FormController formController;
    
    public ExtendedForm() {
	super();
    }
    
    public ExtendedForm(String formId, FormController formController) {
	super();
	this.formId = formId;
	this.formController = formController;
    }



    public void populateFromInputParameterList(List<InputParameter> list) {
	setItemDataSource(DataTranslationUtils.inputParameterListToPropertysetItem(list));
	for (InputParameter p : list) {
	    String pName = p.getName();
	    Field f = getField(pName);
	    f.setRequired(p.isMandatory());
	    if (pName.equals("password")) {
		((TextField) f).setSecret(true);
	    }
	    Object value = p.getValue();
	    if (value != null) {
		f.setPropertyDataSource(new ObjectProperty(value));
	    }
	}

	Button submitButton = new Button("Submit");
	getFooter().addComponent(submitButton);
	submitButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		commit();
		fireCommitEvent();
	    }
	});
	Button discardButton = new Button("Discard");
	getFooter().addComponent(discardButton);
	discardButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		discard();
	    }
	});
    }
    
    public Map<String,Object> getValue(){
	return formController.getFormData(this);
    }

    public void addSubmitButton(String caption) {
	Button submitButton = new Button(caption);
	getFooter().addComponent(submitButton);
	submitButton.addListener(new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		try {
		    commit();
		    fireCommitEvent();
		} catch (Validator.InvalidValueException e) {
		    /* do nothing, as an error message will be displayed to the user */
		}
	    }
	});
    }

    static {
	try {
	    FORM_COMMIT_METHOD = FormListener.class
		    .getDeclaredMethod("onCommit", new Class[] { FormCommitEvent.class });
	} catch (final java.lang.NoSuchMethodException e) {
	    // This should never happen
	    throw new java.lang.RuntimeException("Internal error finding methods in DefaultForm");
	}
    }

    /**
     * Adds the button click listener.
     * 
     * @param listener
     *            the Listener to be added.
     */
    public void addListener(FormListener listener) {
	addListener(FormCommitEvent.class, listener, FORM_COMMIT_METHOD);
    }

    /**
     * Removes the button click listener.
     * 
     * @param listener
     *            the Listener to be removed.
     */
    public void removeListener(FormListener listener) {
	removeListener(FormCommitEvent.class, listener, FORM_COMMIT_METHOD);
    }

    /**
     * Emits the options change event.
     */
    protected void fireCommitEvent() {
	fireEvent(new ExtendedForm.FormCommitEvent(this));
    }

    /**
     * 
     */
    private static final long serialVersionUID = -9076769623028583645L;

    public class FormCommitEvent extends Component.Event {

	public FormCommitEvent(Component source) {
	    super(source);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8640639705929583525L;

	public Form getForm() {
	    return (Form) getSource();
	}

    }

    public interface FormListener extends Serializable {

	void onCommit(FormCommitEvent event);
    }

}
