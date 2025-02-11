package rs.hexatech.beeback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Harvest.
 */
@Entity
@Table(name = "harvest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Harvest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hive_frames")
    private Integer hiveFrames;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date_collected")
    private Instant dateCollected;

    @Column(name = "jhi_group")
    private String group;

    @Column(name = "group_record_id")
    private Integer groupRecordId;

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
    @JsonIgnoreProperties(value = { "user", "hiveType", "apiary" }, allowSetters = true)
    private Hive hive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private HarvestType harvestType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Harvest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHiveFrames() {
        return this.hiveFrames;
    }

    public Harvest hiveFrames(Integer hiveFrames) {
        this.setHiveFrames(hiveFrames);
        return this;
    }

    public void setHiveFrames(Integer hiveFrames) {
        this.hiveFrames = hiveFrames;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Harvest amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getDateCollected() {
        return this.dateCollected;
    }

    public Harvest dateCollected(Instant dateCollected) {
        this.setDateCollected(dateCollected);
        return this;
    }

    public void setDateCollected(Instant dateCollected) {
        this.dateCollected = dateCollected;
    }

    public String getGroup() {
        return this.group;
    }

    public Harvest group(String group) {
        this.setGroup(group);
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getGroupRecordId() {
        return this.groupRecordId;
    }

    public Harvest groupRecordId(Integer groupRecordId) {
        this.setGroupRecordId(groupRecordId);
        return this;
    }

    public void setGroupRecordId(Integer groupRecordId) {
        this.groupRecordId = groupRecordId;
    }

    public Integer getExternalId() {
        return this.externalId;
    }

    public Harvest externalId(Integer externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Harvest uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Harvest dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return this.dateModified;
    }

    public Harvest dateModified(Instant dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateSynched() {
        return this.dateSynched;
    }

    public Harvest dateSynched(Instant dateSynched) {
        this.setDateSynched(dateSynched);
        return this;
    }

    public void setDateSynched(Instant dateSynched) {
        this.dateSynched = dateSynched;
    }

    public Instant getDateDeleted() {
        return this.dateDeleted;
    }

    public Harvest dateDeleted(Instant dateDeleted) {
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

    public Harvest user(User user) {
        this.setUser(user);
        return this;
    }

    public Hive getHive() {
        return this.hive;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    public Harvest hive(Hive hive) {
        this.setHive(hive);
        return this;
    }

    public HarvestType getHarvestType() {
        return this.harvestType;
    }

    public void setHarvestType(HarvestType harvestType) {
        this.harvestType = harvestType;
    }

    public Harvest harvestType(HarvestType harvestType) {
        this.setHarvestType(harvestType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Harvest)) {
            return false;
        }
        return getId() != null && getId().equals(((Harvest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Harvest{" +
            "id=" + getId() +
            ", hiveFrames=" + getHiveFrames() +
            ", amount=" + getAmount() +
            ", dateCollected='" + getDateCollected() + "'" +
            ", group='" + getGroup() + "'" +
            ", groupRecordId=" + getGroupRecordId() +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            ", dateDeleted='" + getDateDeleted() + "'" +
            "}";
    }
}
