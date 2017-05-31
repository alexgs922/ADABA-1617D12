
package funcionalTesting;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.AdministratorService;
import services.CategoryService;
import utilities.AbstractTest;
import domain.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private CategoryService			categoryService;


	// Test 1: Test Relacionado con la creación de categorias 

	protected void templateCreateCategory(final String username, final String name, final String description, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Category category = this.categoryService.create();

			category.setName(name);
			category.setDescription(description);

			this.categoryService.saveAndFlush(category);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverCreateCategory() {

		final Object testingData[][] = {
			{   //admin crea una categoría correctamente
				"admin", "Category45", "Description1", null
			}, {
				//user 1 intenta crear una categoría, el sistema no se lo permite.
				"user1", "Category78", "Description123", IllegalArgumentException.class
			}, {
				//admin intenta crear una categoría con los campos vacíos
				"admin", "", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateCategory((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// Test 2: Test Relacionado con la edición de categorias

	protected void templateEditCategory(final String username, final Category cat, final String name, final String description, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			cat.setName(name);
			cat.setDescription(description);

			this.categoryService.saveAndFlush(cat);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverEditCategory() {

		this.authenticate("admin");
		final Category category = this.categoryService.findOne(567);
		this.unauthenticate();

		final Object testingData[][] = {
			{   //admin editar una categoría correctamente
				"admin", category, "Category45", "Description1", null
			}, {
				//user 1 intenta editar una categoría, el sistema no se lo permite.
				"user1", category, "Category78", "Description123", IllegalArgumentException.class
			}, {
				//admin intenta editar una categoría con los campos vacíos
				"admin", category, "", "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditCategory((String) testingData[i][0], (Category) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Test 3: Test relacionado con la eliminación de una categoría

	protected void templateDeleteCategory(final String username, final Category category, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			this.categoryService.delete(category);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverDeleteCategory() {

		this.authenticate("admin");
		final Category category = this.categoryService.findOne(567);
		this.unauthenticate();

		final Object testingData[][] = {
			{   //admin borra una categoría correctamente
				"admin", category, null
			}, {
				//user 1 intenta borrar una categoría, el sistema no se lo permite.
				"user1", category, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteCategory((String) testingData[i][0], (Category) testingData[i][1], (Class<?>) testingData[i][2]);

	}
}
