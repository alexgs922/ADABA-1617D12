
package domain;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class Comment extends DomainEntity {

	//Constructors -------------------------------------

	public Comment() {
		super();
	}


	//Attributes ------------------------------------

	private String	title;
	private String	text;
	private Date	moment;


	//Getters and Setters --------------------------

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}
	
	private ShoppingGroup shoppingGroupComments;

	@ManyToOne(optional=false)
	@Valid
	@NotNull
	public ShoppingGroup getShoppingGroupComments() {
		return shoppingGroupComments;
	}

	public void setShoppingGroupComments(ShoppingGroup shoppingGroupComments) {
		this.shoppingGroupComments = shoppingGroupComments;
	}
	
	

}
