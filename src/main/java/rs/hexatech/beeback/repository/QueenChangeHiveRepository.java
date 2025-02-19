package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.QueenChangeHive;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the QueenChangeHive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueenChangeHiveRepository extends JpaRepository<QueenChangeHive, Long> {
  @Query("select queenChangeHive from QueenChangeHive queenChangeHive where queenChangeHive.user.login = ?#{authentication.name}")
  List<QueenChangeHive> findByUserIsCurrentUser();

  Page<QueenChangeHive> findDistinctByUser_id(Pageable pageable, Long userId);

  QueenChangeHive findByUuidIs(String uuid);

  QueenChangeHive findByUserAndExternalId(User user, Integer externalId);
}
