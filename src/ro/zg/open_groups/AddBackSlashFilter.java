package ro.zg.open_groups;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddBackSlashFilter implements Filter {

    @Override
    public void destroy() {
	// TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException,
	    ServletException {
	HttpServletRequest req = (HttpServletRequest) arg0;
	HttpServletResponse res = (HttpServletResponse) arg1;

	String contextPath = req.getContextPath();
	String requestUri = req.getRequestURI();
	// System.out.println("context path=" + contextPath);
	// System.out.println("req uri=" + requestUri);
	String uri = requestUri.substring(contextPath.length());
	// System.out.println("real uri="+uri);

//	if (!isVaadinStuff(uri)) {
//	    System.out.println("send redirect");
//	    if (uri.length() > 1) {
//		res.sendRedirect(req.getRequestURL() + "/#" + uri);
//	    }
//
//	    return;
//	}
	arg2.doFilter(arg0, arg1);
    }

    private boolean isVaadinStuff(String uri) {
	return uri.contains("VAADIN") || uri.contains("UIDL");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
	// TODO Auto-generated method stub

    }

}
