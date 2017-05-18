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


<display:table pagesize="5" class="displaytag" name="shoppingGroups" requestURI="${requestURI}" id="sh">
	
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
	<display:column property="description" title="${shDescription}" sortable="false" />
	
	<spring:message code="sh.freePlaces" var="shFreePlaces" />
	<display:column property="freePlaces" title="${shFreePlaces}" sortable="true" />
	
	<spring:message code="sh.frecuentedSites" var="shFrecuentedSites" />
	<display:column title="${shFrecuentedSites}">
	
	<spring:message code="sh.puntuation" var="shPuntuation" />
	<display:column property="puntuation" title="${shPuntuation}" sortable="true" />
	
		<jstl:choose>
		
			<jstl:when test="${empty(sh.frecuentedSites)}">
				<spring:message code="sh.emptyFc" var="shEmptyFs" />
				<jstl:out value="${shEmptyFs}"></jstl:out>
			
			</jstl:when>

			<jstl:when test="${fn:contains(sh.frecuentedSites, ',')}">
				<jstl:set var="attachparts"
					value="${fn:split(sh.frecuentedSites, ',')}" />


				<jstl:forEach var="i" begin="0" end="${fn:length(attachparts)}">
					<a href="${attachparts[i]}"> <jstl:out
							value="${attachparts[i]}"></jstl:out></a>
						&nbsp; &nbsp;
			</jstl:forEach>

			</jstl:when>
			
			<jstl:otherwise>
				<a href="${sh.frecuentedSites}"> <jstl:out
						value="${sh.frecuentedSites}  "></jstl:out></a>
			</jstl:otherwise>

		</jstl:choose>

	</display:column>
	
	
	
	
	


</display:table>