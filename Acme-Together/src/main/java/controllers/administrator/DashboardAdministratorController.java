
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import services.OrderDomainService;
import services.ShoppingGroupService;
import services.UserService;
import controllers.AbstractController;
import domain.User;

@Controller
@RequestMapping("/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services ---------------------------------------

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CommentService			commentService;

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

		res = new ModelAndView("administrator/dashboard");
		res.addObject("numberOfUserRegistered", q1);
		res.addObject("numberOfOrderLastMonth", q2);
		res.addObject("usersWhoCreateMoreShoppingGroup", q3);
		res.addObject("usersWhoCreateMinusShoppingGroup", q4);

		return res;

	}
}
