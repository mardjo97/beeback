package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.MovedHive;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the MovedHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovedHiveRepository extends JpaRepository<MovedHive, Long> {
  @Query("select movedHive from MovedHive movedHive where movedHive.user.login = ?#{authentication.name}")
  List<MovedHive> findByUserIsCurrentUser();

  Page<MovedHive> findDistinctByUser_id(Pageable pageable, Long userId);

  MovedHive findByUuidIs(String uuid);

  MovedHive findByUserAndExternalId(User user, Integer externalId);
}
