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


<display:table pagesize="5" class="displaytag" name="products"
	requestURI="${requestURI}" id="u">


	<spring:message code="product.name" var="productName" />
	<display:column property="name" title="${productName}" sortable="true" />

	<spring:message code="product.url" var="productURL" />
	<display:column property="url" title="${productURL}" sortable="true" />

	<spring:message code="product.referenceNumber"
		var="productReferenceNumber" />
	<display:column property="name" title="${productReferenceNumber}"
		sortable="true" />

	<spring:message code="product.price" var="productPrice" />
	<display:column property="price" title="${productPrice}"
		sortable="true" />
		
	<spring:message code="product.user" var="productUser" />
	<display:column property="userProduct.userAccount.username" title="${productUser}"
		sortable="true" />
		
		<spring:message code="product.userAddress" var="productUserAdress" />
	<display:column property="userProduct.address" title="${productUserAdress}"
		sortable="false" />
	
	


</display:table>


	




