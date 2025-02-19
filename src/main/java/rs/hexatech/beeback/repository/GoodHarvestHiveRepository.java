package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.GoodHarvestHive;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the GoodHarvestHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodHarvestHiveRepository extends JpaRepository<GoodHarvestHive, Long> {
  @Query("select goodHarvestHive from GoodHarvestHive goodHarvestHive where goodHarvestHive.user.login = ?#{authentication.name}")
  List<GoodHarvestHive> findByUserIsCurrentUser();

  Page<GoodHarvestHive> findDistinctByUser_id(Pageable pageable, Long userId);

  GoodHarvestHive findByUuidIs(String uuid);

  GoodHarvestHive findByUserAndExternalId(User user, Integer externalId);
}
