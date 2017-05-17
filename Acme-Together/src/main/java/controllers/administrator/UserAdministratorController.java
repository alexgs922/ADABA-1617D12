
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import controllers.AbstractController;
import domain.User;

@Controller
@RequestMapping("/user/administrator")
public class UserAdministratorController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public UserAdministratorController() {
		super();
	}


	// Services -------------------------------------------------------------------

	@Autowired
	private UserService	userService;


	//List all users (banned and not banned) --------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<User> users;

		users = this.userService.findAll();

		result = new ModelAndView("user/administrator/list");
		result.addObject("users", users);
		result.addObject("requestURI", "user/administrator/list.do");

		return result;
	}

	// Ban and unBan user -------------------------------------------------------------------------

	@RequestMapping(value = "/banUser", method = RequestMethod.GET)
	public ModelAndView banUser(@RequestParam final int userId) {
		ModelAndView result;
		User user;

		user = this.userService.findOne(userId);

		try {
			this.userService.banUser(user);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable th) {
			result = new ModelAndView("forbiddenOperation");

		}

		return result;

	}

	@RequestMapping(value = "/unBanUser", method = RequestMethod.GET)
	public ModelAndView unBanUser(@RequestParam final int userId) {
		ModelAndView result;
		User user;

		user = this.userService.findOne(userId);

		try {
			this.userService.unBanUser(user);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable th) {
			result = new ModelAndView("forbiddenOperation");

		}

		return result;

	}

}
