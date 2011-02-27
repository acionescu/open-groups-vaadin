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
package ro.zg.netcell.vaadin.action.user;

import java.sql.Timestamp;

import ro.zg.netcell.vaadin.action.OpenGroupsActionHandler;
import ro.zg.opengroups.vo.User;
import ro.zg.util.data.GenericNameValueContext;

public abstract class UserHandler extends OpenGroupsActionHandler{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    protected User getUserFromParamsContext(GenericNameValueContext userRow) {
	User user = new User();
	String username = (String)userRow.getValue("username");
	long userId = (Long)userRow.getValue("id");
	Timestamp lastLogin =(Timestamp)userRow.getValue("last_login");
	String email = (String)userRow.getValue("email");
	user.setUserId(userId);
	user.setUsername(username);
	user.setLastLogin(lastLogin);
	user.setEmail(email);
	return user;
    }
}