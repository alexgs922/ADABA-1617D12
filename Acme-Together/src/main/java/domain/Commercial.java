
package domain;

import java.util.Collection;

import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class Commercial extends Actor {

	//Constructors -------------------------------------

	public Commercial() {
		super();
	}


	//Attributes ------------------------------------

	private String	companyName;
	private String	vatNumber;


	//Getters and Setters --------------------------

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	@NotBlank
	@Pattern(regexp = "^ES[ABCDEFGHJNPQRSUVW]{1}[1-9]{8}$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(final String vatNumber) {
		this.vatNumber = vatNumber;
	}


	private Collection<Coupon>	coupon;


	@Valid
	@NotNull
	@OneToMany
	public Collection<Coupon> getCoupon() {
		return this.coupon;
	}

	public void setCoupon(final Collection<Coupon> coupon) {
		this.coupon = coupon;
	}

}
