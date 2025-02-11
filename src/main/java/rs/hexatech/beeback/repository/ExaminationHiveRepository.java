package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.ExaminationHive;

/**
 * Spring Data JPA repository for the ExaminationHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExaminationHiveRepository extends JpaRepository<ExaminationHive, Long> {
    @Query("select examinationHive from ExaminationHive examinationHive where examinationHive.user.login = ?#{authentication.name}")
    List<ExaminationHive> findByUserIsCurrentUser();
}
