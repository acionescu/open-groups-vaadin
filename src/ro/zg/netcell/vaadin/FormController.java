package ro.zg.netcell.vaadin;

import java.util.Map;

import com.vaadin.ui.Form;

public interface FormController {

    ExtendedForm createForm(FormContext formContext);
    
    Map<String,Object> getFormData(Form form);
}
