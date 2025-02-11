package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.HiveLocation;

/**
 * Spring Data JPA repository for the HiveLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HiveLocationRepository extends JpaRepository<HiveLocation, Long> {
    @Query("select hiveLocation from HiveLocation hiveLocation where hiveLocation.user.login = ?#{authentication.name}")
    List<HiveLocation> findByUserIsCurrentUser();
}
