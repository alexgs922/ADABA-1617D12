
package funcionalTesting;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.AdministratorService;
import services.ConfigurationService;
import utilities.AbstractTest;
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Test 2: Test Relacionado con la edición de configuracion

	protected void templateEditConfiguration(final String username, final Configuration con, final Double fee, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			con.setFee(fee);

			this.configurationService.saveAndFlush(con);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverEditConfiguration() {

		this.authenticate("admin");
		final Configuration configuration = this.configurationService.findOne(621);
		this.unauthenticate();

		final Object testingData[][] = {
			{   //admin editar la configuracion correctamente
				"admin", configuration, 3.0, null
			}, {
				//user 1 intenta editar la configuracion, el sistema no se lo permite.
				"user1", configuration, 2.0, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditConfiguration((String) testingData[i][0], (Configuration) testingData[i][1], (double) testingData[i][2], (Class<?>) testingData[i][3]);

	}
}
