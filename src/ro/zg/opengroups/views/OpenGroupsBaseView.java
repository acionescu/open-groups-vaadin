package ro.zg.opengroups.views;

import ro.zg.presentation.utils.BaseView;
import ro.zg.presentation.utils.UserEvent;
import ro.zg.presentation.utils.UserEventHandler;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;

public abstract class OpenGroupsBaseView<T> extends BaseView<T,UserEvent>{
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
