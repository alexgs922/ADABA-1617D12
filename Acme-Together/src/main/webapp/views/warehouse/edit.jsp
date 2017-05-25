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


<form:form action="warehouse/edit.do" modelAttribute="warehouse">
<form:hidden path="id"/>
<form:hidden path="version"/>

<fieldset>
		<legend><spring:message code="warehouse.Info"/></legend>
		<acme:textbox code="warehouse.name" path="name"/>
		<acme:textbox code="warehouse.warehouseAddress" path="warehouseAddress"/>
	
	</fieldset>
	<acme:submit name="save" code="warehouse.accept"/>			
	
</form:form>