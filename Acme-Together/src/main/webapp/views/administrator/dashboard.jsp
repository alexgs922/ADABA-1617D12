<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<h3>
	<spring:message code="administrator.numberOfUserRegistered" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="numberOfUserRegistered" id="row">

	<spring:message code="administrator.numberOfUserRegistered"
		var="numberOfUserRegistered" />
	<display:column title="${numberOfUserRegistered}" sortable="false">
		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>


</display:table>

<h3>
	<spring:message code="administrator.numberOfOrderLastMonth" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="numberOfOrderLastMonth" id="row">

	<spring:message code="administrator.numberOfOrderLastMonth"
		var="numberOfOrderLastMonth" />
	<display:column title="${numberOfOrderLastMonth}" sortable="false">
		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>


</display:table>


<h3>
	<spring:message
		code="administrator.usersWhoCreateMoreShoppingGroup" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="usersWhoCreateMoreShoppingGroup" id="row">

	<spring:message code="administrator.actor.name" var="nameActor" />
	<display:column property="userAccount.username" title="${nameActor}" sortable="false" />

	<spring:message code="administrator.actor.sg" var="sgActor" />
	<display:column sortable="false">
		<fmt:formatNumber value="${row.myShoppingGroups.size()}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>
	


</display:table>

<h3>
	<spring:message
		code="administrator.usersWhoCreateMinusShoppingGroup" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="usersWhoCreateMinusShoppingGroup" id="row">

	<spring:message code="administrator.actor.name" var="nameActor" />
	<display:column property="userAccount.username" title="${nameActor}" sortable="false" />
	
	
	<spring:message code="administrator.actor.sg" var="sgActor" />
	<display:column sortable="false">
		<fmt:formatNumber value="${row.myShoppingGroups.size()}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>



</display:table>










