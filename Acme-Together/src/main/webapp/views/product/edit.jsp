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

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="shoppingGroup/user/editProduct.do?shoppingGroupId=${shoppingGroup.id }"
	modelAttribute="product">
	
	
	<form:hidden path="id"/>
	<form:hidden path="version"/> 
	
	<br>
	<div class="row">
	<acme:textbox code="product.name" path="name" />
	<br>
	<acme:textbox code="product.url" path="url" />
	<br>
	<acme:textbox code="product.referenceNumber" path="referenceNumber" />
	<br>
	<acme:textbox code="product.price" path="price" />
	<br>
	</div>

	<acme:submit name="save" code="product.save" />&nbsp; 
	<a href="shoppingGroup/user/display.do?shoppingGroupId=${shoppingGroup.id}" class="waves-effect waves-light btn"><spring:message code="product.cancel" /></a>
	<br />

</form:form>





