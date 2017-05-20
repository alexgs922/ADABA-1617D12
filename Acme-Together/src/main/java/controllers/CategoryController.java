
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import domain.Category;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	//Constructor ---------------------------------------------------------------

	public CategoryController() {
		super();
	}


	@Autowired
	private CategoryService	categoryService;


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

}
