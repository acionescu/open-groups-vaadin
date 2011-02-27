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
package ro.zg.open_groups;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.gui.constants.UriFragments;
import ro.zg.opengroups.constants.ActionLocations;
import ro.zg.opengroups.vo.ActionUri;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.opengroups.vo.UserActionList;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.URIHandler;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;
import com.vaadin.ui.Window.Notification;

public class OpenGroupsUriHandler implements URIHandler, FragmentChangedListener {

    /**
     * 
     */
    private static final long serialVersionUID = -2654694055928915479L;

    private OpenGroupsApplication app;
    private Map<String, FragmentHandler> fragmentHandlers = new HashMap<String, FragmentHandler>();
    private static final Logger logger = MasterLogManager.getLogger("URI_HANDLER");

    public OpenGroupsUriHandler(OpenGroupsApplication app) {
	this.app = app;
	initFragmentHandlers();
    }

    private void initFragmentHandlers() {
	// fragmentHandlers.put("", new FragmentHandler() {
	//
	// @Override
	// public void handleFragment(String[] params) {
	// /* 1 is the id of the entity */
	// app.getTemporaryTab(app.getRootEntity()).setRefreshOn(true);
	// app.openTemporaryTab(app.getRootEntity());
	//		
	// }
	// });

	fragmentHandlers.put(UriFragments.SHOW_ENTITY, new FragmentHandler() {

	    @Override
	    public void handleFragment(String[] params) {
		int currentPage = 1;
		String actionPath = "";
		int pl = params.length;
		long id = app.getRootEntity().getId();
		/* 1 is the id of the entity */
		/* 2 is the current page */
		/* >= 3 represent the action path */
		if (pl > 1) {
		    id = Long.parseLong(params[1]);
		}
		if (pl > 2) {
		    currentPage = Integer.parseInt(params[2]);
		}
		if (pl > 3) {
		    for (int i = 3; i < params.length; i++) {
			if (i > 3) {
			    actionPath += "/";
			}
			actionPath += params[i];
		    }
		}
		app.openEntityById(id, actionPath, currentPage);
	    }
	});
    }

    @Override
    public DownloadStream handleURI(URL context, String relativeUri) {
	System.out.println("handle uri " + relativeUri);
	System.out.println("full url: " + app.getURL());
	ActionUri au = getActionUri(relativeUri);
	UserActionList actionsList = ActionsManager.getInstance().getGlobalActions(ActionLocations.URI);
	if (actionsList == null) {
	    app.showNotification("generic.error", Notification.TYPE_ERROR_MESSAGE);
	    return null;
	}
	UserAction ua = actionsList.getActionByName(au.getHandlerUri() + "/#");
	if (ua != null) {
	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("actionUri", au);
	    ua.executeHandler(app, params);
	} else {

	    if ("".equals(relativeUri.trim())) {
		 app.fullyRefreshCurrentSelectedEntity();
	    } else {
		/* assume this is a page refresh */
		handleFragment(relativeUri);
	    }
	}
	return null;
    }

    private ActionUri getActionUri(String relativeUri) {
	ActionUri au = new ActionUri();
	int index = relativeUri.indexOf("/");
	if (index < 0) {
	    au.setHandlerUri(relativeUri);
	} else {
	    au.setHandlerUri(relativeUri.substring(0, index));
	    au.setParamUri(relativeUri.substring(index + 1));
	}

	return au;
    }

    @Override
    public void fragmentChanged(FragmentChangedEvent source) {

	UriFragmentUtility ufu = source.getUriFragmentUtility();
	String fragment = ufu.getFragment();
	handleFragment(fragment);

    }

    private void handleFragment(String fragment) {
	long currentTime = System.currentTimeMillis();
	long lastTime = app.getLastFragmentUpdate();
	long dif = (currentTime - lastTime);
	System.out.println("handle fragment " + fragment + " at " + currentTime + " dif " + dif);
	// if( dif < 2000) {
	// return;
	// }
	app.setLastFragmentUpdate(currentTime);
	app.setLastFragment(fragment);

	FragmentHandler fh = null;
	String[] params = new String[0];
	if (fragment != null && !"".equals(fragment.trim())) {
	    params = fragment.split("/");
	    if (params.length > 0) {
		fh = fragmentHandlers.get(params[0]);
	    }
	} else {
	    fh = fragmentHandlers.get("");
	}
	if (fh != null) {
	    fh.handleFragment(params);
	} else {
	    // ufu.setFragment(UriFragments.SHOW_ENTITY_FRAGMENT+app.getSelectedEntity().getId(),false);
	    app.setFragmentToCurrentEntity();
	    logger.error("No fragment handler found for fragment '" + fragment + "'");
	}
    }
}

interface FragmentHandler {
    /**
     * first param is always the name of the command
     * 
     * @param params
     */
    void handleFragment(String[] params);
}