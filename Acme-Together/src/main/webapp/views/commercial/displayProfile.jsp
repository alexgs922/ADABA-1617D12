<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="commercial.personalInfo" var="commercialPersonalInfo" />

<h4>
	<jstl:out value="${commercialPersonalInfo}" />
</h4>
<display:table pagesize="5" class="highlight" name="commercial"
	requestURI="${requestURI}" id="row">

	<spring:message code="commercial.name" var="name" />
	<display:column property="name" title="${name}" sortable="false" />

	<spring:message code="commercial.surname" var="surName" />
	<display:column property="surName" title="${surName}"
		sortable="false" />
		
	<spring:message code="commercial.email" var="email" />
	<display:column property="email" title="${email}"
		sortable="false" />	

	<spring:message code="commercial.phone" var="phone" />
	<display:column property="phone" title="${phone}"
		sortable="false" />	
</display:table>

<security:authorize access="hasRole('COMMERCIAL')">
<br>
	<jstl:choose>

			<jstl:when test="${principal.id == row.id }">
				<a class="waves-effect waves-light btn" href="commercial/editProfile.do?commercialId=${row.id}"> <spring:message
						code="commercial.editprofile" />
				</a>

			</jstl:when>
</jstl:choose>
</security:authorize>