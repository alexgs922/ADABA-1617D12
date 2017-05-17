
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ShoppingGroupService;
import services.UserService;
import controllers.AbstractController;
import domain.ShoppingGroup;
import domain.User;

@Controller
@RequestMapping("/shoppingGroup/user")
public class ShoppingGroupUserController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public ShoppingGroupUserController() {
		super();
	}


	// Services -------------------------------------------------------------------

	@Autowired
	private ShoppingGroupService	shoppingGroupService;

	@Autowired
	private UserService				userService;


	// List my joined shoppingGroups ----------------------------------------------

	@RequestMapping(value = "/joinedShoppingGroups", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<ShoppingGroup> shoppingGroups;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroups = principal.getShoppingGroup();

		res = new ModelAndView("shoppingGroup/list");
		res.addObject("shoppingGroups", shoppingGroups);
		res.addObject("requestURI", "/shoppingGroup/user/joinedShoppingGroups.do");

		return res;

	}

}
