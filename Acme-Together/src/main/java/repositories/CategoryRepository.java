
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;
import domain.ShoppingGroup;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select sh from ShoppingGroup sh where sh.category.id=?1")
	Collection<ShoppingGroup> shFromCategory(int categoryId);

}
