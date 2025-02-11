package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.HarvestType;

/**
 * Spring Data JPA repository for the HarvestType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HarvestTypeRepository extends JpaRepository<HarvestType, Long> {
    @Query("select harvestType from HarvestType harvestType where harvestType.user.login = ?#{authentication.name}")
    List<HarvestType> findByUserIsCurrentUser();
}
