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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="privateMessage/response/create.do" modelAttribute="privateMessage">

    <form:hidden path="recipient"/>
	<br>
	<div class="row">
	<acme:textbox code="chirp.subject" path="subject" />
	<br>

	<acme:textarea code="chirp.text" path="text" />
	<br>

	<acme:textarea code="chirp.attachments" path="attachments" />
	<br>
	</div>


	<acme:submit name="save" code="chirp.save" />

	<a href="privateMessage/listReceivedMessages.do" class="waves-effect waves-light btn"><spring:message code="chirp.cancel" /></a>

</form:form>