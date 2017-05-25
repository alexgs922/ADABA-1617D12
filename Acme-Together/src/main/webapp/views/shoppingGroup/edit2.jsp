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



<form:form action="${requestURI}" modelAttribute="shoppingGroup">
	
	<fieldset>
		<legend><spring:message code="sh.Info"/></legend>
		<br>
		
		<form:label path="private_group">
		<spring:message code="sh.privateGroup" />
		</form:label>
		<jstl:choose>
			<jstl:when test="${shoppingGroup.private_group eq true}">
				<input type="radio" name="private_group" value="true" checked="checked">
				<spring:message code="sh.privateGroupTrue" />
				<input type="radio" name="private_group" value="false" />
				<spring:message code="sh.privateGroupFalse" />
			</jstl:when>
			<jstl:otherwise>
				<input type="radio" name="private_group" value="true">
				<spring:message code="sh.privateGroupTrue" />
				<input type="radio" name="private_group" value="false" checked="checked"/>
				<spring:message code="sh.privateGroupFalse" />
			</jstl:otherwise>
		</jstl:choose>
		<br>
		
		<br>
		<acme:textbox code="sh.name" path="name"/>
		<br>
		<acme:textarea code="sh.description" path="description"/>
		<br>
		
		<form:label path="freePlaces">
		<spring:message code="sh.freePlaces" />
		</form:label>
		<form:input type="number" min="0" step="1" path="freePlaces"/>
		<br>
		<br>
		<acme:textbox code="sh.site" path="site"/>
		<br>
	</fieldset>


	<fieldset>
		<br>
		<legend><spring:message code="sh.category"/></legend>
		<acme:selectSH items="${categories}" itemLabel="name" code="sh.category" path="category"/>
		<br>
	</fieldset>
	
	<br>
	
	<br>
	<acme:submit name="save" code="sh.accept"/>			
	<acme:cancel url="shoppingGroup/user/joinedShoppingGroups.do" code="sh.cancel"/>
	<br>
	<br>

	

</form:form>