package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.ExaminationHive;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the ExaminationHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExaminationHiveRepository extends JpaRepository<ExaminationHive, Long> {
  @Query("select examinationHive from ExaminationHive examinationHive where examinationHive.user.login = ?#{authentication.name}")
  List<ExaminationHive> findByUserIsCurrentUser();

  Page<ExaminationHive> findDistinctByUser_id(Pageable pageable, Long userId);

  ExaminationHive findByUuidIs(String uuid);

  ExaminationHive findByUserAndExternalId(User user, Integer externalId);
}
