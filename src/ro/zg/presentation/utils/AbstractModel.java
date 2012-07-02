package ro.zg.presentation.utils;

import java.util.ArrayList;
import java.util.List;

public class AbstractModel<E extends UserEvent> {
    private List<BaseView<? extends AbstractModel<E>,E>> views = new ArrayList<BaseView<? extends AbstractModel<E>,E>>();
    
    
    public void addView(BaseView<? extends AbstractModel<E>,E> view){
	views.add(view);
    }
    
    protected <T extends AbstractModel<E>> void dispatchUpdate(T bo){
	for(BaseView view : views){
	    view.update(bo);
	}
    }
}
