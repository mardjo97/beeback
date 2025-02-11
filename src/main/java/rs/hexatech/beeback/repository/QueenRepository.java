package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Queen;

/**
 * Spring Data JPA repository for the Queen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueenRepository extends JpaRepository<Queen, Long> {
    @Query("select queen from Queen queen where queen.user.login = ?#{authentication.name}")
    List<Queen> findByUserIsCurrentUser();
}
