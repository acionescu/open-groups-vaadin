package ro.zg.opengroups.views;

import ro.zg.presentation.utils.BaseView;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public abstract class OpenGroupsBaseView<T> extends BaseView<T>{
    protected ComponentContainer container;
    
    public OpenGroupsBaseView(){
	container = new CssLayout();
    }
    
    public ComponentContainer getContainer() {
        return container;
    }

    public void setContainer(ComponentContainer container) {
        this.container = container;
    }
    
    
    
}
