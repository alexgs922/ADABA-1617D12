
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Punctuation;

@Repository
public interface PunctuationRepository extends JpaRepository<Punctuation, Integer> {

	@Query("select p from Punctuation p where p.shoppingGroup.id = ?1 and p.user.id = ?2")
	Punctuation getPunctuationByShoppingGroupAndUser(int shoppingGroupId, int userId);

}
