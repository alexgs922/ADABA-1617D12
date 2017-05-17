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

<!-- Listing grid -->

<display:table pagesize="5" class="displaytag" name="categories"
	requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->


	<spring:message code="category.name" var="categoryName" />
	<display:column property="name" title="${categoryName}" sortable="true" />

	<spring:message code="category.description" var="categoryDescription" />
	<display:column property="description" title="${categoryDescription}"
		sortable="false" />
		
		
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<button onclick = "location.href='category/administrator/edit.do?categoryId=${row.id}'"> <spring:message
					code="category.edit" />
			</button>
		</display:column>
	</security:authorize>
	
	<spring:message code="category.confirm.delete" var ="confirmDelete"/>
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<button onclick = "if(confirm('${confirmDelete }'))
			location.href='category/administrator/delete.do?categoryId=${row.id}'
			"  > <spring:message
					code="category.delete" />
			</button>
		</display:column>
	</security:authorize>		
		


</display:table>	
	

<!-- Action links -->

<security:authorize access ="hasRole('ADMIN')" >	
	<div>
		<button onclick = "location.href='category/administrator/create.do'"> 
		<spring:message code="category.create" />
		</button>
	</div>
</security:authorize>







