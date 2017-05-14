
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.PuntuableEntity;

@Repository
public interface PuntuableEntityRepository extends JpaRepository<PuntuableEntity, Integer> {

}
