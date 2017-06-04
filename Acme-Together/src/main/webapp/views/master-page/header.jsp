<%--
 * header.jsp
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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>


<ul id="chirp1" class="dropdown-content">
	<li><a href="privateMessage/create.do"><spring:message
				code="master.page.chirp.create" /></a></li>
	<li><a href="privateMessage/listSentMessages.do"><spring:message
				code="master.page.chirp.sent" /></a></li>
	<li><a href="privateMessage/listReceivedMessages.do"><spring:message
				code="master.page.chirp.received" /></a></li>
</ul>
<ul id="profile1" class="dropdown-content">
	<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
</ul>
<ul id="dashboard1" class="dropdown-content">
	<li><a href="administrator/dashboard.do"><spring:message code="master.page.admin.dashboard" /></a></li>
	<li><a href="configuration/administrator/list.do"><spring:message code="master.page.admin.configuration" /></a></li>
</ul>
<ul id="follow1" class="dropdown-content">
	<li><a href="user/myFriends.do"><spring:message
				code="master.page.yourFriends" /></a></li>
	<li><a href="user/listUnbanned.do"><spring:message
				code="master.page.followMorePeople" /></a></li>
</ul>


<div class="nav-wrapper">
  <a href="welcome/index.do" class="brand-logo" style="margin-left:2%;"><img src="images/logoPaquete.png" alt="Acme-Together Co., Inc." width="11%" /></a>
  <ul class="right hide-on-med-and-down">
	<security:authorize access="hasRole('ADMIN')">
	<li><a class="dropdown-button" href="#!" data-activates="dashboard1"><spring:message code="master.page.dashboard" /><i class="material-icons right">arrow_drop_down</i></a></li>
	<li><a href="distributor/register.do"><spring:message code="master.page.registerDistributor" /></a></li>
	<li><a href="commercial/register.do"><spring:message code="master.page.registerCommercial" /></a></li>
	<li><a href="user/administrator/list.do"><spring:message code="master.page.allUsers" /></a></li>
	<li><a href="category/administrator/list.do"><spring:message code="master.page.listCategories" /></a></li>
	</security:authorize>
	<security:authorize access="hasRole('USER')">
	<li><a class="dropdown-button" href="#!" data-activates="follow1"><spring:message code="master.page.followPeople" /><i class="material-icons right">arrow_drop_down</i></a></li>
	<li><a href="user/viewProfile.do"><spring:message code="master.page.viewProfile" /></a></li>
	<li><a href="shoppingGroup/user/list.do"><spring:message code="master.page.shoppingGroupsPublic" /></a></li>
	<li><a href="shoppingGroup/user/joinedShoppingGroups.do"><spring:message code="master.page.UserJoinedShoppingGroups" /></a></li>
	<li><a href="order/user/list.do"><spring:message code="master.page.UserOrders" /></a></li>			
	<li><a href="coupon/user/list.do"><spring:message code="master.page.listCoupons" /></a></li>
	</security:authorize>
	<security:authorize access="hasRole('COMMERCIAL')">
	<li><a href="commercial/viewProfile.do"><spring:message	code="master.page.viewProfile" /></a></li>
	<li><a href="coupon/commercial/list.do"><spring:message code="master.page.listCoupons" /></a></li>
	</security:authorize>
	<security:authorize access="hasRole('DISTRIBUTOR')">
	<li><a href="distributor/viewProfile.do"><spring:message code="master.page.viewProfile" /></a></li>
	<li><a href="warehouse/myWarehouses.do"><spring:message code="master.page.viewMyWarehouses" /></a></li>
	</security:authorize>
	<security:authorize access="isAuthenticated()">
	<li><a href="category/list.do"><spring:message code="master.page.listCategories" /></a></li>
	<li><a href="distributor/list.do"><spring:message code="master.page.listDistributors" /></a></li>
	<li><a class="dropdown-button" href="#!" data-activates="chirp1"><spring:message code="master.page.chirp" /><i class="material-icons right">arrow_drop_down</i></a></li>
	<li><a class="dropdown-button" href="#!" data-activates="profile1"><spring:message code="master.page.profile" /> (<security:authentication property="principal.username" />)<i class="material-icons right">arrow_drop_down</i></a></li>
	</security:authorize>
	<security:authorize access="isAnonymous()">
	<li><a href="security/login.do"><spring:message code="master.page.login" /></a></li>
	<li><a href="user/register.do"><spring:message code="master.page.registerUser" /></a></li>
	<li><a href="category/list.do"><spring:message code="master.page.listCategories" /></a></li>
	<li><a href="distributor/list.do"><spring:message code="master.page.listDistributors" /></a></li>
	</security:authorize> 
  </ul>
</div>


