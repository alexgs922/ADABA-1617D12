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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="coupon/commercial/edit.do" modelAttribute="coupon">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<br>
	<div class="row">
		<h4>
			<spring:message code="coupon.info" />
		</h4>
		
		<acme:textbox code="coupon.couponNumber" path="couponNumber" />
		<br>
		<acme:number code="coupon.discount" path="discount" step="0.01" min="0.0" max="1.0"/>
		<br>
		
	</div>

	<acme:submit name="save" code="coupon.save" />
	<a href="coupon/commercial/list.do" class="waves-effect waves-light btn"><spring:message code="coupon.cancel" /></a>

</form:form>