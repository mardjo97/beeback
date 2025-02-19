package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.FeedingHive;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the FeedingHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedingHiveRepository extends JpaRepository<FeedingHive, Long> {
  @Query("select feedingHive from FeedingHive feedingHive where feedingHive.user.login = ?#{authentication.name}")
  List<FeedingHive> findByUserIsCurrentUser();

  Page<FeedingHive> findDistinctByUser_id(Pageable pageable, Long userId);

  FeedingHive findByUuidIs(String uuid);

  FeedingHive findByUserAndExternalId(User user, Integer externalId);
}
