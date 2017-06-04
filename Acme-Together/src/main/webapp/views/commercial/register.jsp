<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>



<form:form action="commercial/register.do" modelAttribute="commercial">

	<br>
	<div class="row">
		<h4><spring:message code="commercial.userAccountInfo"/></h4>
		<acme:textbox code="commercial.username" path="username"/>
		<acme:password code="commercial.password" path="password"/>
		<acme:password code="commercial.passwordConf" path="passwordCheck"/>
	</div>


	<div class="row">
		<h4><spring:message code="commercial.contactInfo"/></h4>
		<acme:textbox code="commercial.name" path="name"/>
		<acme:textbox code="commercial.surname" path="surName"/>
		<acme:textbox code="commercial.myEmail" path="email"/>
		<acme:textbox code="commercial.myPhone" path="phone"/>
	</div>
	
	
	<div class="row">
		<h4><spring:message code="commercial.personalInfo"/></h4>
		<acme:textbox code="commercial.companyName" path="companyName"/>
		<acme:textbox code="commercial.vatNumber" path="vatNumber"/>	
	</div>
	

	<input id="termsOfUse" name="termsOfUse" class="filled-in" type="checkbox" value="true">
	<label for="termsOfUse">
	<spring:message code="commercial.termsOfUse.confirmation"/> 
	<a href="commercial/dataProtection.do">
		<spring:message code="commercial.termsOfUse.link" />
	</a>
	</label>
	<form:errors cssClass="error" path="termsOfUse" />
	<br>
	<br>
	<acme:submit name="save" code="commercial.accept"/>			
	<a href="welcome/index.do" class="waves-effect waves-light btn"><spring:message code="commercial.cancel" /></a>
	<br>
	<br>

	

</form:form>