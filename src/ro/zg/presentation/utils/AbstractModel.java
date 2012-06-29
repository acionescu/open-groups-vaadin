package ro.zg.presentation.utils;

import java.util.ArrayList;
import java.util.List;

public class AbstractModel {
    private List<BaseView<? extends AbstractModel>> views = new ArrayList<BaseView<? extends AbstractModel>>();
    
    
    public void addView(BaseView<? extends AbstractModel> view){
	views.add(view);
    }
    
    protected <T extends AbstractModel> void dispatchUpdate(T bo){
	for(BaseView view : views){
	    view.update(bo);
	}
    }
}
