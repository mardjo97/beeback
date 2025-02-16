package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.HiveLocation;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the HiveLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HiveLocationRepository extends JpaRepository<HiveLocation, Long> {
  @Query("select hiveLocation from HiveLocation hiveLocation where hiveLocation.user.login = ?#{authentication.name}")
  List<HiveLocation> findByUserIsCurrentUser();

  List<HiveLocation> findByUserIsNull();

  Page<HiveLocation> findDistinctByUser_id(Pageable pageable, Long userId);

  HiveLocation findByUuidIs(String uuid);

  HiveLocation findByUserAndExternalId(User user, Integer externalId);
}
