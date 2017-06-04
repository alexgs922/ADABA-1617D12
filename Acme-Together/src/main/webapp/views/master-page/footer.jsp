<%--
 * footer.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:useBean id="date" class="java.util.Date" />

<div class="footer-copyright">
	<div class="container">
		<b>Copyright &copy; <fmt:formatDate value="${date}" pattern="yyyy" />
			Acme-Together Co., Inc.
		</b>
		<div class="fixed-action-btn horizontal click-to-toggle">
		  <a class="btn-floating btn-large red">
		    <i class="material-icons">menu</i>
		  </a>
		  <ul>
		    <li><a class="btn-floating red" href="?language=en" title="en"><i class="material-icons">language</i></a></li>
		    <li><a class="btn-floating blue" href="?language=es" title="es"><i class="material-icons">translate</i></a></li>
		  </ul>
		</div>
	</div>
</div>

