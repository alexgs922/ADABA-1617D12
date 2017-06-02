
package funcionalTesting;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.CommercialService;
import services.CouponService;
import utilities.AbstractTest;
import domain.Commercial;
import domain.Coupon;
import domain.OrderDomain;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CouponServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private CommercialService	commercialService;

	@Autowired
	private CouponService		couponService;


	// Test 1: Test Relacionado con la creación de cupones 

	protected void templateCreateCoupon(final String username, final String couponNumber, final double discount, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Coupon coupon = this.couponService.create();
			final Commercial commercial = this.commercialService.findByPrincipal();
			final Collection<OrderDomain> orders = new ArrayList<OrderDomain>();

			coupon.setCouponNumber(couponNumber);
			coupon.setDiscount(discount);
			coupon.setCommercial(commercial);
			coupon.setOrders(orders);

			this.couponService.saveAndFlush(coupon);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverCreateCoupon() {

		final Object testingData[][] = {
			{   //comercial crea un cupon correctamente
				"commercial1", "descuento1", 0.8, null
			}, {
				//user 1 intenta crear un cupon, el sistema no se lo permite.
				"user1", "descuento2", 0.2, IllegalArgumentException.class
			}, {
				//commercial intenta crear una cupon con los campos vacíos
				"commercial1", "", 0.3, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateCoupon((String) testingData[i][0], (String) testingData[i][1], (double) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// Test 2: Test Relacionado con la edición de cupones

	protected void templateEditCoupon(final String username, final Coupon c, final String couponNumber, final double discount, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			c.setCouponNumber(couponNumber);
			c.setDiscount(discount);

			this.couponService.saveEdit(c);
			this.couponService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverEditCoupon() {

		this.authenticate("commercial1");
		final Coupon coupon = this.couponService.findOne(561);
		this.unauthenticate();

		final Object testingData[][] = {
			{   //commercial editar una coupon correctamente
				"commercial1", coupon, "discount5", 0.1, null
			}, {
				//user 1 intenta editar una coupon, el sistema no se lo permite.
				"user1", coupon, "coupon2", 0.9, NullPointerException.class
			}, {
				//commercial intenta editar una cupon con los campos vacíos
				"commercial1", coupon, "", 0.2, ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditCoupon((String) testingData[i][0], (Coupon) testingData[i][1], (String) testingData[i][2], (double) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Test 3: Test relacionado con la eliminación de un cupon

	protected void templateDeleteCoupon(final String username, final Coupon coupon, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			this.couponService.delete(coupon);
			this.couponService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	@Test
	public void driverDeleteCoupon() {

		this.authenticate("commercial1");
		final Coupon coupon = this.couponService.findOne(561);
		this.unauthenticate();

		final Object testingData[][] = {
			{   //commercial borra un cupon correctamente
				"commercial1", coupon, null
			}, {
				//user 1 intenta borrar un cupon, el sistema no se lo permite.
				"commercial3", coupon, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteCoupon((String) testingData[i][0], (Coupon) testingData[i][1], (Class<?>) testingData[i][2]);

	}
}
