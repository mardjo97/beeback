package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.ReproductionHive;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the ReproductionHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReproductionHiveRepository extends JpaRepository<ReproductionHive, Long> {
  @Query("select reproductionHive from ReproductionHive reproductionHive where reproductionHive.user.login = ?#{authentication.name}")
  List<ReproductionHive> findByUserIsCurrentUser();

  Page<ReproductionHive> findDistinctByUser_id(Pageable pageable, Long userId);

  ReproductionHive findByUuidIs(String uuid);

  ReproductionHive findByUserAndExternalId(User user, Integer externalId);
}
