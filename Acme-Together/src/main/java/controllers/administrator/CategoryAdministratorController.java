
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
		res = this.createCreateModelAndView(category);

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@ModelAttribute("category") final Category category, final BindingResult binding) {

		ModelAndView res;
		Category categoryRes;

		if (binding.hasErrors())
			res = this.createCreateModelAndView(category);
		else
			try {
				categoryRes = this.categoryService.reconstruct(category, binding);
				this.categoryService.saveAndFlush(categoryRes);
				res = new ModelAndView("redirect: list.do");

			} catch (final Throwable tw) {
				res = this.createCreateModelAndView(category, "category.commit.error");
			}

		return res;
	}

	// Edit category -----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {

		ModelAndView res;
		Category category;

		category = this.categoryService.findOne(categoryId);
		res = this.createEditModelAndView(category);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@ModelAttribute("category") final Category category, final BindingResult binding) {

		ModelAndView res;
		Category categoryRes;

		if (binding.hasErrors())
			res = this.createEditModelAndView(category);
		else
			try {
				categoryRes = this.categoryService.reconstruct(category, binding);
				this.categoryService.saveAndFlush(categoryRes);
				res = new ModelAndView("redirect: list.do");

			} catch (final Throwable tw) {
				res = this.createEditModelAndView(category, "category.commit.error");
			}

		return res;
	}

	// Delete Category ---------------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int categoryId) {
		ModelAndView res;
		Category c;

		c = this.categoryService.findOne(categoryId);

		try {
			this.categoryService.delete(c);
			res = new ModelAndView("redirect: list.do");

		} catch (final Throwable tw) {
			res = new ModelAndView("forbiddenOperation");
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

	protected ModelAndView createCreateModelAndView(final Category category) {
		ModelAndView res;

		res = this.createCreateModelAndView(category, null);

		return res;
	}

	protected ModelAndView createCreateModelAndView(final Category category, final String message) {
		ModelAndView result;

		result = new ModelAndView("category/create");
		result.addObject("category", category);
		result.addObject("message", message);

		return result;
	}

}
