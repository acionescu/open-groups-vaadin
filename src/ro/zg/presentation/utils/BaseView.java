package ro.zg.presentation.utils;

public abstract class BaseView<T,E extends UserEvent> {
    private UserEventHandler<E> userEventHandler;
    private ViewsManager viewsManager;
    
    public BaseView() {
	super();
    }
    
    /**
     * Override when needed
     */
    protected void init(){
	
    }

    public abstract void update(T updateData);

    public void setUserEventHandler(UserEventHandler<E> userEventHandler) {
        this.userEventHandler = userEventHandler;
    }

    public void setViewsManager(ViewsManager viewsManager) {
        this.viewsManager = viewsManager;
    }
    
    protected <V extends BaseView<?, E>, H extends UserEventHandler<E>> V createView(Class<V> clazz, H handler){
	return viewsManager.createView(clazz, handler);
    }
    
    protected <V extends BaseView<?, E>> V createView(Class<V> clazz){
	return viewsManager.createView(clazz, userEventHandler);
    }
    
    protected void handleEvent(E event){
	userEventHandler.handleEvent(event);
    }
}
