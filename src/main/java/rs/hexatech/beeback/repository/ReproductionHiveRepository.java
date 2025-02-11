package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.ReproductionHive;

/**
 * Spring Data JPA repository for the ReproductionHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReproductionHiveRepository extends JpaRepository<ReproductionHive, Long> {
    @Query("select reproductionHive from ReproductionHive reproductionHive where reproductionHive.user.login = ?#{authentication.name}")
    List<ReproductionHive> findByUserIsCurrentUser();
}
