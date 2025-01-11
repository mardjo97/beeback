package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Apiary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApiaryRepository extends JpaRepository<Apiary, Long> {
  @Query("select apiary from Apiary apiary where apiary.user.login = ?#{authentication.name}")
  List<Apiary> findByUserIsCurrentUser();

  Page<Apiary> findDistinctByUser_id(Pageable pageable, Long userId);

  Apiary findByUuidIs(String uuid);

  Apiary findByUserAndExternalId(User user, Integer externalId);
}
