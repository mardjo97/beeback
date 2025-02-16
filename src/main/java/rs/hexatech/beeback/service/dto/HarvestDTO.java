package rs.hexatech.beeback.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.Harvest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HarvestDTO implements Serializable {

  private Long id;

  private Integer hiveFrames;

  private Double amount;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
  private Instant dateCollected;

  private String group;

  private Integer groupRecordId;

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

  private HarvestTypeDTO harvestType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getHiveFrames() {
    return hiveFrames;
  }

  public void setHiveFrames(Integer hiveFrames) {
    this.hiveFrames = hiveFrames;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Instant getDateCollected() {
    return dateCollected;
  }

  public void setDateCollected(Instant dateCollected) {
    this.dateCollected = dateCollected;
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

  public HarvestTypeDTO getHarvestType() {
    return harvestType;
  }

  public void setHarvestType(HarvestTypeDTO harvestType) {
    this.harvestType = harvestType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HarvestDTO)) {
      return false;
    }

    HarvestDTO harvestDTO = (HarvestDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, harvestDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "HarvestDTO{" +
        "id=" + getId() +
        ", hiveFrames=" + getHiveFrames() +
        ", amount=" + getAmount() +
        ", dateCollected='" + getDateCollected() + "'" +
        ", group='" + getGroup() + "'" +
        ", groupRecordId=" + getGroupRecordId() +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        ", hive=" + getHive() +
        ", harvestType=" + getHarvestType() +
        "}";
  }
}
