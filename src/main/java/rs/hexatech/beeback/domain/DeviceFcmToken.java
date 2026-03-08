package rs.hexatech.beeback.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;

/**
 * Stores FCM token per device (device_id from Device-Id header).
 */
@Entity
@Table(name = "device_fcm_token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DeviceFcmToken implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "device_id", length = 100)
  private String deviceId;

  @NotNull
  @Column(name = "fcm_token", nullable = false, length = 512)
  private String fcmToken;

  @NotNull
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getFcmToken() {
    return fcmToken;
  }

  public void setFcmToken(String fcmToken) {
    this.fcmToken = fcmToken;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
