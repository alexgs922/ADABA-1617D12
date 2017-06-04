<%--

 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
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

<!-- Listing grid -->

<display:table pagesize="5" class="highlight" name="distributors"
	requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->

	<spring:message code="distributor.companyName" var="distributorCompanyName" />
	<display:column property="companyName" title="${distributorCompanyName}" sortable="true" />

	<spring:message code="distributor.webPage" var="distributorWebPage" />
	<display:column property="webPage" title="${distributorWebPage}"
		sortable="false" />
	
	<security:authorize access="isAuthenticated()">
	<display:column>
		<a href="distributor/profile.do?distributorId=${row.id}">
					<spring:message code="distributor.viewProfile" />
				</a>
	</display:column>
	</security:authorize>
	
</display:table>