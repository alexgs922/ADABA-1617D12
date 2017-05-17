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


<form:form action="commercial/editProfile.do" modelAttribute="commercial">
<form:hidden path="id"/>
<form:hidden path="version"/>

<fieldset>
		<legend><spring:message code="commercial.contactInfo"/></legend>
		<acme:textbox code="commercial.name" path="name"/>
		<acme:textbox code="commercial.surname" path="surName"/>
		<acme:textbox code="commercial.email" path="email"/>
		<acme:textbox code="commercial.phone" path="phone"/>

	</fieldset>
	<acme:submit name="save" code="commercial.accept"/>			
	
</form:form>