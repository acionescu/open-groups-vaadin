package ro.zg.presentation.utils;

public class ListColumn {
    private String width;
    private String description;
    
    public ListColumn(String width, String description) {
	super();
	this.width = width;
	this.description = description;
    }
    public String getWidth() {
        return width;
    }
    public void setWidth(String width) {
        this.width = width;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
