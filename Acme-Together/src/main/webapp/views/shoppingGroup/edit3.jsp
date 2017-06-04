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
	<br>
	<div class="row">
		<h4><spring:message code="sh.Info"/></h4>		
		<br>
		<acme:textbox code="sh.name" path="name"/>
		<br>
		<acme:textarea code="sh.description" path="description"/>
		<br>
		<acme:textbox code="sh.site" path="site"/>
		<br>
		
		<spring:message code="sh.users"/><jstl:out value=" : "></jstl:out>
		<form:checkboxes items="${usuarios}" path="users" itemLabel="name" multiple="multiple"/>
		<form:errors cssClass="error" path="users" />
		<jstl:if test="${empty usuarios}">
			<spring:message code="sh.users.empty" var="vacioUsuarios"/><b><jstl:out value="${vacioUsuarios}"></jstl:out></b>
		</jstl:if>
		<br>
		
	</div>


	<div class="row">
		<acme:select items="${categories}" itemLabel="name" code="sh.category" path="category"/>
		<br>
	</div>
	
	<br>

	<input id="termsOfUse" name="termsOfUse" class="filled-in" type="checkbox" value="true">
	<label for="termsOfUse">
	<spring:message code="sh.termsOfUse.confirmation"/> 
	<a href="user/dataProtection.do">
		<spring:message code="sh.termsOfUse.link" />
	</a>
	</label>
	<form:errors cssClass="error" path="termsOfUse" />
	<br>
	<br>
	<acme:submit name="save" code="sh.accept"/>			
	<a href="shoppingGroup/user/joinedShoppingGroups.do" class="waves-effect waves-light btn"><spring:message code="sh.cancel" /></a>
	<br>
	<br>

</form:form>
<script type="text/javascript">
	$(document).ready(function() {
		$('select').material_select();
	});
</script>