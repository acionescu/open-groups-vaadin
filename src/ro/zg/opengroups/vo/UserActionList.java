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

import java.util.LinkedHashMap;
import java.util.Map;

import ro.zg.netcell.vaadin.action.ActionContext;
import ro.zg.netcell.vaadin.action.UserActionListHandler;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

public class UserActionList extends UserAction {

	/**
     * 
     */
	private static final long serialVersionUID = -8641960062868676410L;

	// private static Map<String, String> actionTargetsDisplayNames = new
	// LinkedHashMap<String, String>();
	//
	// static {
	// actionTargetsDisplayNames.put("ISSUE", "Issues");
	// actionTargetsDisplayNames.put("ISSUE/LIST", "List issues");
	// actionTargetsDisplayNames.put("COMMENT", "Comments");
	// actionTargetsDisplayNames.put("SOLUTION", "Solutions");
	// actionTargetsDisplayNames.put("COMMENT/LIST", "List comments");
	// actionTargetsDisplayNames.put("SOLUTION/LIST",
	// "List proposed solutions");
	// }

	private Map<String, UserAction> actions = new LinkedHashMap<String, UserAction>();

	public UserActionList() {
		setActionHandler(new UserActionListHandler());
	}

	public UserActionList(String actionPath) {
		this();
		setActionName(actionPath);
		// System.out.println("create UserActionList("+actionPath+")");
	}

	public UserActionList(GenericNameValueList list) {
		setActionHandler(new UserActionListHandler());
		// ListMap<String, Object> actions = new ListMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			GenericNameValueContext row = (GenericNameValueContext) list
					.getValueForIndex(i);
			UserAction ua = new UserAction(row);
			// addAction(ua.getActionTarget(), ua);
			addAction(ua.getActionPath(), ua);
		}
	}

	private void addAction(UserAction ua) {
		actions.put(ua.getActionName() + "/" + ua.getTargetEntityType(), ua);
	}

	public void addAction(String actionTarget, UserAction ua) {
		if (actionTarget == null) {
			addAction(ua);
			return;
		}
		int index = actionTarget.indexOf("/");
		if (index < 0) {
			UserActionList ual = getUserActionList(actionTarget, ua
					.getActionPath());
			ual.addAction(ua);
		} else {
			String actionTargetPrefix = actionTarget.substring(0, index);
			UserActionList ual = getUserActionList(actionTargetPrefix,
					actionTargetPrefix);
			ual.addAction(actionTarget.substring(index + 1), ua);
		}
	}

	private UserActionList getUserActionList(String actionTarget,
			String fullActionTarget) {
		UserActionList ual = (UserActionList) actions.get(actionTarget);
		if (ual == null) {
			ual = new UserActionList(actionTarget);
			ual.setDisplayName(OpenGroupsResources.getMessage("action-list."
					+ fullActionTarget));
			actions.put(actionTarget, ual);
		}
		return ual;
	}

//	@Override
//	public boolean allowRead(List<String> userTypes) {
//		for (UserAction nestedAction : actions.values()) {
//			if (nestedAction.allowRead(userTypes)) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	@Override
	public boolean allowRead(ActionContext ac) {
		for (UserAction nestedAction : actions.values()) {
			if (nestedAction.allowRead(ac)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the actions
	 */
	public Map<String, UserAction> getActions() {
		return actions;
	}

	/**
	 * @param actions
	 *            the actions to set
	 */
	public void setActions(Map<String, UserAction> actions) {
		this.actions = actions;
	}

	public UserAction getActionByName(String actionName) {
		return actions.get(actionName);
	}

}
