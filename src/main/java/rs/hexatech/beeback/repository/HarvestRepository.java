package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Harvest;

/**
 * Spring Data JPA repository for the Harvest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    @Query("select harvest from Harvest harvest where harvest.user.login = ?#{authentication.name}")
    List<Harvest> findByUserIsCurrentUser();
}
