package rs.hexatech.beeback.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.Group} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupDTO implements Serializable {

  private Long id;

  private String name;

  private String enumValueName;

  private String color;

  private Integer hiveCount;

  private Integer hiveCountFinished;

  private String additionalInfo;

  private Integer orderNumber;

  @NotNull
  private String uuid;

  @NotNull
  private Instant dateCreated;

  @NotNull
  private Instant dateModified;

  @NotNull
  private Instant dateSynched;

  private Instant dateDeleted;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEnumValueName() {
    return enumValueName;
  }

  public void setEnumValueName(String enumValueName) {
    this.enumValueName = enumValueName;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Integer getHiveCount() {
    return hiveCount;
  }

  public void setHiveCount(Integer hiveCount) {
    this.hiveCount = hiveCount;
  }

  public Integer getHiveCountFinished() {
    return hiveCountFinished;
  }

  public void setHiveCountFinished(Integer hiveCountFinished) {
    this.hiveCountFinished = hiveCountFinished;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public Integer getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
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
    if (!(o instanceof GroupDTO)) {
      return false;
    }

    GroupDTO groupDTO = (GroupDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, groupDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "GroupDTO{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", enumValueName='" + getEnumValueName() + "'" +
        ", color='" + getColor() + "'" +
        ", hiveCount=" + getHiveCount() +
        ", hiveCountFinished=" + getHiveCountFinished() +
        ", additionalInfo='" + getAdditionalInfo() + "'" +
        ", orderNumber=" + getOrderNumber() +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        "}";
  }
}
