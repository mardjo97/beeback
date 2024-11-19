package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.domain.User;

/**
 * Spring Data JPA repository for the Apiary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiaryRepository extends JpaRepository<Apiary, Long> {
    @Query("select apiary from Apiary apiary where apiary.user.login = ?#{authentication.name}")
    List<Apiary> findByUserIsCurrentUser();

    Apiary findByUuidIs(String uuid);
    Apiary findByUserAndExternalId(User user, Integer externalId);
}
