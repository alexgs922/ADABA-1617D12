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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<display:table pagesize="5" class="displaytag" name="users"
	requestURI="${requestURI}" id="row">
	
	
	<spring:message code="user.picture" var="userPicture" />
	<display:column title="${userPicture}" sortable="false">
		<img src="${row.picture}" width="200" height="100" />
	</display:column>

	<spring:message code="user.name" var="userName" />
	<display:column property="name" title="${userName}" sortable="false" />
	
	<spring:message code="user.surname" var="userSurname" />
	<display:column property="surName" title="${userSurname}" sortable="false" />
	
	<spring:message code="user.identification" var="userIdentification" />
	<display:column property="identification" title="${userIdentification}" sortable="false" />
	
	<spring:message code="user.email" var="userEmail" />
	<display:column property="email" title="${userEmail}" sortable="false" />
	
	<security:authorize access="isAuthenticated()">
		<display:column>
			<a href="user/profile.do?userId=${row.id}"> <spring:message
					code="user.profile" />
			</a>

		</display:column>
	</security:authorize>
	
	
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:choose>
				<jstl:when test = "${principal.friends.contains(row)}" >
					<a href="user/unfollow.do?userId=${row.id}"> <spring:message
					code="user.unfollow" />
			</a>
				</jstl:when>
				<jstl:when test = "${!principal.friends.contains(row)}" >
					<a href="user/follow.do?userId=${row.id}"> <spring:message
					code="user.follow" />
			</a>
				</jstl:when>
			</jstl:choose>

		</display:column>
	</security:authorize>


</display:table>