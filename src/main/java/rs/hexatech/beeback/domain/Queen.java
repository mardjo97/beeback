package rs.hexatech.beeback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Queen.
 */
@Entity
@Table(name = "queen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Queen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "origin")
    private String origin;

    @Column(name = "year")
    private Integer year;

    @Column(name = "is_marked")
    private Boolean isMarked;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "active_from_date")
    private Instant activeFromDate;

    @Column(name = "active_to_date")
    private Instant activeToDate;

    @Column(name = "queen_change_date")
    private Instant queenChangeDate;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Queen id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Queen origin(String origin) {
        this.setOrigin(origin);
        return this;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getYear() {
        return this.year;
    }

    public Queen year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getIsMarked() {
        return this.isMarked;
    }

    public Queen isMarked(Boolean isMarked) {
        this.setIsMarked(isMarked);
        return this;
    }

    public void setIsMarked(Boolean isMarked) {
        this.isMarked = isMarked;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Queen active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getActiveFromDate() {
        return this.activeFromDate;
    }

    public Queen activeFromDate(Instant activeFromDate) {
        this.setActiveFromDate(activeFromDate);
        return this;
    }

    public void setActiveFromDate(Instant activeFromDate) {
        this.activeFromDate = activeFromDate;
    }

    public Instant getActiveToDate() {
        return this.activeToDate;
    }

    public Queen activeToDate(Instant activeToDate) {
        this.setActiveToDate(activeToDate);
        return this;
    }

    public void setActiveToDate(Instant activeToDate) {
        this.activeToDate = activeToDate;
    }

    public Instant getQueenChangeDate() {
        return this.queenChangeDate;
    }

    public Queen queenChangeDate(Instant queenChangeDate) {
        this.setQueenChangeDate(queenChangeDate);
        return this;
    }

    public void setQueenChangeDate(Instant queenChangeDate) {
        this.queenChangeDate = queenChangeDate;
    }

    public Integer getExternalId() {
        return this.externalId;
    }

    public Queen externalId(Integer externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Queen uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Queen dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return this.dateModified;
    }

    public Queen dateModified(Instant dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateSynched() {
        return this.dateSynched;
    }

    public Queen dateSynched(Instant dateSynched) {
        this.setDateSynched(dateSynched);
        return this;
    }

    public void setDateSynched(Instant dateSynched) {
        this.dateSynched = dateSynched;
    }

    public Instant getDateDeleted() {
        return this.dateDeleted;
    }

    public Queen dateDeleted(Instant dateDeleted) {
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

    public Queen user(User user) {
        this.setUser(user);
        return this;
    }

    public Hive getHive() {
        return this.hive;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    public Queen hive(Hive hive) {
        this.setHive(hive);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Queen)) {
            return false;
        }
        return getId() != null && getId().equals(((Queen) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Queen{" +
            "id=" + getId() +
            ", origin='" + getOrigin() + "'" +
            ", year=" + getYear() +
            ", isMarked='" + getIsMarked() + "'" +
            ", active='" + getActive() + "'" +
            ", activeFromDate='" + getActiveFromDate() + "'" +
            ", activeToDate='" + getActiveToDate() + "'" +
            ", queenChangeDate='" + getQueenChangeDate() + "'" +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            ", dateDeleted='" + getDateDeleted() + "'" +
            "}";
    }
}
