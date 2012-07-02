package ro.zg.presentation.utils;

public class UserEvent {
    private String eventType;
    private Object source;
    private Object target;
    
    
    public UserEvent(String eventType, Object source, Object target) {
	super();
	this.eventType = eventType;
	this.source = source;
	this.target = target;
    }


    public String getEventType() {
        return eventType;
    }


    public Object getSource() {
        return source;
    }


    public Object getTarget() {
        return target;
    }
    
    
}
