package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Group;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Group entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
  @Query("select jhiGroup from Group jhiGroup where jhiGroup.user.login = ?#{authentication.name}")
  List<Group> findByUserIsCurrentUser();

  List<Group> findByUserIsNull();

  Page<Group> findDistinctByUser_id(Pageable pageable, Long userId);

  Group findByUuidIs(String uuid);

  Group findByUserAndExternalId(User user, Integer externalId);
}
