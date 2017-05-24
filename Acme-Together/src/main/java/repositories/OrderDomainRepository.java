
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.OrderDomain;

@Repository
public interface OrderDomainRepository extends JpaRepository<OrderDomain, Integer> {

	//Dashboard
	@Query("select count(o) from OrderDomain o where MONTH(o.finishDate) = MONTH(CURRENT_DATE)-1 and o.finishDate is not null")
	Double numberOfOrderLastMonth();

}
