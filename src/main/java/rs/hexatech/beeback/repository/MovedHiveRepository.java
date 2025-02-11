package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.MovedHive;

/**
 * Spring Data JPA repository for the MovedHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovedHiveRepository extends JpaRepository<MovedHive, Long> {
    @Query("select movedHive from MovedHive movedHive where movedHive.user.login = ?#{authentication.name}")
    List<MovedHive> findByUserIsCurrentUser();
}
