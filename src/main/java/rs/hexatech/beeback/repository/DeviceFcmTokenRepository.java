package rs.hexatech.beeback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.DeviceFcmToken;

@Repository
public interface DeviceFcmTokenRepository extends JpaRepository<DeviceFcmToken, String> {
}
