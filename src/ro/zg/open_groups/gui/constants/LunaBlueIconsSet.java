/*******************************************************************************
 * Copyright 2012 AdrianIonescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
	addIcon(ADD,"add.png");
	addIcon(ACCEPT,"accept.png");
	addIcon(REFRESH,"refresh.png");
	addIcon(SEARCH,"zoom.png");
	addIcon(UPSTREAM_HIERARCHY_OFF, "download.png");
	addIcon(UPSTREAM_HIERARCHY_ON, "next.png");
	
	addIcon(SHOW_CAUSE_OFF, "next.png");
	addIcon(SHOW_CAUSE_ON, "up.png");
	
	addIcon(USER, "user.png");
    }

}
