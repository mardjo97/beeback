package rs.hexatech.beeback.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.Note} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NoteDTO implements Serializable {

  private Long id;

  private Boolean hasReminder;

  private String title;

  private String content;

  private String group;

  private Integer groupRecordId;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant dateHidden;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant reminderDate;

  private Integer reminderId;

  @NotNull
  private String uuid;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant dateCreated;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant dateModified;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant dateSynched;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant dateDeleted;

  private HiveDTO hive;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getHasReminder() {
    return hasReminder;
  }

  public void setHasReminder(Boolean hasReminder) {
    this.hasReminder = hasReminder;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public Integer getGroupRecordId() {
    return groupRecordId;
  }

  public void setGroupRecordId(Integer groupRecordId) {
    this.groupRecordId = groupRecordId;
  }

  public Instant getDateHidden() {
    return dateHidden;
  }

  public void setDateHidden(Instant dateHidden) {
    this.dateHidden = dateHidden;
  }

  public Instant getReminderDate() {
    return reminderDate;
  }

  public void setReminderDate(Instant reminderDate) {
    this.reminderDate = reminderDate;
  }

  public Integer getReminderId() {
    return reminderId;
  }

  public void setReminderId(Integer reminderId) {
    this.reminderId = reminderId;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Instant getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Instant dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Instant getDateModified() {
    return dateModified;
  }

  public void setDateModified(Instant dateModified) {
    this.dateModified = dateModified;
  }

  public Instant getDateSynched() {
    return dateSynched;
  }

  public void setDateSynched(Instant dateSynched) {
    this.dateSynched = dateSynched;
  }

  public Instant getDateDeleted() {
    return dateDeleted;
  }

  public void setDateDeleted(Instant dateDeleted) {
    this.dateDeleted = dateDeleted;
  }

  public HiveDTO getHive() {
    return hive;
  }

  public void setHive(HiveDTO hive) {
    this.hive = hive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NoteDTO)) {
      return false;
    }

    NoteDTO noteDTO = (NoteDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, noteDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "NoteDTO{" +
        "id=" + getId() +
        ", hasReminder='" + getHasReminder() + "'" +
        ", title='" + getTitle() + "'" +
        ", content='" + getContent() + "'" +
        ", group='" + getGroup() + "'" +
        ", groupRecordId=" + getGroupRecordId() +
        ", dateHidden='" + getDateHidden() + "'" +
        ", reminderDate='" + getReminderDate() + "'" +
        ", reminderId=" + getReminderId() +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        ", hive=" + getHive() +
        "}";
  }
}
