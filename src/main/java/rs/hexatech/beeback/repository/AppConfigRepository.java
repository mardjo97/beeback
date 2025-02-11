package rs.hexatech.beeback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.AppConfig;

/**
 * Spring Data JPA repository for the AppConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
    @Query("select appConfig from AppConfig appConfig where appConfig.user.login = ?#{authentication.name}")
    List<AppConfig> findByUserIsCurrentUser();
}
