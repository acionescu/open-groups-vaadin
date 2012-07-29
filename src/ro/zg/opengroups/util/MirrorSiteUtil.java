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
package ro.zg.opengroups.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.netcell.vaadin.action.ActionsManager;
import ro.zg.open_groups.gui.constants.UriFragments;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.open_groups.model.OpenGroupsModel;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.constants.ApplicationConfigParam;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityLink;
import ro.zg.opengroups.vo.EntityList;
import ro.zg.opengroups.vo.EntityState;
import ro.zg.opengroups.vo.OpenGroupsApplicationState;
import ro.zg.opengroups.vo.Tag;
import ro.zg.opengroups.vo.UserAction;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class MirrorSiteUtil {
	private static final Logger logger = MasterLogManager
			.getLogger("MirrorSiteUtil");

	private String baseUrl;
	private OpenGroupsApplicationState appState;
	
	
	

	public MirrorSiteUtil() throws ContextAwareException {
	   appState = new OpenGroupsApplicationState();
	}

	public void updateBaseUrl(String url) {
		if (baseUrl == null) {
			baseUrl = url;
		}
	}

	public void updateBaseUrl(URL url) {
		if (baseUrl == null) {
			String base = url.getProtocol() + "://" + url.getHost();
			if (url.getPort() > 0) {
				base += ":" + url.getPort();
			}
			base += url.getPath();
			baseUrl = base;
		}
	}

	public void generatePage(String fragment, HttpServletResponse res) {
		/* obtain the entity id and path from the url */

		int page = 1;
		long id = 0;
		String path = "";
		if (fragment != null && !"".equals(fragment.trim())) {
			String[] params = fragment.split("/");
			int l = params.length;
			if (l > 0) {
				if (UriFragments.SHOW_ENTITY.equals(params[0])) {
					if (l > 1) {
						id = Long.parseLong(params[1]);
					}
					if (l > 2) {
						page = Integer.parseInt(params[2]);
						if (page < 0) {
							page = 1;
						}
					}
					if (l > 3) {
						for (int i = 3; i < params.length; i++) {
							if (i > 3) {
								path += "/";
							}
							path += params[i];
						}
					} else {
						path = "entity.list.recent.activity";
					}
				} else {
					return;
				}
			}
		} else {
			id = (Long) getAppConfigManager().getApplicationConfigParam(
					ApplicationConfigParam.DEFAULT_ENTITY_ID);
			path = "entity.list.recent.activity";
		}
		try {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = new PrintWriter(new OutputStreamWriter(res
					.getOutputStream(), "UTF8"), true);
			appendPageStart(out);
			generatePageForIdAndPath(out, id, path, page);
			appendPageEnd(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void appendPageStart(PrintWriter out) {
		out
				.append("<!DOCTYPE html>\n"
						+ "<html>\n"
						+ "<head>\n"
						+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />"
						+ "</head>" + "<body>");
	}

	private void appendPageEnd(PrintWriter out) {
		out.append("</body></html>");
	}

	private String getMessage(String key) {
		return OpenGroupsResources.getMessage(key);
	}

	private ApplicationConfigManager getAppConfigManager() {
		return appState.getAppConfigManager();
	}

	private void generatePageForIdAndPath(PrintWriter out, long id,
			String path, int page) {
		Entity e = getEntityForId(id);
		if (e == null) {
			return;
		}
		EntityState state = e.getState();
		state.setCurrentActionsPath(path);
		state.setCurrentPageForAction("/" + path, page);
		state.setOpened(true);
		state.setEntityTypeVisible(true);

		// generateFragmentForEntity(out, e);
		generateEntitySummaryFragment(out, e);
		out.append(OpenGroupsUtil.BR);
		out.append(OpenGroupsUtil.BR);
		getActionFragment(out, e, path);
	}

	private OpenGroupsModel getModel() {
		return appState.getModel();
	}

	private Entity getEntityForId(long id) {
		Entity entity = new Entity(id);
		try {
			getModel().refreshEntity(entity, null);
		} catch (ContextAwareException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}

	private void getActionFragment(PrintWriter out, Entity e, String path) {
		logger.debug("Generate action for path: " + path);
		UserAction ua = ActionsManager.getInstance().getActionByPath(path);
		e.getState().setCurrentTabAction(ua);
		EntityList list = getModel().getChildrenListForEntity(e, ua, null);
		generateListFragment(out, list, e);
	}

	private void generateListFragment(PrintWriter out, EntityList list,
			Entity entity) {
		for (Entity e : list.getItemsList()) {
			generateEntitySummaryFragment(out, e);
			out.append("<br/>");
		}
		generatePageControls(out, entity);
	}

	private void generateEntitySummaryFragment(PrintWriter out, Entity entity) {
		EntityState state = entity.getState();
		EntityLink selectedCause =entity.getSelectedCause();
		
		String complexEntityType = entity.getComplexType();
		List<String> subtyesList = getAppConfigManager()
				.getSubtypesForComplexType(complexEntityType);

		if (state.isEntityTypeVisible()) {
			String entityTypeCaption = "("
					+ getMessage(entity.getComplexType()) + ")";
			out.append(entityTypeCaption);
			out.append(OpenGroupsUtil.NBSP);
		}

		/*
		 * if the entity is opened or it is a leaf entity, and it is not
		 * displayed in the recent activity list
		 */
		if (state.isOpened() || (subtyesList == null && selectedCause== null)) {
			out.append(OpenGroupsUtil.wrapAsH(entity.getTitle(), 2));
		} else if (subtyesList != null) {
			out.append(OpenGroupsUtil.wrapAsA(getUrlForEntity(entity), entity
					.getTitle()));
		} else { /* leaf entity */
			Entity parentEntity = new Entity(selectedCause.getParentId());
			parentEntity.getState().setEntityTypeVisible(true);
			parentEntity.getState().setDesiredActionsPath(
					entity.getComplexType() + "/LIST/entity.list.newest");
			out.append(OpenGroupsUtil.wrapAsA(getUrlForEntity(parentEntity), entity
					.getTitle()));
		}
		/* add tags */
		List<Tag> tags = entity.getTags();
		if (tags != null) {
			if (tags.size() > 0) {
				String tagsList = "";
				for (int i = 0; i < tags.size(); i++) {
					Tag tag = tags.get(i);
					tagsList += tag.getTagName();
					if ((tags.size() - i) > 1) {
						tagsList += ", ";
					}
				}
				String tagsLabelString = getMessage("tags.label") + tagsList;
				out.append(OpenGroupsUtil.BR);
				out.append(tagsLabelString);
			}
		}

		Object contentObj = entity.getContent();
		if (contentObj != null) {
			out.append(OpenGroupsUtil.BR);
			out.append(contentObj.toString());
		}

	}

	private void generatePageControls(PrintWriter out, Entity entity) {
		int totalItems = entity.getState().getCurrentListTotalItemsCount();
		int totalPages = (int) Math.ceil(totalItems
				/ entity.getState().getItemsPerPage());
		int currentPage = entity.getState().getCurrentPageForCurrentAction();
		entity.getState().setDesiredActionsPath(
				entity.getState().getCurrentActionPath().substring(1));
		if (currentPage > 1) {
			int prevPage = currentPage - 1;
			entity.getState().setCurrentPageForCurrentAction(prevPage);
			out.append(OpenGroupsUtil.wrapAsA(getUrlForEntity(entity), "<"));
		}
		if (currentPage < totalPages) {
			int nextPage = currentPage + 1;
			entity.getState().setCurrentPageForCurrentAction(nextPage);
			out.append(OpenGroupsUtil.wrapAsA(getUrlForEntity(entity), ">"));
		}

	}

	private String getUrlForEntity(Entity entity) {
		return baseUrl + "/" + UriFragments.SHOW_ENTITY_FRAGMENT
				+ entity.getId() + "/"
				+ entity.getState().getCurrentPageForCurrentAction()
				+ entity.getState().getDesiredActionsPath();

	}

	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL(
				"http://localhost:8080/open-groups#showEntity/247/1/COMMENT/LIST/entity.list.most_popular");
		String base = url.getProtocol() + "://" + url.getHost();
		if (url.getPort() > 0) {
			base += ":" + url.getPort();
		}
		base += url.getPath();

		System.out.println(base);
	}

}
