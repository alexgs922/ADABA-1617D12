
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userAccount.id = ?1")
	User findByUserAccountId(int userAccountId);

	@Query("select c from User c where c.banned=false")
	Collection<User> findAllNotBannedUsers();

	//user 1150 hasta 1154
	@Query("select c.friends from User c where c.id = ?1")
	Collection<User> findAllMyFriends(int userId);
}
