
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EngagementRepository;
import domain.Configuration;
import domain.Engagement;
import domain.OrderDomain;
import domain.Product;
import domain.User;

@Service
@Transactional
public class EngagementService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private EngagementRepository	engagementRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public EngagementService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Engagement create(final OrderDomain o) {
		Engagement result;

		result = new Engagement();

		Collection<String> col;
		Collection<User> users;
		col = new ArrayList<String>();
		users = new ArrayList<User>();

		for (final Product p : o.getProducts()) {
			String res_es;
			String res_en;
			String total;
			res_en = "Product: " + p.getName() + ", 	User: " + p.getUserProduct().getName() + ", 		Price:  " + p.getPrice();
			res_es = res_en = "Producto: " + p.getName() + ", 	Usuario: " + p.getUserProduct().getName() + ", 		Precio:  " + p.getPrice();
			total = res_es + "  //  " + res_en;
			col.add(total);
			if (!users.contains(p.getUserProduct()))
				users.add(p.getUserProduct());
		}

		final Configuration conf = this.configurationService.findConfiguration();

		final double fee = conf.getFee();

		final String fee_parts = "La cuota cargada son " + fee + " EUROS" + ", 	Al ser " + users.size() + " personas, son " + (fee / (users.size() * 1.0)) + " EUROS cada uno";
		final String fee_parts_en = "The fee is " + fee + " EUROS" + ", 	If there are " + users.size() + " users, there are " + (fee / (users.size() * 1.0)) + " EUROS each";
		final String fee_total = fee_parts + " // " + fee_parts_en;

		col.add(fee_total);

		final String totalAmount_es = "TOTAL --> " + o.getProducts().size() + " artículos por " + o.getTotalPrice() + " EUROS ";
		final String totalAmount_en = "TOTAL --> " + o.getProducts().size() + " items for " + o.getTotalPrice() + " EUROS ";

		final String totalAmount = totalAmount_es + " // " + totalAmount_en;

		col.add(totalAmount);

		if (o.getCoupon() != null) {
			final String coupons = "Se ha aplicado el cupón " + o.getCoupon().getCouponNumber() + " con un descuento del: " + o.getCoupon().getDiscount() * 100 + " porciento";
			final String coupons2 = "The coupon has been applied " + o.getCoupon().getCouponNumber() + " with a discount of : " + o.getCoupon().getDiscount() * 100 + " percent";
			final String couponTotal = coupons + " // " + coupons2;
			col.add(couponTotal);
		}

		result.setListOrdersByUser(col);
		result.setOrder(o);

		return result;
	}
	public Collection<Engagement> findAll() {
		Collection<Engagement> res;
		res = this.engagementRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Engagement findOne(final int engagementId) {
		Engagement res;
		res = this.engagementRepository.findOne(engagementId);
		Assert.notNull(res);
		return res;
	}

	public Engagement save(final Engagement e) {
		Assert.notNull(e);
		return this.engagementRepository.save(e);

	}

	public void delete(final Engagement e) {
		Assert.notNull(e);
		this.engagementRepository.delete(e);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.engagementRepository.flush();
	}
}
