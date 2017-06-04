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

	<br>
	<div class="row">
		<h4><spring:message code="user.userAccountInfo"/></h4>
	
		<acme:textbox code="user.username" path="username" />
		<acme:password code="user.password" path="password"/>
		<acme:password code="user.passwordConf" path="passwordCheck"/>
	</div>

	<div class="row">
		<h4><spring:message code="user.contactInfo"/></h4>
		<acme:textbox code="user.name" path="name"/>
		<acme:textbox code="user.surname" path="surName"/>
		<acme:textbox code="user.myEmail" path="email"/>
		<acme:textbox code="user.myPhone" path="phone"/>
	</div>
	
	
	<div class="row">
		<h4><spring:message code="user.personalInfo"/></h4>
		<acme:textbox code="user.picture" path="picture" placeholder="https://flickr.com"/>
		<acme:textarea code="user.desciption" path="description"/>
		<acme:textbox code="user.birthDate" path="birthDate"/>
		<acme:textbox code="user.adress" path="adress"/>
		<acme:textbox code="user.identification" path="identefication"/>
	</div>
	

	<input id="termsOfUse" name="termsOfUse" class="filled-in" type="checkbox" value="true">
	<label for="termsOfUse">
	<spring:message code="user.termsOfUse.confirmation"/> 
	<a href="user/dataProtection.do">
		<spring:message code="user.termsOfUse.link" />
	</a>
	</label>
	<form:errors cssClass="error" path="termsOfUse" />
	<br>
	<br>
	<acme:submit name="save" code="user.accept"/>
	<a href="welcome/index.do" class="waves-effect waves-light btn"><spring:message code="user.cancel" /></a>
	<br>
	<br>

</form:form>