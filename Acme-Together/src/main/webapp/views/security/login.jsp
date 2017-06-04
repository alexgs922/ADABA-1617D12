 <%--
 * login.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="j_spring_security_check" modelAttribute="credentials">

	<div class="row">
      <div class="input-field col s12">
        <form:input path="username" id="username" />	
		<form:errors class="error" path="username" />
		<label for="username"><spring:message code="security.username" /></label>
      </div>
    </div>
	
	<div class="row">
      <div class="input-field col s12">
       	<form:password path="password" id="password" />	
		<form:errors class="error" path="password" />
		<label for="password"><spring:message code="security.password" /></label>
      </div>
    </div>
	<jstl:if test="${showError == true}">
		<div class="error">
			<spring:message code="security.login.failed" />
		</div>
	</jstl:if>
	
	<button class="btn waves-effect waves-light" type="submit" name="action"><spring:message code="security.login" /><i class="material-icons right">send</i>
	</button>
	
</form:form>