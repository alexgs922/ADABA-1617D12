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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<spring:message code="sh.Info" var="shInfo" />
<h2>
	<jstl:out value="${shInfo}"></jstl:out>
</h2>
<display:table pagesize="5" class="displaytag" name="shoppingGroup"
	requestURI="${requestURI}" id="sh">

	<jstl:if test="${sh.private_group eq true}">
		<spring:message code="sh.privateGroup" var="shPrivate" />
		<display:column title="${shPrivate}" sortable="true">
			<spring:message code="sh.privateGroupTrue" var="shPrivateTrue" />
			<jstl:out value="${shPrivateTrue}"></jstl:out>
		</display:column>

	</jstl:if>

	<jstl:if test="${sh.private_group eq false}">
		<spring:message code="sh.privateGroup" var="shPrivate" />
		<display:column title="${shPrivate}" sortable="true">
			<spring:message code="sh.privateGroupFalse" var="shPrivateFalse" />
			<jstl:out value="${shPrivateFalse}"></jstl:out>
		</display:column>

	</jstl:if>


	<spring:message code="sh.name" var="shName" />
	<display:column property="name" title="${shName}" sortable="false" />

	<spring:message code="sh.description" var="shDescription" />
	<display:column property="description" title="${shDescription}"
		sortable="false" />

	<spring:message code="sh.freePlaces" var="shFreePlaces" />
	<display:column property="freePlaces" title="${shFreePlaces}"
		sortable="true" />

	<spring:message code="sh.site" var="shSite" />
	<display:column property="site" title="${shSite}" sortable="false" />


</display:table>


<security:authorize access="hasRole('USER')">
	<jstl:if test="${shoppingGroup.creator.id == principal.id && allowedMakeOrder == true and not empty shoppingGroup.products}">
		<button
			onclick="location.href='shoppingGroup/user/makeOrder.do?shoppingGroupId=${sh.id}'">
			<spring:message code="shoppingGroup.makeOrder" />
		</button>
	</jstl:if>


</security:authorize>

<br>

<spring:message code="shoppingGroup.confirm.join" var="confirmJoin" />
<security:authorize access="hasRole('USER')">

	<jstl:if
		test="${shoppingGroup.lastOrderDate eq null and shoppingGroup.private_group eq false and shoppingGroup.creator.id != principal.id and !sh.users.contains(principal)and shoppingGroup.freePlaces gt 0}">
		<button
			onclick="if(confirm('${confirmJoin}'))
						location.href='shoppingGroup/user/join.do?shoppingGroupId=${shoppingGroup.id}'">
			<spring:message code="shoppingGroup.join" />
		</button>
	</jstl:if>

</security:authorize>

<br>

<spring:message code="sh.category" var="shCategory" />
<h2>
	<jstl:out value="${shCategory}"></jstl:out>
</h2>
<display:table pagesize="5" class="displaytag" name="category"
	requestURI="${requestURI}" id="cat">

	<spring:message code="category.name" var="categoryName" />
	<display:column property="name" title="${categoryName}"
		sortable="false" />

	<spring:message code="category.description" var="categoryDescription" />
	<display:column property="description" title="${categoryDescription}"
		sortable="false" />

</display:table>


<spring:message code="sh.users" var="shUsers" />
<h2>
	<jstl:out value="${shUsers}"></jstl:out>
</h2>
<display:table pagesize="5" class="displaytag" name="users"
	requestURI="${requestURI}" id="u">


	<spring:message code="user.picture" var="userPicture" />
	<display:column title="${userPicture}" sortable="false">
		<img src="${u.picture}" width="200" height="100" />
	</display:column>

	<spring:message code="user.name" var="userName" />
	<display:column property="name" title="${userName}" sortable="false" />

	<spring:message code="user.surname" var="userSurname" />
	<display:column property="surName" title="${userSurname}"
		sortable="false" />

	<spring:message code="user.identification" var="userIdentification" />
	<display:column property="identification" title="${userIdentification}"
		sortable="false" />

	<spring:message code="user.email" var="userEmail" />
	<display:column property="email" title="${userEmail}" sortable="false" />

	<display:column>
		<a href="user/profile.do?userId=${u.id}"> <spring:message
				code="user.profile" />
		</a>

	</display:column>


</display:table>


<spring:message code="sh.products" var="shProducts" />
<h2>
	<jstl:out value="${shProducts}"></jstl:out>
</h2>
<display:table pagesize="5" class="displaytag" name="products"
	requestURI="${requestURI}" id="p">


	<spring:message code="product.name" var="productName" />
	<display:column property="name" title="${productName}" sortable="false" />


	<display:column>
		<spring:message code="product.enlace" var="shEnlace" />
		<a href="${p.url}"><jstl:out value="${shEnlace}"></jstl:out> </a>

	</display:column>

	<spring:message code="product.referenceNumber"
		var="productreferenceNumber" />
	<display:column property="referenceNumber"
		title="${productreferenceNumber}" sortable="false" />

	<spring:message code="product.price" var="productPrice" />
	<display:column property="price" title="${productPrice}"
		sortable="false" />

	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:if test="${p.userProduct.id == principal.id}">
				<button
					onclick="location.href='shoppingGroup/user/editProduct.do?productId=${p.id}'">
					<spring:message code="product.edit" />
				</button>
			</jstl:if>
		</display:column>
	</security:authorize>

	
	<spring:message code = "product.confirm.delete" var = "productConfirmDelete" />



	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:if test="${p.userProduct.id == principal.id }">
				<button
					onclick="confirm('${productConfirmDelete }')
					location.href='shoppingGroup/user/deleteProduct.do?productId=${p.id}'">
					<spring:message code="product.delete" />
				</button>
			</jstl:if>
		</display:column>
	</security:authorize>



</display:table>

<security:authorize access="hasRole('USER')">
	<jstl:if test="${principal.shoppingGroup.contains(sh) && allowedMakeOrder == true}">
		<button
			onclick="location.href='shoppingGroup/user/addProduct.do?shoppingGroupId=${sh.id}'">
			<spring:message code="shoppingGroup.addProduct" />
		</button>
	</jstl:if>


</security:authorize>


<spring:message code="sh.cooments" var="shComments" />
<h2>
	<jstl:out value="${shComments}"></jstl:out>
</h2>
<display:table pagesize="10" class="displaytag" name="comments"
	requestURI="${requestURI}" id="com">


	<spring:message code="comment.title" var="commentTitle" />
	<display:column property="title" title="${commentTitle}"
		sortable="false" />

	<spring:message code="comment.text" var="commentText" />
	<display:column property="text" title="${commentText}" sortable="false" />

	<spring:message code="comment.moment" var="commentMoment" />
	<display:column property="moment" title="${commentMoment}"
		sortable="false" />

	<display:column>
		<a href="user/profile.do?userId=${com.userComment.id}"><jstl:out
				value="${com.userComment.name}"></jstl:out> </a>

	</display:column>



</display:table>
<jstl:if test="${principal.shoppingGroup.contains(sh)}">
<a href="shoppingGroup/user/comment.do?shoppingGroupId=${sh.id}"> <spring:message
						code="user.comment" /></a>
</jstl:if>



<spring:message code="sh.puntuation" var="shPuntuation" />
<h2><jstl:out value="${shPuntuation}"></jstl:out></h2>

<font size=5>
	<jstl:out value ="${sh.puntuation}"/>
</font>

<br/>
<br/>

<security:authorize access="hasRole('USER')">
	<jstl:choose>
		<jstl:when test="${alreadyPunctuate == false}">
			<button
				onclick="location.href='shoppingGroup/user/punctuate.do?shoppingGroupId=${sh.id}'">
				<spring:message code="shoppingGroup.punctuate" />
			</button>
		</jstl:when>	
		<jstl:when test="${alreadyPunctuate == true}">
			<button
				onclick="location.href='shoppingGroup/user/editPunctuation.do?shoppingGroupId=${sh.id}'">
				<spring:message code="shoppingGroup.punctuate" />
			</button>
			<br/>
			<br/>
			<a><b><spring:message code = "shoppingGroup.alreadyPunctuated"/> <jstl:out value = "${principalPunctuation.value}"/></b></a>
			<br/>
			<a><b><spring:message code = "shoppingGroup.alreadyPunctuated2"/></b></a>
		</jstl:when>
	</jstl:choose>

</security:authorize>





