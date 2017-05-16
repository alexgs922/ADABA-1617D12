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



<form:form action="user/register.do" modelAttribute="user">
	<fieldset>
		<legend><spring:message code="user.userAccountInfo"/></legend>
		<acme:textbox code="user.username" path="username"/>
		<acme:password code="user.password" path="password"/>
		<acme:password code="user.passwordConf" path="passwordCheck"/>
	</fieldset>


	<fieldset>
		<legend><spring:message code="user.contactInfo"/></legend>
		<acme:textbox code="user.name" path="name"/>
		<acme:textbox code="user.surname" path="surName"/>
		<acme:textbox code="user.myEmail" path="email"/>
		<acme:textbox code="user.myPhone" path="phone"/>
	</fieldset>
	
	
	<fieldset>
		<legend><spring:message code="user.personalInfo"/></legend>
		<acme:textbox code="user.picture" path="picture"/>
		<acme:textarea code="user.desciption" path="description"/>
		<acme:textbox code="user.birthDate" path="birthDate"/>
		<acme:textbox code="user.adress" path="adress"/>
		<acme:textbox code="user.identefication" path="identefication"/>
		
	</fieldset>
	

	<form:checkbox path="termsOfUse"/>
	<spring:message code="user.termsOfUse.confirmation"/> 
	<a href="user/dataProtection.do">
		<spring:message code="user.termsOfUse.link" />
	</a>
	<form:errors cssClass="error" path="termsOfUse" />
	<br>
	<acme:submit name="save" code="user.accept"/>			
	<acme:cancel url="welcome/index.do" code="user.cancel"/>

	

</form:form>