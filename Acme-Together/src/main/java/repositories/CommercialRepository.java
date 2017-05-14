
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Commercial;

@Repository
public interface CommercialRepository extends JpaRepository<Commercial, Integer> {

	@Query("select c from Commercial c where c.userAccount.id = ?1")
	Commercial findByUserAccountId(int userAccountId);
}
