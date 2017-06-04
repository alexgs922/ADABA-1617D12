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

<form:form action="${requestURI}" modelAttribute="configuration">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<br>
	<div class="row">
		<h4>
			<spring:message code="configuration.info" />
		</h4>
		
		<acme:number code="configuration.fee" path="fee" step="1" min="0.0" />
		<br>
	
	</div>
	<br>
	<acme:submit name="save" code="configuration.save" />

	<a href="configuration/administrator/list.do" class="waves-effect waves-light btn"><spring:message code="configuration.cancel" /></a>

</form:form>