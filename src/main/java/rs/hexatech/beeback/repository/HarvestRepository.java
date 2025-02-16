package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Harvest;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Harvest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
  @Query("select harvest from Harvest harvest where harvest.user.login = ?#{authentication.name}")
  List<Harvest> findByUserIsCurrentUser();

  Page<Harvest> findDistinctByUser_id(Pageable pageable, Long userId);

  Harvest findByUuidIs(String uuid);

  Harvest findByUserAndExternalId(User user, Integer externalId);
}
