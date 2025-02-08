package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Hive;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Hive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HiveRepository extends JpaRepository<Hive, Long> {
  @Query("select hive from Hive hive where hive.user.login = ?#{authentication.name}")
  List<Hive> findByUserIsCurrentUser();

  Page<Hive> findDistinctByUser_id(Pageable pageable, Long userId);

  Hive findByUuidIs(String uuid);

  Hive findByUserAndExternalId(User user, Integer externalId);
}
