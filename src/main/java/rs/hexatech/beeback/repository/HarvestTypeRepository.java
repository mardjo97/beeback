package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the HarvestType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HarvestTypeRepository extends JpaRepository<HarvestType, Long> {
  @Query("select harvestType from HarvestType harvestType where harvestType.user.login = ?#{authentication.name}")
  List<HarvestType> findByUserIsCurrentUser();

  List<HarvestType> findByUserIsNull();

  Page<HarvestType> findDistinctByUser_id(Pageable pageable, Long userId);

  //  HarvestType findByUuidIs(String uuid);
  @Query("select harvestType from HarvestType harvestType where harvestType.uuid = :uuid")
  HarvestType findByUuidIs(@Param("uuid") String uuid);

  HarvestType findByUserAndExternalId(User user, Integer externalId);
}
