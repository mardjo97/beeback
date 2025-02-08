package rs.hexatech.beeback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Hive.
 */
@Entity
@Table(name = "hive")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "examination_date")
    private Instant examinationDate;

    @Column(name = "archived_date")
    private Instant archivedDate;

    @Column(name = "archived_reason")
    private String archivedReason;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private HiveType hiveType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Apiary apiary;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hive id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public Hive barcode(String barcode) {
        this.setBarcode(barcode);
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getOrderNumber() {
        return this.orderNumber;
    }

    public Hive orderNumber(Integer orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public Hive description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getExaminationDate() {
        return this.examinationDate;
    }

    public Hive examinationDate(Instant examinationDate) {
        this.setExaminationDate(examinationDate);
        return this;
    }

    public void setExaminationDate(Instant examinationDate) {
        this.examinationDate = examinationDate;
    }

    public Instant getArchivedDate() {
        return this.archivedDate;
    }

    public Hive archivedDate(Instant archivedDate) {
        this.setArchivedDate(archivedDate);
        return this;
    }

    public void setArchivedDate(Instant archivedDate) {
        this.archivedDate = archivedDate;
    }

    public String getArchivedReason() {
        return this.archivedReason;
    }

    public Hive archivedReason(String archivedReason) {
        this.setArchivedReason(archivedReason);
        return this;
    }

    public void setArchivedReason(String archivedReason) {
        this.archivedReason = archivedReason;
    }

    public Integer getExternalId() {
        return this.externalId;
    }

    public Hive externalId(Integer externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Hive uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Hive dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return this.dateModified;
    }

    public Hive dateModified(Instant dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateSynched() {
        return this.dateSynched;
    }

    public Hive dateSynched(Instant dateSynched) {
        this.setDateSynched(dateSynched);
        return this;
    }

    public void setDateSynched(Instant dateSynched) {
        this.dateSynched = dateSynched;
    }

    public Instant getDateDeleted() {
        return this.dateDeleted;
    }

    public Hive dateDeleted(Instant dateDeleted) {
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

    public Hive user(User user) {
        this.setUser(user);
        return this;
    }

    public HiveType getHiveType() {
        return this.hiveType;
    }

    public void setHiveType(HiveType hiveType) {
        this.hiveType = hiveType;
    }

    public Hive hiveType(HiveType hiveType) {
        this.setHiveType(hiveType);
        return this;
    }

    public Apiary getApiary() {
        return this.apiary;
    }

    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
    }

    public Hive apiary(Apiary apiary) {
        this.setApiary(apiary);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hive)) {
            return false;
        }
        return getId() != null && getId().equals(((Hive) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hive{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", orderNumber=" + getOrderNumber() +
            ", description='" + getDescription() + "'" +
            ", examinationDate='" + getExaminationDate() + "'" +
            ", archivedDate='" + getArchivedDate() + "'" +
            ", archivedReason='" + getArchivedReason() + "'" +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            ", dateDeleted='" + getDateDeleted() + "'" +
            "}";
    }
}
