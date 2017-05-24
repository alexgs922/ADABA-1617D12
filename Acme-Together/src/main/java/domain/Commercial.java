
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
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


	//Relationships

	private Collection<Coupon>	coupons;


	@OneToMany(mappedBy = "commercial")
	public Collection<Coupon> getCoupons() {
		return this.coupons;
	}

	public void setCoupons(final Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

}
