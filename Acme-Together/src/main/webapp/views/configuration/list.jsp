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


<display:table pagesize="5" class="highlight" name="configurations"
	requestURI="${requestURI}" id="row">

	<spring:message code="configuration.fee" var="configurationFee" />
	<display:column property="fee" title="${configurationFee}" sortable="false" />
	

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<button class="waves-effect waves-light btn" onclick="location.href='configuration/administrator/edit.do?configurationId=${row.id}'"> <spring:message code="configuration.change" /></button>
		</display:column>
	</security:authorize>

</display:table>
