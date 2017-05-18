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

<spring:message code="distributor.personalInfo" var="distributorPersonalInfo" />
<h2>
	<jstl:out value="${distributorPersonalInfo}" />
</h2>
<display:table pagesize="5" class="displaytag" name="distributor"
	requestURI="${requestURI}" id="row">

	<spring:message code="distributor.name" var="name" />
	<display:column property="name" title="${name}" sortable="false" />

	<spring:message code="distributor.surname" var="surName" />
	<display:column property="surName" title="${surName}"
		sortable="false" />
		
	<spring:message code="distributor.email" var="email" />
	<display:column property="email" title="${email}"
		sortable="false" />	

	<spring:message code="distributor.phone" var="phone" />
	<display:column property="phone" title="${phone}"
		sortable="false" />	
</display:table>


<security:authorize access="hasRole('DISTRIBUTOR')">
		<jstl:choose>

			<jstl:when test="${principal.id == row.id }">
				<a href="distributor/editProfile.do?distributorId=${row.id}"> <spring:message
						code="distributor.editprofile" />
				</a>

			</jstl:when>
</jstl:choose>
</security:authorize>
