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

<display:table pagesize="5" class="displaytag" name="orders" id="row">

	<!-- Attributes -->

	<spring:message code="order.initDate" var="initDate" />
	<display:column property="initDate" title="${initDate}"
		sortable="false" />

	<spring:message code="order.finishDate" var="finishDate" />
	<display:column property="finishDate" title="${finishDate}"
		sortable="false" />

	<spring:message code="order.status" var="status" />
	<display:column property="status" title="${status}" sortable="false" />

	<spring:message code="order.totalPrice" var="totalPrice" />
	<display:column property="totalPrice" title="${totalPrice}"
		sortable="false" />

	<security:authorize access="hasRole('DISTRIBUTOR')">
		<display:column>

			<a href="warehouse/displayOrder.do?orderId=${row.id}"> <spring:message
					code="order.products" />
			</a>
		</display:column>
	</security:authorize>


	<jstl:choose>

		<jstl:when test="${row.status == 'INPROCESS'}">
			<display:column>

				<a href="order/changeStatus.do?orderId=${row.id}"> <spring:message
						code="order.changeStatus" />
				</a>
			</display:column>
		</jstl:when>

		<jstl:when test="${row.status == 'RECEIVED'}">

			<!-- HAY QUE HACER EL ELIMINAR ORDER -->
			<display:column>
				<a href="order/remove.do?orderId=${row.id}"> <spring:message
						code="order.removeOrder" />
				</a>
			</display:column>
		</jstl:when>

	</jstl:choose>

</display:table>







