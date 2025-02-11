package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.GoodHarvestHive;

/**
 * Spring Data JPA repository for the GoodHarvestHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodHarvestHiveRepository extends JpaRepository<GoodHarvestHive, Long> {
    @Query("select goodHarvestHive from GoodHarvestHive goodHarvestHive where goodHarvestHive.user.login = ?#{authentication.name}")
    List<GoodHarvestHive> findByUserIsCurrentUser();
}
