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

<form:form action="shoppingGroup/user/comment.do?shoppingGroupId=${shoppingGroup.id}" modelAttribute="comment">

	<br>	
	<div class="row">
	<acme:textbox code="comment.title" path="title" />
	<br>

	<acme:textarea code="comment.text" path="text" />
	<br>
	</div>
	<acme:submit name="save" code="comment.save" />
	<a href="shoppingGroup/user/display.do?shoppingGroupId=${shoppingGroup.id}" class="waves-effect waves-light btn"><spring:message code="comment.cancel" /></a>
	<br />
	

</form:form>