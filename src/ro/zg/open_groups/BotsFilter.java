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

import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ro.zg.opengroups.util.MirrorSiteUtil;

public class BotsFilter implements Filter {
    private MirrorSiteUtil mirrorSite = new MirrorSiteUtil();

    @Override
    public void destroy() {
	// TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException,
	    ServletException {

	HttpServletRequest req = (HttpServletRequest) arg0;
	HttpServletResponse res = (HttpServletResponse) arg1;
	String userAgent = req.getHeader("User-Agent");
	
	boolean isBot = userAgent.contains("Googlebot") || userAgent.contains("Yahoo") || userAgent.contains("WordPress") || userAgent.toLowerCase().contains("bot");

	if (isBot) {
	    System.out.println("Serving bot: "+userAgent);
	    System.out.println("url: "+req.getRequestURL());
	    URL url = new URL(req.getRequestURL().toString());
	    String contextPath = req.getContextPath();
	    String base = url.getProtocol() + "://" + url.getHost();
	    if (url.getPort() > 0) {
		base += ":" + url.getPort();
	    }
	    base += contextPath;
	    mirrorSite.updateBaseUrl(base);
	    
	    String uri = req.getRequestURI();
	    String fragment="";
	    if(uri.length() > contextPath.length() ) {
		fragment = uri.substring(contextPath.length()+1);
	    }
	    mirrorSite.generatePage(fragment, res);
	    return;
	}
	arg2.doFilter(arg0, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
	// TODO Auto-generated method stub

    }

}
