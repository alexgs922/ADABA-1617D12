
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.OrderDomain;

@Repository
public interface OrderRepository extends JpaRepository<OrderDomain, Integer> {

}
