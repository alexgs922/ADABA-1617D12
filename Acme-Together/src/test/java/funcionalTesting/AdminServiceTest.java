
package funcionalTesting;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.AdministratorService;
import services.CategoryService;
import services.UserService;
import utilities.AbstractTest;
import domain.Category;
import domain.User;

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

	@Autowired
	private UserService				userService;


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

	// Test 4: El administrador puede bannear a un Usuario

	// Baneamos a un user, y posteriomente comprobamos que el user ha sido
	// banneado correctamente, es decir cuando
	// su atributo booleano ban es true
	protected void templateBanUser(final String username, final User user, final boolean result, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			this.authenticate(username);

			final User res = user;
			this.userService.banUser(res);

			Assert.isTrue(res.isBanned() == result);

			this.unauthenticate();
			this.userService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	// Chorbi = 63,64,65,66,67,68
	@Test
	public void driverBanUser() {

		final User user1 = this.userService.findOneToSent(591); // Obtenemos de la base de datos el user con id = 591 inicialmente no banneado
		final User user2 = this.userService.findOneToSent(594); //Obtenemos de la base de datos el user con id = 594 inicialmente banneado

		final Object testingData[][] = {
			// TEST POSITIVO: Bannear un user que aun no esta baneado, y
			// comprobar que el resultado es correcto.
			{
				"admin", user1, true, null
			},
			// TEST NEGATIVO: Bannear un un user que ya ha sido banneado y
			// comprobar que salta correctamente la excepcion.
			{
				"admin", user2, true, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateBanUser((String) testingData[i][0], (User) testingData[i][1], (boolean) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// Test 5: El administrador puede bannear a un Usuario

	// Permitimos a un user, y posteriomente comprobamos que el user ha sido
	// permitido correctamente, es decir cuando
	// su atributo booleano ban es false
	protected void templateUnbanUser(final String username, final User user, final boolean result, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			this.authenticate(username);

			final User res = user;
			this.userService.unBanUser(res);

			Assert.isTrue(res.isBanned() == result);

			this.unauthenticate();
			this.userService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverUnbanUser() {

		final User user1 = this.userService.findOneToSent(594); // Obtenemos de la base de datos el user con id = 594 inicialmente baneado
		final User user2 = this.userService.findOneToSent(591); // Obtenemos de la base de datos el user con id = 591 inicialmente sin banear

		final Object testingData[][] = {
			// TEST POSITIVO: Permitir un user que esta baneado, y
			// comprobar que el resultado es correcto.
			{
				"admin", user1, false, null
			},
			// TEST NEGATIVO: Permitir un user que ya se le permite entrar
			// en el sistema y comprobar que salta correctamente la
			// excepcion.
			{
				"admin", user2, false, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUnbanUser((String) testingData[i][0], (User) testingData[i][1], (boolean) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Test:6 Con este test comprobamos que al intentar logearnos con un user que se
	// encuentra banneado no es posible realizar dicha operacin y el test
	// devuelve IllegalArgumentException
	@Test(expected = IllegalArgumentException.class)
	public void testLoginBannedUser() {

		this.authenticate("user4");

	}
}
