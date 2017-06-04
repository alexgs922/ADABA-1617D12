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


<form:form action="user/createCreditCard.do" modelAttribute="creditCard">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<br>
	<div class="row">
		<h4>
			<spring:message code="creditCard.Information" />
		</h4>
		<acme:textbox code="creditCard.holderName" path="holderName" />
		
		<label for="brandName"><spring:message code="creditCard.brandName" /></label>
		<select name="brandName" id="brandName">
			<option value="0">Select</option>
			<option value="VISA">VISA</option>
			<option value="MASTERCARD">MASTERCARD</option>
			<option value="DISCOVER">DISCOVER</option>
			<option value="DINNERS">DINNERS</option>
			<option value="AMEX">AMEX</option>
		</select>
		
		<acme:textbox code="creditCard.number" path="number" />
		<acme:textbox code="creditCard.expirationMonth" path="expirationMonth" />
		<acme:textbox code="creditCard.expirationYear" path="expirationYear" />
		<acme:textbox code="creditCard.cvvCode" path="cvvCode" />
	</div>
	<acme:submit name="save" code="creditCard.accept" />
	<br>
	<br>

</form:form>
<script type="text/javascript">
	$(document).ready(function() {
		$('select').material_select();
	});
</script>
