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

<display:table pagesize="5" class="displaytag" name="orders"
	requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->


	<spring:message code="order.initDate" var="orderInitDate" />
	<display:column property="initDate" title="${orderInitDate}" sortable="true" />

	<spring:message code="order.finishDate" var="orderFinishDate" />
	<display:column property="finishDate" title="${orderFinishDate}"
		sortable="false" />
		
	<spring:message code="order.status" var="orderStatus" />
	<display:column property="status" title="${orderFinishDate}"
		sortable="false" />	
		
		
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:if test="${row.status == 'SENT' }">
			<button onclick = "location.href='order/user/markAsAReceived.do?orderId=${row.id}'"> <spring:message
					code="order.markAsAReceived" />
			</button>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
		<display:column>		
			<a href="engagement/see.do?orderId=${row.id}"> <spring:message
				code="order.engagement" />
			</a>
		</display:column>
	</security:authorize>
	
		
		


</display:table>	
	











