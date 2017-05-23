
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ShoppingGroup;
import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userAccount.id = ?1")
	User findByUserAccountId(int userAccountId);

	@Query("select c from User c where c.banned=false")
	Collection<User> findAllNotBannedUsers();

	//user 1222 hasta 1226
	@Query("select c.friends from User c where c.id = ?1")
	Collection<User> findAllMyFriends(int userId);

	@Query("select c from User u join u.shoppingGroup c where c.private_group=false and u.id = ?1")
	Collection<ShoppingGroup> findAllShoppingGroupsNoPrivate(int userId);

}
