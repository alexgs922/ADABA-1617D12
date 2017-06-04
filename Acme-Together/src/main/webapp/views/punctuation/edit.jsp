<%--

 *
 * Copyright (C) 2016 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
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

<form:form action="shoppingGroup/user/editPunctuation.do?shoppingGroupId=${shoppingGroup.id }&oldValue=${oldValue}"
	modelAttribute="punctuation">
	
	
	<form:hidden path="id"/>
	<form:hidden path="version"/> 
	<br>
	<div class="row">
	
	<label for="value"><spring:message code="punctuation.value" /></label>
	<select name="value" id="value">
		<option value="0">Select</option>
		<option value ="-5">-5</option>
		
		<option value ="-4">-4</option>
		
		<option value ="-3">-3</option>
		
		<option value ="-2">-2</option>
		
		<option value ="-1">-1</option>

		<option value ="0">0</option>

		<option value ="1">1</option>

		<option value ="2">2</option>

		<option value ="3">3</option>

		<option value ="4">4</option>

		<option value ="5">5</option>
	</select>

	<form:errors cssClass="error" path="value"/>

	<br />
	</div>
	
	<acme:submit name="save" code="punctuation.save"/>&nbsp; 
	<a href="shoppingGroup/user/display.do?shoppingGroupId=${shoppingGroup.id}" class="waves-effect waves-light btn"><spring:message code="punctuation.cancel" /></a>
	<br />

</form:form>
<script type="text/javascript">
	$(document).ready(function() {
		$('select').material_select();
	});
</script>





