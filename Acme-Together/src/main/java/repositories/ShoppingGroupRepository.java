
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ShoppingGroup;

@Repository
public interface ShoppingGroupRepository extends JpaRepository<ShoppingGroup, Integer> {

	@Query("select s from ShoppingGroup s where s.category.id = ?1")
	Collection<ShoppingGroup> findShoppingGroupsByCategory(int categoryId);

}
