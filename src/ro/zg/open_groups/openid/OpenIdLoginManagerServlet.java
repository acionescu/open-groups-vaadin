/*******************************************************************************
 * Copyright 2012 AdrianIonescu
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
package ro.zg.open_groups.openid;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;

import ro.zg.openid.util.OpenIdConstants;
import ro.zg.openid.util.OpenIdOperations;

public class OpenIdLoginManagerServlet extends HttpServlet {
    private static String providerUrl = /* "https://www.google.com/accounts/o8/site-xrds?hd=qualitance.ro"; */"https://www.google.com/accounts/o8/id";
    private ConsumerManager manager = new ConsumerManager();

    /**
     * 
     */
    private static final long serialVersionUID = -1102875870985702323L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	String contextPath = config.getServletContext().getContextPath();

	System.out.println("init " + this.getClass());
	System.out.println("contextPaht = " + contextPath);
	System.out.println("servlet context name = " + config.getServletContext().getServletContextName());

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	System.out.println("do get " + req.getRequestURL());

	String path = req.getServletPath();
	System.out.println(path);
	System.out.println(req.getContextPath());
	System.out.println(req.getRequestURI());

	String uri = req.getRequestURI();
	String servletPathPrefix = req.getContextPath() + req.getServletPath();
	String operation = null;
	if (uri.length() > servletPathPrefix.length()) {
	    operation = uri.substring(servletPathPrefix.length() + 1);
	}

	System.out.println("operation = " + operation);

	if (OpenIdOperations.LOGIN.equals(operation)) {
	    String consumerListenAddress = getOpenIdVerifyAuthRespUrl(req);
//	    consumerListenAddress.replace("8080", "20000");

	    System.out.println("listen address= " + consumerListenAddress);
	    try {
		authenticate(req, resp, consumerListenAddress);
	    } catch (OpenIDException e) {
		e.printStackTrace();
	    }
	} else if (OpenIdOperations.VERIFY_AUTH_RESPONSE.equals(operation)) {
	    try {
		String email = verifyResponse(req);
//		if (email != null) {
//		    req.setAttribute("email", email);
//		    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/res/content.jsp");
//		    dispatcher.forward(req, resp);
//		} else {
//		    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/");
//		    dispatcher.forward(req, resp);
//		}
		HttpSession session = req.getSession(true);
		session.setAttribute(OpenIdConstants.USER_OPENID, email);
		
		String returnUrl = (String)session.getAttribute(OpenIdConstants.RETURN_URL);
		if(returnUrl != null) {
		    System.out.println("Sending redirect to: "+returnUrl);
		    resp.sendRedirect(returnUrl);
		}
		
	    } catch (OpenIDException e) {
		e.printStackTrace();
	    }
	} else {
	    resp.setStatus(resp.SC_BAD_REQUEST);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	System.out.println("do post");

	super.doPost(req, resp);
    }
    

    private String getOpenIdVerifyAuthRespUrl(HttpServletRequest req) {
	String fullUrl = req.getRequestURL().toString();
	int index = fullUrl.lastIndexOf("/");
	String baseUrl = fullUrl;
	if (index > 0) {
	    baseUrl = fullUrl.substring(0, index);
	}
	return baseUrl + "/" + OpenIdOperations.VERIFY_AUTH_RESPONSE;
    }

    private void authenticate(HttpServletRequest req, HttpServletResponse res, String consumerUrl)
	    throws OpenIDException, IOException, ServletException {
	// perform discovery on the user-supplied identifier
	List discoveries = manager.discover(providerUrl);

	// attempt to associate with the OpenID provider
	// and retrieve one service endpoint for authentication
	DiscoveryInformation discovered = manager.associate(discoveries);

	// store the discovery information in the user's session
	req.getSession().setAttribute("openid-disc", discovered);

	// obtain a AuthRequest message to be sent to the OpenID provider
	AuthRequest authReq = manager.authenticate(discovered, consumerUrl);

	// Attribute Exchange example: fetching the 'email' attribute
	FetchRequest fetch = FetchRequest.createFetchRequest();
	fetch.addAttribute("email",
	// attribute alias
		"http://schema.openid.net/contact/email", // type URI
		true); // required

	// attach the extension to the authentication request
	authReq.addExtension(fetch);

	if (!discovered.isVersion2()) {
	    // Option 1: GET HTTP-redirect to the OpenID Provider endpoint
	    // The only method supported in OpenID 1.x
	    // redirect-URL usually limited ~2048 bytes
	    res.sendRedirect(authReq.getDestinationUrl(true));
	} else {
	    // Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)

	    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/res/formredirection.jsp");
	    req.setAttribute("parameterMap", authReq.getParameterMap());
	    req.setAttribute("destinationUrl", authReq.getDestinationUrl(false));
	    dispatcher.forward(req, res);
	}
    }

    private String verifyResponse(HttpServletRequest httpReq) throws OpenIDException {
	// extract the parameters from the authentication response
	// (which comes in as a HTTP request from the OpenID provider)
	ParameterList response = new ParameterList(httpReq.getParameterMap());

	// retrieve the previously stored discovery information
	DiscoveryInformation discovered = (DiscoveryInformation) httpReq.getSession().getAttribute("openid-disc");

	// extract the receiving URL from the HTTP request
	StringBuffer receivingURL = httpReq.getRequestURL();
	String queryString = httpReq.getQueryString();
	if (queryString != null && queryString.length() > 0)
	    receivingURL.append("?").append(httpReq.getQueryString());

	// verify the response; ConsumerManager needs to be the same
	// (static) instance used to place the authentication request
	VerificationResult verification = manager.verify(receivingURL.toString(), response, discovered);

	// examine the verification result and extract the verified identifier
	Identifier verified = verification.getVerifiedId();
	System.out.println("verified= " + verified);
	String email = null;
	if (verified != null) {
	    AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

	    if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
		FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
		System.out.println("fetched: "+fetchResp.getAttributes());
		List emails = fetchResp.getAttributeValues("email");
		email = (String) emails.get(0);

	    }

	}

	return email;
    }
}
