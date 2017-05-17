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

<display:table pagesize="5" class="displaytag" name="shoppingGroups"
	requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->


	<spring:message code="shoppingGroup.name" var="shoppingGroupName" />
	<display:column property="name" title="${shoppingGroupName}" sortable="true" />
	
	<spring:message code="shoppingGroup.description" var="shoppingGroupDescription" />
	<display:column property="description" title="${shoppingGroupDescription}" sortable="false" />
	
	<spring:message code="shoppingGroup.puntuation" var="shoppingGroupPuntuation" />
	<display:column property="puntuation" title="${shoppingGroupPuntuation}" sortable="true" />
	
	<spring:message code="shoppingGroup.private" var="shoppingGroupPrivate" />
	<display:column property="private_group" title="${shoppingGroupPrivate}" sortable="true" />
	
	<spring:message code="shoppingGroup.lastOrderDate" var="shoppingGroupLastOrderDate" />
	<display:column property="lastOrderDate" title="${shoppingGroupLastOrderDate}" sortable="true" />
	
	<spring:message code="shoppingGroup.freePlaces" var="shoppingGroupFreePlaces" />
	<display:column property="freePlaces" title="${shoppingGroupFreePlaces}" sortable="true" />
	
	


</display:table>	
	









