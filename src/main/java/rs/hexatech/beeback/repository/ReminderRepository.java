package rs.hexatech.beeback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Reminder;
import rs.hexatech.beeback.domain.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

  @Query("select r from Reminder r where r.scheduledAt <= ?1")
  List<Reminder> findByScheduledAtBefore(Instant instant);

  @Query("select r from Reminder r where r.user.login = ?#{authentication.name}")
  List<Reminder> findByUserIsCurrentUser();

  Optional<Reminder> findByIdAndUser(Long id, User user);
}
