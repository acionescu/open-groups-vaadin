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

public class HtmlHelper {
	public static final String NBSP = "&nbsp;";
	public static final String BR = "<br/>";

	public static String wrapAsH(String s, int type) {
		return "<h" + type + ">" + s + "</h" + type + ">";
	}

	public static String wrapAsA(String url, String text) {
		return "<a href=\"" + url + "\">" + text + "</a>";
	}
	
	public static String wrapAsA(String url, String text, String target) {
		return "<a href=\"" + url + "\" target=\""+target+"\">" + text + "</a>";
	}
}
