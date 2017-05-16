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



<form:form action="distributor/register.do" modelAttribute="distributor">
	<fieldset>
		<legend><spring:message code="distributor.userAccountInfo"/></legend>
		<acme:textbox code="distributor.username" path="username"/>
		<acme:password code="distributor.password" path="password"/>
		<acme:password code="distributor.passwordConf" path="passwordCheck"/>
	</fieldset>


	<fieldset>
		<legend><spring:message code="distributor.contactInfo"/></legend>
		<acme:textbox code="distributor.name" path="name"/>
		<acme:textbox code="distributor.surname" path="surName"/>
		<acme:textbox code="distributor.myEmail" path="email"/>
		<acme:textbox code="distributor.myPhone" path="phone"/>
	</fieldset>
	
	
	<fieldset>
		<legend><spring:message code="distributor.personalInfo"/></legend>
		<acme:textbox code="distributor.companyName" path="companyName"/>
		<acme:textbox code="distributor.companyAddress" path="companyAddress"/>
		<acme:textbox code="distributor.vatNumber" path="vatNumber"/>
		<acme:textbox code="distributor.webPage" path="webPage"/>
			
	</fieldset>
	

	<form:checkbox path="termsOfUse"/>
	<spring:message code="distributor.termsOfUse.confirmation"/> 
	<a href="distributor/dataProtection.do">
		<spring:message code="distributor.termsOfUse.link" />
	</a>
	<form:errors cssClass="error" path="termsOfUse" />
	<br>
	<acme:submit name="save" code="distributor.accept"/>			
	<acme:cancel url="welcome/index.do" code="distributor.cancel"/>

	

</form:form>