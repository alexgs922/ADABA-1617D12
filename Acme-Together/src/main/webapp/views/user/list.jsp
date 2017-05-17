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
	
	
	<spring:message code ="user.confirm.ban" var = "confirmBan"/>
	<spring:message code ="user.confirm.unBan" var = "confirmUnBan"/>
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<jstl:choose>
				<jstl:when test = "${row.banned == false }" >
					<button
						onclick = "confirm('${confirmBan }')
						location.href='user/administrator/banUser.do?userId=${row.id}'">
						<spring:message code="user.banUser" />
					</button>
				</jstl:when>
				<jstl:when test = "${row.banned == true }" >
					<button
						onclick = "confirm('${confirmUnBan }')
						location.href='user/administrator/unBanUser.do?userId=${row.id}'">
						<spring:message code="user.unBanUser" />
					</button>
				</jstl:when>
			</jstl:choose>

		</display:column>
	</security:authorize>


</display:table>