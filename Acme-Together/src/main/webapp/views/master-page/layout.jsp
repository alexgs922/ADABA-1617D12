<%--
 * layout.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="shortcut icon" href="favicon.ico" />

<link rel="stylesheet"
	href='<jstl:url value="http://fonts.googleapis.com/icon?family=Material+Icons" />'
	type="text/css">

<link href='<jstl:url value="https://fonts.googleapis.com/css?family=Source+Sans+Pro" />' rel="stylesheet">

<!-- <script type="text/javascript" src="scripts/jquery.js"></script>
<script type="text/javascript" src="scripts/jquery-ui.js"></script>
<script type="text/javascript" src="scripts/jmenu.js"></script> -->

<link rel="stylesheet" href="styles/common.css" type="text/css">
<link rel="stylesheet" href="styles/materialize.css" type="text/css">
<script type="text/javascript" src="scripts/jquery-3.2.1.min.js"></script>
<!-- <link rel="stylesheet" href="styles/jmenu.css" media="screen"
	type="text/css" />
<link rel="stylesheet" href="styles/displaytag.css" type="text/css"> -->

<title><tiles:insertAttribute name="title" ignore="true" /></title>

<script type="text/javascript">
	$(document).ready(function() {
	 $(".dropdown-button").dropdown();
	});
	 
	function askSubmission(msg, form) {
		if (confirm(msg))
			form.submit();
	}
</script>

<style type="text/css">
  
body {
	display: flex;
	min-height: 100vh;
	flex-direction: column;
}

main {
	flex: 1 0 auto;
}
</style>

</head>

<body>

	<nav>
		<tiles:insertAttribute name="header" />
	</nav>
	<main>
		<div class="container">
			<h1>
				<tiles:insertAttribute name="title" />
			</h1>
			<tiles:insertAttribute name="body" />
			<jstl:if test="${message != null}">
				<br />
				<span class="message"><spring:message code="${message}" /></span>
			</jstl:if>
		</div>
	</main>
	<footer class="page-footer">
		<tiles:insertAttribute name="footer" />
	</footer>
	<script type="text/javascript" src="scripts/materialize.min.js"></script>
</body>
</html>