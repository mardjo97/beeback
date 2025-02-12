package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Queen;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Queen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueenRepository extends JpaRepository<Queen, Long> {
  @Query("select queen from Queen queen where queen.user.login = ?#{authentication.name}")
  List<Queen> findByUserIsCurrentUser();

  Page<Queen> findDistinctByUser_id(Pageable pageable, Long userId);

  Queen findByUuidIs(String uuid);

  Queen findByUserAndExternalId(User user, Integer externalId);
}
