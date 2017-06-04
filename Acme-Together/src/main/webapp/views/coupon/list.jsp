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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table pagesize="5"  sort="list" class="highlight" name="coupons" requestURI="${requestURI}" id="row">
	
	<spring:message code="coupon.couponNumber" var="couponNumber" />
	<display:column property="couponNumber" title="${couponNumber}" sortable="true" class="text-normal"/>

	<spring:message code="coupon.discount" var="couponDiscount" />
	<display:column property="discount" title="${couponDiscount}" sortable="true" class="text-normal"/>
	
	<security:authorize access="hasRole('COMMERCIAL')">
		<display:column>
			<button class="waves-effect waves-light btn" onclick="location.href='coupon/commercial/edit.do?couponId=${row.id}'"> <spring:message code="coupon.edit" /></button>
			<jstl:if test="${row.orders == null || empty row.orders }">
				<acme:confirmDelete url="coupon/commercial/delete.do?couponId=${row.id}" code="coupon.delete" codeConfirm="coupon.confirm.delete" />
			</jstl:if>
		</display:column>
	</security:authorize>
	
</display:table>
<br>

<security:authorize access="hasRole('COMMERCIAL')">
	<div>
		<button class="waves-effect waves-light btn" onclick="location.href='coupon/commercial/create.do'"> 
			<spring:message code="coupon.create" />
		</button>
	</div>
</security:authorize>