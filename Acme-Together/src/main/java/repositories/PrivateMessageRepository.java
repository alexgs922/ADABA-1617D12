
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PrivateMessage;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Integer> {

	@Query("select m from Actor c join c.messageReceives m where m.copy=false and c.id=?1")
	Collection<PrivateMessage> myRecivedMessages(int actorId);

	@Query("select m from Actor c join c.messageWrites m where m.copy=true and c.id=?1")
	Collection<PrivateMessage> mySendedMessages(int actorId);

}
