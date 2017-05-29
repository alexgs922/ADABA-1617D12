<%--
 * forbiddenOperation.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="${requestURI}" modelAttribute="couponsforOrderform">
	
	<fieldset>
	
	<spring:message code="order.select.coupon" var = "shSelectCoupon"/><b><jstl:out value="${shSelectCoupon}"></jstl:out></b>
	<br>
	<br>

	<form:radiobuttons path="coupon" items="${cupones}"  itemLabel="couponNumber"/>
	<form:errors cssClass="error" path="coupon" />
	<br>
	<br>
	
	<acme:selectSH items="${distributors}" itemLabel="name" code="order.distributors" path="distributor"/>
	
	<br>
	<br>
	
	</fieldset>
	<br>
	<br>
	
	<acme:submit name="save" code="sh.accept"/>			
	<acme:cancel url="shoppingGroup/user/display.do?shoppingGroupId=${shId}" code="sh.cancel"/>
	<br>
	<br>
	
</form:form>