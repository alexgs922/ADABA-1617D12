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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="user/editProfile.do" modelAttribute="user">
<form:hidden path="id"/>
<form:hidden path="version"/>

<fieldset>
		<legend><spring:message code="user.contactInfo"/></legend>
		<acme:textbox code="user.name" path="name"/>
		<acme:textbox code="user.surname" path="surName"/>
		<acme:textbox code="user.email" path="email"/>
		<acme:textbox code="user.phone" path="phone"/>

	</fieldset>
	<acme:submit name="save" code="user.accept"/>			
	
</form:form>