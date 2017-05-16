
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import controllers.AbstractController;
import domain.Category;

@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController {

	//Constructor ---------------------------------------------------------------

	public CategoryAdministratorController() {
		super();
	}


	@Autowired
	private CategoryService	categoryService;


	//List categories -------------------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<Category> categories;

		categories = this.categoryService.findAll();

		res = new ModelAndView("category/list");
		res.addObject("categories", categories);
		res.addObject("requestURI", "/category/administrator/list.do");

		return res;

	}

	//Create category --------------------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView res;
		Category category;

		category = this.categoryService.create();
		res = this.createEditModelAndView(category);

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@ModelAttribute("category") final Category category, final BindingResult binding) {

		ModelAndView res;
		Category categoryRes;

		try {
			categoryRes = this.categoryService.reconstruct(category, binding);
			this.categoryService.saveAndFlush(categoryRes);
			res = new ModelAndView("redirect: list.do");

		} catch (final Throwable tw) {
			res = this.createEditModelAndView(category, "category.commit.error");
		}

		return res;
	}

	//  Ancillary methods -------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Category category) {
		ModelAndView res;

		res = this.createEditModelAndView(category, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String message) {
		ModelAndView result;

		result = new ModelAndView("category/edit");
		result.addObject("category", category);
		result.addObject("message", message);

		return result;
	}

}
