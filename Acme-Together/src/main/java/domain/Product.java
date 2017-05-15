
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Product extends DomainEntity {

	//Constructors -------------------------------------

	public Product() {
		super();
	}


	//Attributes ------------------------------------

	private String	name;
	private String	url;
	private String	referenceNumber;
	private double	price;


	//Getters and Setters --------------------------

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@URL
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getReferenceNumber() {
		return this.referenceNumber;
	}

	public void setReferenceNumber(final String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	@NotBlank
	@Min(0)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}


	//Relationship

	private ShoppingGroup	shoppingGroupProducts;
	private Order			orderProduct;
	private User			userProduct;


	@ManyToOne(optional = true)
	@Valid
	@NotNull
	public Order getOrderProduct() {
		return this.orderProduct;
	}

	public void setOrderProduct(final Order orderProduct) {
		this.orderProduct = orderProduct;
	}

	@ManyToOne(optional = true)
	@Valid
	@NotNull
	public User getUserProduct() {
		return this.userProduct;
	}

	public void setUserProduct(final User userProduct) {
		this.userProduct = userProduct;
	}

	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public ShoppingGroup getShoppingGroupProducts() {
		return this.shoppingGroupProducts;
	}

	public void setShoppingGroupProducts(final ShoppingGroup shoppingGroupProducts) {
		this.shoppingGroupProducts = shoppingGroupProducts;
	}

}
