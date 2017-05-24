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
	<spring:message code="administrator.usersWhoCreateMoreShoppingGroup" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="usersWhoCreateMoreShoppingGroup" id="row">

	<spring:message code="administrator.actor.name" var="nameActor" />
	<display:column property="userAccount.username" title="${nameActor}"
		sortable="false" />

	<spring:message code="administrator.actor.sg" var="sgActor" />
	<display:column sortable="false">
		<fmt:formatNumber value="${row.myShoppingGroups.size()}" type="number"
			maxFractionDigits="3" minFractionDigits="3" />
	</display:column>



</display:table>

<h3>
	<spring:message code="administrator.usersWhoCreateMinusShoppingGroup" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="usersWhoCreateMinusShoppingGroup" id="row">

	<spring:message code="administrator.actor.name" var="nameActor" />
	<display:column property="userAccount.username" title="${nameActor}"
		sortable="false" />


	<spring:message code="administrator.actor.sg" var="sgActor" />
	<display:column sortable="false">
		<fmt:formatNumber value="${row.myShoppingGroups.size()}" type="number"
			maxFractionDigits="3" minFractionDigits="3" />
	</display:column>



</display:table>

<h3>
	<spring:message code="administrator.shoppingGroupsWithMorePuntuation" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="shoppingGroupsWithMorePuntuation" id="row">

	<spring:message code="administrator.sg.name" var="nameSG" />
	<display:column property="name" title="${nameSG}" sortable="false" />

	<spring:message code="administrator.sg.description" var="descriptionSG" />
	<display:column property="description" title="${descriptionSG}"
		sortable="false" />

	<spring:message code="administrator.sg.puntuation" var="puntuationSG" />
	<display:column property="puntuation" title="${puntuationSG}"
		sortable="false" />

	<spring:message code="administrator.sg.creator" var="creatoSG" />
	<display:column property="creator.userAccount.username"
		title="${creatoSG}" sortable="false" />

</display:table>


<h3>
	<spring:message code="administrator.shoppingGroupsWithLessPuntuation" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="shoppingGroupsWithLessPuntuation" id="row">

	<spring:message code="administrator.sg.name" var="nameSG" />
	<display:column property="name" title="${nameSG}" sortable="false" />

	<spring:message code="administrator.sg.description" var="descriptionSG" />
	<display:column property="description" title="${descriptionSG}"
		sortable="false" />

	<spring:message code="administrator.sg.puntuation" var="puntuationSG" />
	<display:column property="puntuation" title="${puntuationSG}"
		sortable="false" />

	<spring:message code="administrator.sg.creator" var="creatoSG" />
	<display:column property="creator.userAccount.username"
		title="${creatoSG}" sortable="false" />

</display:table>

<h3>
	<spring:message code="administrator.percentShoppingGroupsWithFreePlaces" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="percentShoppingGroupsWithFreePlaces" id="row">

	<spring:message code="administrator.percentShoppingGroupsWithFreePlaces"
		var="percentShoppingGroupsWithFreePlaces" />
	<display:column title="${percentShoppingGroupsWithFreePlaces}" sortable="false">
		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>


</display:table>

<h3>
	<spring:message code="administrator.percentShoppingGroupsWithoutFreePlaces" />
</h3>

<display:table class="displaytag" keepStatus="true"
	name="percentShoppingGroupsWithoutFreePlaces" id="row">

	<spring:message code="administrator.percentShoppingGroupsWithoutFreePlaces"
		var="percentShoppingGroupsWithoutFreePlaces" />
	<display:column title="${percentShoppingGroupsWithoutFreePlaces}" sortable="false">
		<fmt:formatNumber value="${row}" type="number" maxFractionDigits="3"
			minFractionDigits="3" />
	</display:column>


</display:table>









