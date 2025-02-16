package rs.hexatech.beeback.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.ReproductionHive} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReproductionHiveDTO implements Serializable {

  private Long id;

  private String note;

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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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
    if (!(o instanceof ReproductionHiveDTO)) {
      return false;
    }

    ReproductionHiveDTO reproductionHiveDTO = (ReproductionHiveDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, reproductionHiveDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "ReproductionHiveDTO{" +
        "id=" + getId() +
        ", note='" + getNote() + "'" +
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
