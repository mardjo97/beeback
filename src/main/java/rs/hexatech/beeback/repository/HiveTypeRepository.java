package rs.hexatech.beeback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.HiveType;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the HiveType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HiveTypeRepository extends JpaRepository<HiveType, Long> {
  @Query("select hiveType from HiveType hiveType where hiveType.user.login = ?#{authentication.name}")
  List<HiveType> findByUserIsCurrentUser();

  List<HiveType> findByUserIsNull();

  HiveType findByUuidIs(String uuid);

  HiveType findByUserAndExternalId(User user, Integer externalId);
}
