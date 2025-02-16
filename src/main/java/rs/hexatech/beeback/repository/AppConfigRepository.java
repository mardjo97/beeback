package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.AppConfig;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the AppConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
  @Query("select appConfig from AppConfig appConfig where appConfig.user.login = ?#{authentication.name}")
  List<AppConfig> findByUserIsCurrentUser();

  List<AppConfig> findByUserIsNull();

  Page<AppConfig> findDistinctByUser_id(Pageable pageable, Long userId);

  AppConfig findByUuidIs(String uuid);

  AppConfig findByUserAndExternalId(User user, Integer externalId);
}
