package ro.zg.opengroups.vo;

import ro.zg.open_groups.gui.OpenGroupsMainWindow;
import ro.zg.open_groups.model.OpenGroupsModel;

public class OpenGroupsApplicationState {
    
    private Entity rootEntity;
    private OpenGroupsMainWindow activeWindow;
    private Entity activeEntity;
    
    public OpenGroupsApplicationState() {
	OpenGroupsModel model = OpenGroupsModel.getInstance();
	rootEntity = model.getRootEntity();
	
    }


    /**
     * @return the rootEntity
     */
    public Entity getRootEntity() {
        return rootEntity;
    }


    /**
     * @return the activeWindow
     */
    public OpenGroupsMainWindow getActiveWindow() {
        return activeWindow;
    }


    /**
     * @param activeWindow the activeWindow to set
     */
    public void setActiveWindow(OpenGroupsMainWindow activeWindow) {
        this.activeWindow = activeWindow;
    }


    /**
     * @return the activeEntity
     */
    public Entity getActiveEntity() {
        return activeEntity;
    }


    /**
     * @param activeEntity the activeEntity to set
     */
    public void setActiveEntity(Entity activeEntity) {
        this.activeEntity = activeEntity;
    }
    
}
