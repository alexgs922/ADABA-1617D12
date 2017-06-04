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

<form:form action="${requestURI}" modelAttribute="joinToForm">
	
	<div class="row">
	
	<b><spring:message code="sh.joinTo.mess"/></b> &nbsp;&nbsp; <jstl:out value="${shToJoinName}"></jstl:out>
	<br>
	<br>
	<input id="termsOfUse" name="termsOfUse" class="filled-in" type="checkbox" value="true">
	<label for="termsOfUse">
	<spring:message code="sh.termsOfUse.confirmation"/> 
	<a href="user/dataProtection.do">
		<spring:message code="sh.termsOfUse.link" />
	</a>
	</label>
	<form:errors cssClass="error" path="termsOfUse" />
	<br>
	<br>
	</div>
	<acme:submit name="save" code="sh.accept"/>			
	<a href="shoppingGroup/user/joinedShoppingGroups.do" class="waves-effect waves-light btn"><spring:message code="sh.cancel" /></a>
	<br>
	<br>
	
</form:form>