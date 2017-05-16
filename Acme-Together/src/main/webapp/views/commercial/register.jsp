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
	<fieldset>
		<legend><spring:message code="commercial.userAccountInfo"/></legend>
		<acme:textbox code="commercial.username" path="username"/>
		<acme:password code="commercial.password" path="password"/>
		<acme:password code="commercial.passwordConf" path="passwordCheck"/>
	</fieldset>


	<fieldset>
		<legend><spring:message code="commercial.contactInfo"/></legend>
		<acme:textbox code="commercial.name" path="name"/>
		<acme:textbox code="commercial.surname" path="surName"/>
		<acme:textbox code="commercial.myEmail" path="email"/>
		<acme:textbox code="commercial.myPhone" path="phone"/>
	</fieldset>
	
	
	<fieldset>
		<legend><spring:message code="commercial.personalInfo"/></legend>
		<acme:textbox code="commercial.companyName" path="companyName"/>
		<acme:textbox code="commercial.vatNumber" path="vatNumber"/>	
	</fieldset>
	

	<form:checkbox path="termsOfUse"/>
	<spring:message code="commercial.termsOfUse.confirmation"/> 
	<a href="commercial/dataProtection.do">
		<spring:message code="commercial.termsOfUse.link" />
	</a>
	<form:errors cssClass="error" path="termsOfUse" />
	<br>
	<acme:submit name="save" code="commercial.accept"/>			
	<acme:cancel url="welcome/index.do" code="commercial.cancel"/>

	

</form:form>