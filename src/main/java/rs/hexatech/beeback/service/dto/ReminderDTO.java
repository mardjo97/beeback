package rs.hexatech.beeback.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for Reminder (create/update from Flutter: uuid, title, body, scheduledAt, hiveId?, groupRecordId?).
 */
public class ReminderDTO implements Serializable {

  private Long id;

  @NotNull
  private String uuid;

  private String title;

  private String body;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
  private Instant scheduledAt;

  private Long hiveId;

  private Integer groupRecordId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Long getHiveId() {
    return hiveId;
  }

  public void setHiveId(Long hiveId) {
    this.hiveId = hiveId;
  }

  public Integer getGroupRecordId() {
    return groupRecordId;
  }

  public void setGroupRecordId(Integer groupRecordId) {
    this.groupRecordId = groupRecordId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReminderDTO)) return false;
    ReminderDTO that = (ReminderDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
