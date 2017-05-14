
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Distributor;

@Repository
public interface DistributorRepository extends JpaRepository<Distributor, Integer> {

	@Query("select d from Distributor d where d.userAccount.id = ?1")
	Distributor findByUserAccountId(int userAccountId);
}
