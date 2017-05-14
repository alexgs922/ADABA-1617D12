
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

}
