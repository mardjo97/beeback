package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.QueenChangeHive;

/**
 * Spring Data JPA repository for the QueenChangeHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueenChangeHiveRepository extends JpaRepository<QueenChangeHive, Long> {
    @Query("select queenChangeHive from QueenChangeHive queenChangeHive where queenChangeHive.user.login = ?#{authentication.name}")
    List<QueenChangeHive> findByUserIsCurrentUser();
}
