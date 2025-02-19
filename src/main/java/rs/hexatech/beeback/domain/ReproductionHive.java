package rs.hexatech.beeback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReproductionHive.
 */
@Entity
@Table(name = "reproduction_hive")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReproductionHive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

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

    @Column(name = "date_finished")
    private Instant dateFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "hiveType", "apiary" }, allowSetters = true)
    private Hive hive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReproductionHive id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public ReproductionHive note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getExternalId() {
        return this.externalId;
    }

    public ReproductionHive externalId(Integer externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public ReproductionHive uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public ReproductionHive dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return this.dateModified;
    }

    public ReproductionHive dateModified(Instant dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateSynched() {
        return this.dateSynched;
    }

    public ReproductionHive dateSynched(Instant dateSynched) {
        this.setDateSynched(dateSynched);
        return this;
    }

    public void setDateSynched(Instant dateSynched) {
        this.dateSynched = dateSynched;
    }

    public Instant getDateDeleted() {
        return this.dateDeleted;
    }

    public ReproductionHive dateDeleted(Instant dateDeleted) {
        this.setDateDeleted(dateDeleted);
        return this;
    }

    public void setDateDeleted(Instant dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public Instant getDateFinished() {
        return this.dateFinished;
    }

    public ReproductionHive dateFinished(Instant dateFinished) {
        this.setDateFinished(dateFinished);
        return this;
    }

    public void setDateFinished(Instant dateFinished) {
        this.dateFinished = dateFinished;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReproductionHive user(User user) {
        this.setUser(user);
        return this;
    }

    public Hive getHive() {
        return this.hive;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    public ReproductionHive hive(Hive hive) {
        this.setHive(hive);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReproductionHive)) {
            return false;
        }
        return getId() != null && getId().equals(((ReproductionHive) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReproductionHive{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            ", dateDeleted='" + getDateDeleted() + "'" +
            ", dateFinished='" + getDateFinished() + "'" +
            "}";
    }
}
