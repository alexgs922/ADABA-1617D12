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

	<fieldset>
		<legend>
			<spring:message code="coupon.info" />
		</legend>
		
		<acme:textbox code="coupon.couponNumber" path="couponNumber" />
		<br>
		<acme:textbox code="coupon.discount" path="discount" />
		<br>
		
	
	</fieldset>
	<br><br>


	<acme:submit name="save" code="coupon.save" />

	<acme:cancel url="coupon/commercial/list.do" code="coupon.cancel" />

</form:form>