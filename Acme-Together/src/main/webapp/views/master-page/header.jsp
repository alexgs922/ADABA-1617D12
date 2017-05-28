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

<div>
	<img src="images/logo.png" alt="Acme-Together Co., Inc." width="30%" />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">

			<li><a class="fNiv"><spring:message
						code="master.page.dashboard" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/dashboard.do"><spring:message
								code="master.page.admin.dashboard" /></a></li>
					<li><a href="configuration/administrator/list.do"><spring:message
								code="master.page.admin.configuration" /></a></li>
				</ul>
			</li>

			<li><a class="fNiv" href="distributor/register.do"><spring:message
						code="master.page.registerDistributor" /></a></li>
			<li><a class="fNiv" href="commercial/register.do"><spring:message
						code="master.page.registerCommercial" /></a></li>

			<li><a class="fNiv" href="user/administrator/list.do"><spring:message
						code="master.page.allUsers" /></a></li>

			<li><a class="fNiv" href="category/administrator/list.do"><spring:message
						code="master.page.listCategories" /></a></li>
		</security:authorize>

		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv"><spring:message
						code="master.page.followPeople" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="user/myFriends.do"><spring:message
								code="master.page.yourFriends" /></a></li>
					<li><a href="user/listUnbanned.do"><spring:message
								code="master.page.followMorePeople" /></a></li>
				</ul></li>
			<li><a class="fNiv" href="user/viewProfile.do"><spring:message
						code="master.page.viewProfile" /></a></li>
			<li><a class="fNiv" href="shoppingGroup/user/list.do"><spring:message
						code="master.page.shoppingGroupsPublic" /></a></li>
			<li><a class="fNiv"
				href="shoppingGroup/user/joinedShoppingGroups.do"><spring:message
						code="master.page.UserJoinedShoppingGroups" /></a></li>
			<li><a class="fNiv"
				href="order/user/list.do"><spring:message
						code="master.page.UserOrders" /></a></li>			
			<li><a class="fNiv" href="coupon/user/list.do"><spring:message
						code="master.page.listCoupons" /></a></li>
		</security:authorize>

		<security:authorize access="hasRole('COMMERCIAL')">
			<li><a class="fNiv" href="commercial/viewProfile.do"><spring:message
						code="master.page.viewProfile" /></a></li>
			<li><a class="fNiv" href="coupon/commercial/list.do"><spring:message
						code="master.page.listCoupons" /></a></li>
		</security:authorize>

		<security:authorize access="hasRole('DISTRIBUTOR')">

			<li><a class="fNiv" href="distributor/viewProfile.do"><spring:message
						code="master.page.viewProfile" /></a></li>
			<li><a class="fNiv" href="warehouse/myWarehouses.do"><spring:message
						code="master.page.viewMyWarehouses" /></a></li>

		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<li><a class="fNiv" href="user/register.do"><spring:message
						code="master.page.registerUser" /></a></li>
			<li><a class="fNiv" href="category/list.do"><spring:message
						code="master.page.listCategories" /></a></li>
			<li><a class="fNiv" href="distributor/list.do"><spring:message
						code="master.page.listDistributors" /></a></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">

			<li><a class="fNiv" href="category/list.do"><spring:message
						code="master.page.listCategories" /></a></li>
			<li><a class="fNiv" href="distributor/list.do"><spring:message
						code="master.page.listDistributors" /></a></li>

			<li><a class="fNiv"><spring:message code="master.page.chirp" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="privateMessage/create.do"><spring:message
								code="master.page.chirp.create" /></a></li>
					<li><a href="privateMessage/listSentMessages.do"><spring:message
								code="master.page.chirp.sent" /></a></li>
					<li><a href="privateMessage/listReceivedMessages.do"><spring:message
								code="master.page.chirp.received" /></a></li>

				</ul></li>
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>

					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

