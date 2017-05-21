
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class OrderDomain extends DomainEntity {

	//Constructors -------------------------------------

	public OrderDomain() {
		super();
	}


	//Attributes ------------------------------------

	private Date	initDate;
	private Date	finishDate;
	private Status	status;
	private double	totalPrice;


	//Getters and Setters --------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getInitDate() {
		return this.initDate;
	}

	public void setInitDate(final Date initDate) {
		this.initDate = initDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getFinishDate() {
		return this.finishDate;
	}

	public void setFinishDate(final Date finishDate) {
		this.finishDate = finishDate;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	@Min(0)
	@Digits(integer = 32, fraction = 2)
	public double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(final double totalPrice) {
		this.totalPrice = totalPrice;
	}


	// Relationships

	private Collection<Product>	products;
	private Coupon				coupon;
	private Engagement			engagement;


	@Valid
	@OneToOne(optional = true)
	public Engagement getEngagement() {
		return this.engagement;
	}

	public void setEngagement(final Engagement engagement) {
		this.engagement = engagement;
	}

	@ManyToOne(optional = true)
	@Valid
	public Coupon getCoupon() {
		return this.coupon;
	}

	public void setCoupon(final Coupon coupon) {
		this.coupon = coupon;
	}

	@OneToMany(mappedBy = "orderProduct")
	@Valid
	@NotNull
	public Collection<Product> getProducts() {
		return this.products;
	}

	public void setProducts(final Collection<Product> products) {
		this.products = products;
	}

}
