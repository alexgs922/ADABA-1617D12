
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.ShoppingGroup;

@Repository
public interface ShoppingGroupRepository extends JpaRepository<ShoppingGroup, Integer> {

}
