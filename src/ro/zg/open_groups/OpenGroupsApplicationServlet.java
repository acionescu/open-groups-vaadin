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

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

public class OpenGroupsApplicationServlet extends ApplicationServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2024202391223576656L;

    protected void writeAjaxPageHtmlHeader(final BufferedWriter page, String title, String themeUri, HttpServletRequest req) throws IOException {
	super.writeAjaxPageHtmlHeader(page, title, themeUri,req);
	page
		.write("<script type=\"text/javascript\">\n"
			+ "\n"
			+ "  var _gaq = _gaq || [];\n"
			+ "  _gaq.push(['_setAccount', 'UA-18344562-1']);\n"
			+ "  _gaq.push(['_trackPageview']);\n"
			+ "\n"
			+ "  (function() {\n"
			+ "    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n"
			+ "    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n"
			+ "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n"
			+ "  })();\n" + "\n" + "</script>");
    }

}
