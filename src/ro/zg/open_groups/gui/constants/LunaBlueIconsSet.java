package ro.zg.open_groups.gui.constants;

public class LunaBlueIconsSet extends OpenGroupsIconsSet{

    public LunaBlueIconsSet(String dirPath) {
	super(dirPath);
	
	addSize(SMALL, "14x14");
	addSize(MEDIUM, "24x24");
	addSize(LARGE, "32x32");
	
	addIcon(COMMENT, "comment.png");
	addIcon(ISSUE, "help.png");
	addIcon(SOLUTION, "light_bulb.png");
	addIcon(LEFT_ARROW, "back.png");
	addIcon(RIGHT_ARROW, "next.png");
	addIcon(NEW_POST,"new_page.png");
	addIcon(UPDATED_POST,"edit_page.png");
	addIcon(PARENT, "up.png");
	addIcon(VOTE_UP, "accept.png");
	addIcon(VOTE_DOWN, "remove.png");
	addIcon(CANCEL,"delete.png");
	addIcon(REFRESH,"refresh.png");
	
	addIcon(UPSTREAM_HIERARCHY_OFF, "download.png");
	addIcon(UPSTREAM_HIERARCHY_ON, "next.png");
	
	addIcon(SHOW_CAUSE_OFF, "next.png");
	addIcon(SHOW_CAUSE_ON, "up.png");
    }

}
