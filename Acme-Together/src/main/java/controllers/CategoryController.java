
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.UserService;
import domain.Category;
import domain.ShoppingGroup;
import domain.User;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	//Constructor ---------------------------------------------------------------

	public CategoryController() {
		super();
	}


	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private UserService		userService;


	//List categories -------------------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<Category> categories;

		categories = this.categoryService.findAllLogout();

		res = new ModelAndView("category/list");
		res.addObject("categories", categories);
		res.addObject("requestURI", "/category/list.do");

		return res;
	}

	@RequestMapping(value = "/groupsFrom", method = RequestMethod.GET)
	public ModelAndView groups(@RequestParam final int categoryId) {

		ModelAndView res;
		Collection<ShoppingGroup> sh;
		User principal;

		sh = this.categoryService.shFromCategory(categoryId);
		principal = this.userService.findByPrincipal();

		res = new ModelAndView("category/shlist");
		res.addObject("shoppingGroups", sh);
		res.addObject("isempty", sh.isEmpty());
		res.addObject("principal", principal);
		res.addObject("requestURI", "/category/groupsFrom.do?categoryId=" + categoryId);

		return res;

	}

}
