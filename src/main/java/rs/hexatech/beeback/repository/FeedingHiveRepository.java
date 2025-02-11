package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.FeedingHive;

/**
 * Spring Data JPA repository for the FeedingHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedingHiveRepository extends JpaRepository<FeedingHive, Long> {
    @Query("select feedingHive from FeedingHive feedingHive where feedingHive.user.login = ?#{authentication.name}")
    List<FeedingHive> findByUserIsCurrentUser();
}
