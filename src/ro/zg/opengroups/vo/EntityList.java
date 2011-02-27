/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
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
package ro.zg.opengroups.vo;

import java.util.ArrayList;

import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

public class EntityList {
    private ArrayList<Entity> itemsList;
    private boolean showEntityType;
    
    public EntityList(GenericNameValueList sourceList,boolean showEntityType) {
	this.showEntityType = showEntityType;
	update(sourceList);
    }
    
    private void update(GenericNameValueList source) {
	itemsList = new ArrayList<Entity>();
	
	for (int i = 0; i < source.size(); i++) {
	    GenericNameValueContext row = (GenericNameValueContext) source.getValueForIndex(i);
	    Entity currentEntity = new Entity(row);
	    currentEntity.getState().setEntityTypeVisible(showEntityType);
	    itemsList.add(currentEntity);
	}
    }

    /**
     * @return the itemsList
     */
    public ArrayList<Entity> getItemsList() {
        return itemsList;
    }


    /**
     * @return the showEntityType
     */
    public boolean isShowEntityType() {
        return showEntityType;
    }
}
