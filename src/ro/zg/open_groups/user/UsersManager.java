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
package ro.zg.open_groups.user;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.zg.open_groups.OpenGroupsApplication;
import ro.zg.opengroups.constants.UserTypes;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.User;

public class UsersManager {
    private static UsersManager _instance = new UsersManager();
    
    private Map<Long,User> activeUsers = new HashMap<Long, User>();
    private MessageDigest messageDigest;
    
    private UsersManager() {
	try {
	    messageDigest = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static UsersManager getInstance() {
	return _instance;
    }

//    public List<String> getCurrentUserTypes(Entity selectedEntity,OpenGroupsApplication app) {
//	User u = app.getCurrentUser();
//	List<String> userTypes = new ArrayList<String>();
//	userTypes.add(UserTypes.ANY);
//	if (u == null) {
//	    userTypes.add(UserTypes.GUEST);
//	} else {
//	    userTypes.add(UserTypes.MEMBER);
//	    if (selectedEntity != null) {
//		if (u.getUserId() == selectedEntity.getCreatorId()) {
//		    userTypes.add(UserTypes.CREATOR);
//		}
//	    }
//	}
//	return userTypes;
//    }

    public void addUser(User u) {
	activeUsers.put(u.getUserId(), u);
    }
    
    public void removeUser(Long userId) {
	activeUsers.remove(userId);
    }
    
    public String encrypt(String input) {
	return new BigInteger(1,messageDigest.digest(input.getBytes())).toString(16);
    }
    
    
    public static void main(String[] args) {
	System.out.println(UsersManager.getInstance().encrypt("..."));
    }
    
}
