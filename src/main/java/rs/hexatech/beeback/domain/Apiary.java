package rs.hexatech.beeback.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Apiary.
 */
@Entity
@Table(name = "apiary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Apiary implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "id_number")
  private String idNumber;

  @Column(name = "color")
  private String color;

  @Column(name = "location")
  private String location;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  @Column(name = "order_number")
  private Integer orderNumber;

  @Column(name = "hive_count")
  private Integer hiveCount;

  @NotNull
  @Column(name = "external_id", nullable = false)
  private Integer externalId;

  @NotNull
  @Column(name = "uuid", nullable = false, unique = true)
  private String uuid;

  @NotNull
  @Column(name = "date_created", nullable = false)
  private Instant dateCreated;

  @NotNull
  @Column(name = "date_modified", nullable = false)
  private Instant dateModified;

  @NotNull
  @Column(name = "date_synched", nullable = false)
  private Instant dateSynched;

  @Column(name = "date_deleted")
  private Instant dateDeleted;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public Apiary id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public Apiary name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdNumber() {
    return this.idNumber;
  }

  public Apiary idNumber(String idNumber) {
    this.setIdNumber(idNumber);
    return this;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public String getColor() {
    return this.color;
  }

  public Apiary color(String color) {
    this.setColor(color);
    return this;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getLocation() {
    return this.location;
  }

  public Apiary location(String location) {
    this.setLocation(location);
    return this;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Double getLatitude() {
    return this.latitude;
  }

  public Apiary latitude(Double latitude) {
    this.setLatitude(latitude);
    return this;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return this.longitude;
  }

  public Apiary longitude(Double longitude) {
    this.setLongitude(longitude);
    return this;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Integer getOrderNumber() {
    return this.orderNumber;
  }

  public Apiary orderNumber(Integer orderNumber) {
    this.setOrderNumber(orderNumber);
    return this;
  }

  public void setOrderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Integer getHiveCount() {
    return this.hiveCount;
  }

  public Apiary hiveCount(Integer hiveCount) {
    this.setHiveCount(hiveCount);
    return this;
  }

  public void setHiveCount(Integer hiveCount) {
    this.hiveCount = hiveCount;
  }

  public Integer getExternalId() {
    return this.externalId;
  }

  public Apiary externalId(Integer externalId) {
    this.setExternalId(externalId);
    return this;
  }

  public void setExternalId(Integer externalId) {
    this.externalId = externalId;
  }

  public String getUuid() {
    return this.uuid;
  }

  public Apiary uuid(String uuid) {
    this.setUuid(uuid);
    return this;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Instant getDateCreated() {
    return this.dateCreated;
  }

  public Apiary dateCreated(Instant dateCreated) {
    this.setDateCreated(dateCreated);
    return this;
  }

  public void setDateCreated(Instant dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Instant getDateModified() {
    return this.dateModified;
  }

  public Apiary dateModified(Instant dateModified) {
    this.setDateModified(dateModified);
    return this;
  }

  public void setDateModified(Instant dateModified) {
    this.dateModified = dateModified;
  }

  public Instant getDateSynched() {
    return this.dateSynched;
  }

  public Apiary dateSynched(Instant dateSynched) {
    this.setDateSynched(dateSynched);
    return this;
  }

  public void setDateSynched(Instant dateSynched) {
    this.dateSynched = dateSynched;
  }

  public Instant getDateDeleted() {
    return this.dateDeleted;
  }

  public Apiary dateDeleted(Instant dateDeleted) {
    this.setDateDeleted(dateDeleted);
    return this;
  }

  public void setDateDeleted(Instant dateDeleted) {
    this.dateDeleted = dateDeleted;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Apiary user(User user) {
    this.setUser(user);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Apiary)) {
      return false;
    }
    return getId() != null && getId().equals(((Apiary) o).getId());
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "Apiary{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", idNumber='" + getIdNumber() + "'" +
        ", color='" + getColor() + "'" +
        ", location='" + getLocation() + "'" +
        ", latitude=" + getLatitude() +
        ", longitude=" + getLongitude() +
        ", orderNumber=" + getOrderNumber() +
        ", hiveCount=" + getHiveCount() +
        ", externalId=" + getExternalId() +
        ", uuid='" + getUuid() + "'" +
        ", dateCreated='" + getDateCreated() + "'" +
        ", dateModified='" + getDateModified() + "'" +
        ", dateSynched='" + getDateSynched() + "'" +
        ", dateDeleted='" + getDateDeleted() + "'" +
        "}";
  }
}
