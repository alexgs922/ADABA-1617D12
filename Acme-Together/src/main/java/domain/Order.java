
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class Order extends DomainEntity {

	//Constructors -------------------------------------

	public Order() {
		super();
	}


	//Attributes ------------------------------------

	private Date	initDate;
	private Date	finishDate;
	private Status	status;


	//Getters and Setters --------------------------

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getInitDate() {
		return this.initDate;
	}

	public void setInitDate(final Date initDate) {
		this.initDate = initDate;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
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


	// Relationships

	private Collection<Product>	products;
	private Coupon				coupon;


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
