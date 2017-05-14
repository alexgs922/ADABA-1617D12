
package domain;

import java.util.Collection;

import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

public class Distributor extends Actor {

	//Constructors -------------------------------------

	public Distributor() {
		super();
	}


	//Attributes ------------------------------------

	private String	companyName;
	private String	vatNumber;
	private String	companyAddress;
	private String	webPage;


	//Getters and Setters --------------------------

	@NotBlank
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

	@NotBlank
	public String getCompanyAddress() {
		return this.companyAddress;
	}

	public void setCompanyAddress(final String companyAddress) {
		this.companyAddress = companyAddress;
	}

	@NotBlank
	@URL
	public String getWebPage() {
		return this.webPage;
	}

	public void setWebPage(final String webPage) {
		this.webPage = webPage;
	}

	private Collection<Warehouse> warehouse;

	@OneToMany
	@Valid
	@NotNull
	public Collection<Warehouse> getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Collection<Warehouse> warehouse) {
		this.warehouse = warehouse;
	}
	
	
	
}
