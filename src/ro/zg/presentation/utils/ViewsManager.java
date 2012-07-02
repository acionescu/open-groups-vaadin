package ro.zg.presentation.utils;

public class ViewsManager {
    
    private static ViewsManager instance;
    
    private ViewsManager(){
	
    }
    
    public static ViewsManager getInstance(){
	if(instance == null){
	    instance = new ViewsManager();
	}
	return instance;
    }

    public <E extends UserEvent,T extends BaseView<?,E>, H extends UserEventHandler<E>> T createView(Class<T> clazz, H handler ){
	try {
	    T view = clazz.newInstance();
	    view.setViewsManager(this);
	    view.setUserEventHandler(handler);
	    view.init();
	    return view;
	} catch (InstantiationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }
    
}
