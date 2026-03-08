package rs.hexatech.beeback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Reminder for push notification at scheduled_at (UTC).
 */
@Entity
@Table(name = "reminder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reminder implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotNull
  @Column(name = "device_id", nullable = false, length = 100)
  private String deviceId;

  @NotNull
  @Column(name = "uuid", nullable = false, length = 255)
  private String uuid;

  @Column(name = "title", length = 255)
  private String title;

  @Column(name = "body", length = 1024)
  private String body;

  @NotNull
  @Column(name = "scheduled_at", nullable = false)
  private Instant scheduledAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hive_id")
  @JsonIgnoreProperties(value = {"user", "hiveType", "apiary"}, allowSetters = true)
  private Hive hive;

  @Column(name = "group_record_id")
  private Integer groupRecordId;

  @NotNull
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @NotNull
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Instant getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(Instant scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  public Hive getHive() {
    return hive;
  }

  public void setHive(Hive hive) {
    this.hive = hive;
  }

  public Integer getGroupRecordId() {
    return groupRecordId;
  }

  public void setGroupRecordId(Integer groupRecordId) {
    this.groupRecordId = groupRecordId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
