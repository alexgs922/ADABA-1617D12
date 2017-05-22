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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<display:table pagesize="5" class="displaytag" name="shoppingGroups"
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

	<spring:message code="shoppingGroup.confirm.join" var="confirmJoin" />
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:if test="${row.creator.id == principal.id }">
				<button
					onclick="if(confirm('${confirmJoin }'))
						location.href='shoppingGroup/user/join.do?shoppingGroupId=${row.id}'">
					<spring:message code="shoppingGroup.join" />
				</button>
			</jstl:if>

		</display:column>
	</security:authorize>


</display:table>

<br>

<security:authorize access="hasRole('USER')">

	<button
		onclick="location.href='shoppingGroup/user/create.do?'">
		<spring:message code="shoppingGroup.create" />
	</button>

</security:authorize>




