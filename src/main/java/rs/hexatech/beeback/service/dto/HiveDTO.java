package rs.hexatech.beeback.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.Hive} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HiveDTO implements Serializable {

  private Long id;

  @NotNull
  private String barcode;

  private Integer orderNumber;

  private String description;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant examinationDate;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant archivedDate;

  private String archivedReason;

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

  private HiveTypeDTO hiveType;

  private ApiaryDTO beeyard;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public Integer getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Instant getExaminationDate() {
    return examinationDate;
  }

  public void setExaminationDate(Instant examinationDate) {
    this.examinationDate = examinationDate;
  }

  public Instant getArchivedDate() {
    return archivedDate;
  }

  public void setArchivedDate(Instant archivedDate) {
    this.archivedDate = archivedDate;
  }

  public String getArchivedReason() {
    return archivedReason;
  }

  public void setArchivedReason(String archivedReason) {
    this.archivedReason = archivedReason;
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

  public HiveTypeDTO getHiveType() {
    return hiveType;
  }

  public void setHiveType(HiveTypeDTO hiveType) {
    this.hiveType = hiveType;
  }

  public ApiaryDTO getBeeyard() {
    return beeyard;
  }

  public void setBeeyard(ApiaryDTO beeyard) {
    this.beeyard = beeyard;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HiveDTO)) {
      return false;
    }

    HiveDTO hiveDTO = (HiveDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, hiveDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "HiveDTO{" +
        "id=" + getId() +
        ", barcode='" + getBarcode() + "'" +
        ", orderNumber=" + getOrderNumber() +
        ", description='" + getDescription() + "'" +
        ", examinationDate='" + getExaminationDate() + "'" +
        ", archivedDate='" + getArchivedDate() + "'" +
        ", archivedReason='" + getArchivedReason() + "'" +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        ", hiveType=" + getHiveType() +
        ", apiary=" + getBeeyard() +
        "}";
  }
}
