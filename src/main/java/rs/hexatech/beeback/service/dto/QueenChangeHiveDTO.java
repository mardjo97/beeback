package rs.hexatech.beeback.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  private Integer externalId;

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

  private UserDTO user;

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

  public Integer getExternalId() {
    return externalId;
  }

  public void setExternalId(Integer externalId) {
    this.externalId = externalId;
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

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
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
        ", externalId=" + getExternalId() +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        ", user=" + getUser() +
        "}";
  }
}
