
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.OrderDomainService;
import services.ShoppingGroupService;
import services.UserService;
import controllers.AbstractController;
import domain.ShoppingGroup;
import domain.User;

@Controller
@RequestMapping("/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services ---------------------------------------

	@Autowired
	private UserService				userService;

	@Autowired
	private OrderDomainService		orderDomainService;

	@Autowired
	private ShoppingGroupService	shoppingGroupService;


	// Constructors -----------------------------------

	public DashboardAdministratorController() {
		super();
	}

	// List Category ---------------------------------------------------------

	@RequestMapping(value = "/dashboard")
	public ModelAndView ownDashboard() {
		ModelAndView res;

		final Double q1 = this.userService.numberOfUserRegistered();

		final Double q2 = this.orderDomainService.numberOfOrderLastMonth();

		final Collection<User> q3 = this.userService.usersWhoCreateMoreShoppingGroup();

		final Collection<User> q4 = this.userService.usersWhoCreateMinusShoppingGroup();

		final Collection<ShoppingGroup> q5 = this.shoppingGroupService.shoppingGroupsWithMorePuntuation();

		final Collection<ShoppingGroup> q6 = this.shoppingGroupService.shoppingGroupsWithLessPuntuation();

		final Double q7 = this.shoppingGroupService.percentShoppingGroupsWithFreePlaces();

		final Double q8 = this.shoppingGroupService.percentShoppingGroupsWithoutFreePlaces();

		res = new ModelAndView("administrator/dashboard");
		res.addObject("numberOfUserRegistered", q1);
		res.addObject("numberOfOrderLastMonth", q2);
		res.addObject("usersWhoCreateMoreShoppingGroup", q3);
		res.addObject("usersWhoCreateMinusShoppingGroup", q4);
		res.addObject("shoppingGroupsWithMorePuntuation", q5);
		res.addObject("shoppingGroupsWithLessPuntuation", q6);
		res.addObject("percentShoppingGroupsWithFreePlaces", q7);
		res.addObject("percentShoppingGroupsWithoutFreePlaces", q8);

		return res;

	}
}
