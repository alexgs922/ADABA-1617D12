
package funcionalTesting;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import services.ActorService;
import services.DistributorService;
import services.WarehouseService;
import utilities.AbstractTest;
import domain.Distributor;
import domain.Warehouse;
import forms.DistributorForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private DistributorService	distributorService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private WarehouseService	warehouseService;


	//user = 1222,...,1226
	//distributor = 1195,...,1194
	//admin = 1187

	//Test dedicado a probar el caso de uso: Crear warehouse
	protected void template1(final String username, final int principalId, final String name, final String warehouseAddress, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Distributor principal = this.distributorService.findOne(principalId);
			final Warehouse w = this.warehouseService.create();
			w.setName(name);
			w.setWarehouseAddress(warehouseAddress);
			final BindingResult binding = null;
			final Warehouse res = this.warehouseService.reconstruct(w, binding);
			res.setDistributor(principal);
			this.warehouseService.save(res);
			this.warehouseService.flush();
			this.unauthenticate();
			this.distributorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//user = 1222,...,1226
	//distributor = 1195,...,1194
	//admin = 1187
	@Test
	public void driver1() {

		final Object testingData[][] = {
			{   //distributor 1 crea un warehouse valido
				"distributor1", 564, "warehousePrueba", "warehouseAddressPrueba", null
			}, {
				//user1 intenta crear un warehouse 
				"user1", 591, "warehousePrueba", "warehouseAddressPrueba", IllegalArgumentException.class
			}, {
				//commercial1 intenta crear warehouse 
				"commercial1", 557, "warehousePrueba", "warehousePrueba", IllegalArgumentException.class
			}, {
				//distributor1 intenta crear un warehouse con campos invalidos
				"distributor1", 564, null, null, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (int) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Test positivo
	@Test
	public void testDelete1() {

		this.authenticate("distributor1");
		final Warehouse w = this.warehouseService.findOne(618);
		this.warehouseService.delete(w);
		this.warehouseService.flush();
	}

	//Test negativo: Distributor 1 intenta eliminar warehouse que no es suyo
	@Test(expected = IllegalArgumentException.class)
	public void testDelete2() {

		this.authenticate("distributor1");
		final Warehouse w = this.warehouseService.findOne(619);
		this.warehouseService.delete(w);
		this.warehouseService.flush();
	}

	//Test negativo: User1 intenta eliminar warehouse
	@Test(expected = NullPointerException.class)
	public void testDelete3() {

		this.authenticate("user1");
		final Warehouse w = this.warehouseService.findOne(619);
		this.warehouseService.delete(w);
		this.warehouseService.flush();
	}
	//Test negativo: Commercial1 intenta eliminar warehouse
	@Test(expected = NullPointerException.class)
	public void testDelete4() {

		this.authenticate("commercial1");
		final Warehouse w = this.warehouseService.findOne(619);
		this.warehouseService.delete(w);
		this.warehouseService.flush();
	}

	//Test dedicado a probar el caso de uso de registro de manager
	protected void template3(final String username, final String companyAddress, final String companyName, final String email, final String vatNumber, final String name, final String password, final String passwordCheck, final String phone,
		final String webPage, final String surName, final String username1, final boolean termsOfUse, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final DistributorForm customerForm = new DistributorForm();
			customerForm.setCompanyAddress(companyAddress);
			customerForm.setCompanyName(companyName);
			customerForm.setEmail(email);
			customerForm.setVatNumber(vatNumber);
			customerForm.setName(name);
			customerForm.setPassword(password);
			customerForm.setPasswordCheck(passwordCheck);
			customerForm.setPhone(phone);
			customerForm.setWebPage(webPage);
			customerForm.setSurName(surName);
			customerForm.setTermsOfUse(termsOfUse);
			customerForm.setUsername(username1);

			final Distributor d = this.distributorService.reconstruct(customerForm);

			Assert.notNull(d);
			this.distributorService.save(d);

			this.unauthenticate();
			this.distributorService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driver3() {

		final Object testingData[][] = {
			{//Un admin registra un manager
				"admin", "adress", "companyName", "email@gmail.com", "ESA12385678", "name", "123456", "123456", "+34955613568", "http://www.google.com", "surname", "managerPrueba", true, null
			}, {
				//Admin intenta registrar en el sistema con email invalido
				"admin", "adress", "companyName", "email", "ESA12385678", "name", "123456", "123456", "+34955613568", "http://www.google.com", "surname", "managerPrueba", true, DataIntegrityViolationException.class
			}, {
				//Admin intenta registrar en el sistema con vatNumber invalido
				"admin", "adress", "companyName", "email@gmail.com", "1245678", "name", "123456", "123456", "+34955613568", "http://www.google.com", "surname", "managerPrueba", true, DataIntegrityViolationException.class
			}, {
				//Admin intenta registrar manager con contraseñas diferentes
				"admin", "adress", "companyName", "email@gmail.com", "ESA12385678", "name", "789456", "123456", "+34955613568", "http://www.google.com", "surname", "managerPrueba", true, DataIntegrityViolationException.class
			}, {
				//Usuario no registrado se registra en el sistema con campos null
				//Un admin registra un manager
				"admin", "adress", "companyName", null, null, "name", "123456", "123456", "+34955613568", "http://www.google.com", "surname", null, true, DataIntegrityViolationException.class
			}, {
				//Usuario no registrado se registra en el sistema sin aceptar las condiciones de uso
				"admin", "adress", "companyName", "email@gmail.com", "ESA12385678", "name", "123456", "123456", "+34955613568", "http://www.google.com", "surname", "managerPrueba", false, DataIntegrityViolationException.class

			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (boolean) testingData[i][12], (Class<?>) testingData[i][13]);

	}
}
