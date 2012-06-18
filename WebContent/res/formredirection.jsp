<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- <%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %> --%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>OpenID HTML FORM Redirection</title>

</head>
<body onload="document.forms['openid-form-redirection'].submit();">
	<%
	    System.out
						.println("params=" + request.getAttribute("parameterMap"));
				System.out.println("destinationUrl="
						+ request.getAttribute("destinationUrl"));
	%>
	<form name="openid-form-redirection" action="${destinationUrl}"
		method="post" accept-charset="utf-8">
		<c:forEach var="parameter" items="${parameterMap}">
			<input type="hidden" name="${parameter.key}"
				value="${parameter.value}" />
		</c:forEach>
		<button type="submit">Continue...</button>
	</form>

	<script>
		function send_data() {
			document.forms[0].submit();

		}

		window.onload = function() {
			send_data();
		}
	</script>
</body>
</html>