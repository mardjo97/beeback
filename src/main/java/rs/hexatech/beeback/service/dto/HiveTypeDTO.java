package rs.hexatech.beeback.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.HiveType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HiveTypeDTO implements Serializable {

  private Long id;

  @NotNull
  private String Name;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return Name;
  }

  public void setName(String Name) {
    this.Name = Name;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HiveTypeDTO)) {
      return false;
    }

    HiveTypeDTO hiveTypeDTO = (HiveTypeDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, hiveTypeDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "HiveTypeDTO{" +
        "id=" + getId() +
        ", Name='" + getName() + "'" +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        "}";
  }
}
