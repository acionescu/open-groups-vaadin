package ro.zg.presentation.utils;

public interface UserEventHandler<E extends UserEvent> {
    
    void handleEvent(E event);

}
