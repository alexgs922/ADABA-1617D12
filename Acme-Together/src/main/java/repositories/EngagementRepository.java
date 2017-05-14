
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Engagement;

@Repository
public interface EngagementRepository extends JpaRepository<Engagement, Integer> {

}
