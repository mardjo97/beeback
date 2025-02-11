package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Group;

/**
 * Spring Data JPA repository for the Group entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select jhiGroup from Group jhiGroup where jhiGroup.user.login = ?#{authentication.name}")
    List<Group> findByUserIsCurrentUser();
}
