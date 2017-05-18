
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	//Constructors -------------------------------------

	public User() {
		super();
	}


	//Attributes ------------------------------------

	private String	address;
	private String	picture;
	private String	description;
	private Date	birthDate;
	private String	identification;
	private int		puntuation;
	private boolean	banned;


	//Getters and Setters --------------------------

	public boolean isBanned() {
		return this.banned;
	}

	public void setBanned(final boolean banned) {
		this.banned = banned;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@URL
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	@NotBlank
	@Pattern(regexp = "^(([X-Z]{1})([-]?)(\\d{7})([-]?)([A-Z]{1}))|((\\d{8})([-]?)([A-Z]{1}))")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getIdentification() {
		return this.identification;
	}

	public void setIdentification(final String identification) {
		this.identification = identification;
	}

	@NotNull
	public int getPuntuation() {
		return this.puntuation;
	}

	public void setPuntuation(final int puntuation) {
		this.puntuation = puntuation;
	}


	//Relationships

	private CreditCard					creditCard;
	private Collection<User>			friends;
	private Collection<ShoppingGroup>	shoppingGroup;
	private Collection<ShoppingGroup>	myShoppingGroups;
	private Collection<Product>			products;
	private Collection<Comment>			comments;


	@OneToMany(mappedBy = "creator")
	public Collection<ShoppingGroup> getMyShoppingGroups() {
		return this.myShoppingGroups;
	}

	public void setMyShoppingGroups(final Collection<ShoppingGroup> myShoppingGroups) {
		this.myShoppingGroups = myShoppingGroups;
	}

	@OneToOne
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@ManyToMany
	public Collection<User> getFriends() {
		return this.friends;
	}

	public void setFriends(final Collection<User> friends) {
		this.friends = friends;
	}

	public void follow(final User user) {
		this.friends.add(user);
	}

	public void unfollow(final User user) {
		this.friends.remove(user);
	}

	@ManyToMany
	public Collection<ShoppingGroup> getShoppingGroup() {
		return this.shoppingGroup;
	}

	public void setShoppingGroup(final Collection<ShoppingGroup> shoppingGroup) {
		this.shoppingGroup = shoppingGroup;
	}

	@OneToMany(mappedBy = "userProduct")
	public Collection<Product> getProducts() {
		return this.products;
	}

	public void setProducts(final Collection<Product> products) {
		this.products = products;
	}

	@OneToMany(mappedBy = "userComment")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

}
