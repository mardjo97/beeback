package rs.hexatech.beeback.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.QueenChangeHive} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QueenChangeHiveDTO implements Serializable {

  private Long id;

  private Instant dateQueenChange;

  private String reminderId;

  @NotNull
  private String uuid;

  @NotNull
  private Instant dateCreated;

  @NotNull
  private Instant dateModified;

  @NotNull
  private Instant dateSynched;

  private Instant dateDeleted;

  private Instant dateFinished;

  private HiveDTO hive;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getDateQueenChange() {
    return dateQueenChange;
  }

  public void setDateQueenChange(Instant dateQueenChange) {
    this.dateQueenChange = dateQueenChange;
  }

  public String getReminderId() {
    return reminderId;
  }

  public void setReminderId(String reminderId) {
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

  public Instant getDateFinished() {
    return dateFinished;
  }

  public void setDateFinished(Instant dateFinished) {
    this.dateFinished = dateFinished;
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
    if (!(o instanceof QueenChangeHiveDTO)) {
      return false;
    }

    QueenChangeHiveDTO queenChangeHiveDTO = (QueenChangeHiveDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, queenChangeHiveDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "QueenChangeHiveDTO{" +
        "id=" + getId() +
        ", dateQueenChange='" + getDateQueenChange() + "'" +
        ", reminderId='" + getReminderId() + "'" +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        ", dateFinished='" + getDateFinished() + "'" +
        ", hive=" + getHive() +
        "}";
  }
}
