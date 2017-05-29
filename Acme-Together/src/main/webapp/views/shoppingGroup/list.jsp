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

<jstl:if test="${not empty myShoppingGroups}">
<spring:message code="sh.MySh" var="mySh"/>
<h2><jstl:out value="${mySh}"/></h2>
<br>
<display:table pagesize="5" sort="list" class="displaytag" name="myShoppingGroups"
	requestURI="${requestURI}" id="sh">

	<jstl:if test="${sh.private_group eq true}">
		<spring:message code="sh.privateGroup" var="shPrivate" />
		<display:column title="${shPrivate}" sortable="true">
			<spring:message code="sh.privateGroupTrue" var="shPrivateTrue" />
			<jstl:out value="${shPrivateTrue}"></jstl:out>
		</display:column>

	</jstl:if>

	<jstl:if test="${sh.private_group eq false}">
		<spring:message code="sh.privateGroup" var="shPrivate" />
		<display:column title="${shPrivate}" sortable="true">
			<spring:message code="sh.privateGroupFalse" var="shPrivateFalse" />
			<jstl:out value="${shPrivateFalse}"></jstl:out>
		</display:column>

	</jstl:if>


	<spring:message code="sh.name" var="shName" />
	<display:column property="name" title="${shName}" sortable="false" />

	<spring:message code="sh.description" var="shDescription" />
	<display:column property="description" title="${shDescription}"
		sortable="false" />

	<spring:message code="sh.freePlaces" var="shFreePlaces" />
	<display:column property="freePlaces" title="${shFreePlaces}"
		sortable="true" />

	<spring:message code="sh.site" var="shSite" />
	<display:column property="site" title="${shSite}" sortable="false" />




	<display:column>
		<a href="shoppingGroup/user/display.do?shoppingGroupId=${sh.id}">
			<spring:message code="sh.display" />
		</a>

	</display:column>
	
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:choose>
			<jstl:when test="${sh.creator.id == principal.id and sh.lastOrderDate == null}">
				<a href="shoppingGroup/user/edit.do?shoppingGroupId=${sh.id}">
					<spring:message code="sh.edit" />
				</a>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="sh.noteditableInList"/>
			</jstl:otherwise>
			</jstl:choose>

		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:choose>
			<jstl:when test="${sh.creator.id == principal.id and sh.lastOrderDate eq null}">
				<a href="shoppingGroup/user/delete.do?shoppingGroupId=${sh.id}">
					<spring:message code="sh.delete" />
				</a>
			</jstl:when>
			<jstl:when test ="${sh.creator.id == principal.id and sh.lastOrderDate ne null}">
				<spring:message code="sh.notdeletableInList"/>
			</jstl:when>
			</jstl:choose>

		</display:column>
	</security:authorize>
	
</display:table>

</jstl:if>

<br>

<jstl:if test="${ not empty shoppingGroupsBelongs}">
<spring:message code="sh.ShIBelong" var="shIBelong"/>
<h2><jstl:out value="${shIBelong}"/></h2>
<br>
<display:table pagesize="5" sort="list" class="displaytag" name="shoppingGroupsBelongs"
	requestURI="${requestURI}" id="row">

	<jstl:if test="${row.private_group eq true}">
		<spring:message code="sh.privateGroup" var="shPrivate" />
		<display:column title="${shPrivate}" sortable="true">
			<spring:message code="sh.privateGroupTrue" var="shPrivateTrue" />
			<jstl:out value="${shPrivateTrue}"></jstl:out>
		</display:column>

	</jstl:if>

	<jstl:if test="${row.private_group eq false}">
		<spring:message code="sh.privateGroup" var="shPrivate" />
		<display:column title="${shPrivate}" sortable="true">
			<spring:message code="sh.privateGroupFalse" var="shPrivateFalse" />
			<jstl:out value="${shPrivateFalse}"></jstl:out>
		</display:column>

	</jstl:if>


	<spring:message code="sh.name" var="shName" />
	<display:column property="name" title="${shName}" sortable="false" />

	<spring:message code="sh.description" var="shDescription" />
	<display:column property="description" title="${shDescription}"
		sortable="false" />

	<spring:message code="sh.freePlaces" var="shFreePlaces" />
	<display:column property="freePlaces" title="${shFreePlaces}"
		sortable="true" />

	<spring:message code="sh.site" var="shSite" />
	<display:column property="site" title="${shSite}" sortable="false" />

	<display:column>
		<a href="shoppingGroup/user/display.do?shoppingGroupId=${row.id}">
			<spring:message code="sh.display" />
		</a>

	</display:column>
	
	<security:authorize access="hasRole('USER')">
		<display:column>
			
			<jstl:if test="${row.creator.id != principal.id and row.lastOrderDate eq null}">
				
				<spring:message code="shoppingGroup.confirm.leave" var="confirmLeave" />
				<button onclick="if(confirm('${confirmLeave}')) location.href='shoppingGroup/user/leave.do?shoppingGroupId=${row.id}'">
				<spring:message code="sh.leave" />
				</button>
			</jstl:if>
			

		</display:column>
	</security:authorize>
	
</display:table>

</jstl:if>
<br>

<security:authorize access="hasRole('USER')">

	<button
		onclick="location.href='shoppingGroup/user/create.do?'">
		<spring:message code="shoppingGroup.create" />
	</button>

</security:authorize>

<security:authorize access="hasRole('USER')">

	<button
		onclick="location.href='shoppingGroup/user/createPrivate.do?'">
		<spring:message code="shoppingGroup.createPrivate" />
	</button>

</security:authorize>








