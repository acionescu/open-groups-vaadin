package ro.zg.netcell.vaadin;

import java.util.Map;

import com.vaadin.ui.Field;

public interface FormFieldController<F extends Field> {
    
    F buildField(Map<String, String> fieldConfigMap, FormContext formContext);
    
    void initField(F field, FormContext formContext);
    
    Object getFieldData(F field);

}
