<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<br>
<spring:message code="eng.Info" var="engInfo" />

<h4>
	<jstl:out value="${engInfo}"></jstl:out>
</h4>
<display:table pagesize="15" class="highlight" name="lista" requestURI="${requestURI}" id="fila">

	
	<%-- <jstl:forEach var = "i" begin = "0" end = "${fn:length(lista)}"> --%>
		 <%-- <display:column>  --%>
			<jstl:out value = "${fila}"/>
		<%-- </display:column>  --%>
         
      <%-- </jstl:forEach> --%>
	
</display:table>
