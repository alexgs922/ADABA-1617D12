
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

	@Query("select s from ShoppingGroup s where s.id in (select sh.id from ShoppingGroup sh where sh.private_group=false) or s.id in (select sh1.id from ShoppingGroup sh1 join sh1.users u where u.id=?1)")
	Collection<ShoppingGroup> listPublicForUsersOfSH(int userId);

	@Query("select sh from User u join u.shoppingGroup sh where u.id=?1 and sh.creator!=?1")
	Collection<ShoppingGroup> ShoppingGroupsToWichBelongsAndNotCreatedBy(int userId);

	//Dashboard

	@Query("select sg from ShoppingGroup sg where sg.puntuation >= All(select sg2.puntuation from ShoppingGroup sg2)")
	Collection<ShoppingGroup> shoppingGroupsWithMorePuntuation();

	@Query("select sg from ShoppingGroup sg where sg.puntuation <= All(select sg2.puntuation from ShoppingGroup sg2)")
	Collection<ShoppingGroup> shoppingGroupsWithLessPuntuation();

	@Query("select count(c1)*100.0/(select count(c)*1.0 from ShoppingGroup c)from ShoppingGroup c1 where c1.freePlaces>0")
	Double percentShoppingGroupsWithFreePlaces();

	@Query("select count(c1)*100.0/(select count(c)*1.0 from ShoppingGroup c)from ShoppingGroup c1 where c1.freePlaces<=0")
	Double percentShoppingGroupsWithoutFreePlaces();

}
