package ro.zg.opengroups.views;

import ro.zg.presentation.utils.BaseView;

import com.vaadin.ui.CssLayout;

public abstract class OpenGroupsBaseView<T> extends BaseView<T>{
    protected CssLayout container;
    
    public OpenGroupsBaseView(){
	container = new CssLayout();
    }
    
    public CssLayout getContainer() {
        return container;
    }

    public void setContainer(CssLayout container) {
        this.container = container;
    }
    
    
    
}
