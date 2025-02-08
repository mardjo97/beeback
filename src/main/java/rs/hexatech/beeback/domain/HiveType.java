package rs.hexatech.beeback.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HiveType.
 */
@Entity
@Table(name = "hive_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HiveType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String Name;

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

    public HiveType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.Name;
    }

    public HiveType Name(String Name) {
        this.setName(Name);
        return this;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Integer getExternalId() {
        return this.externalId;
    }

    public HiveType externalId(Integer externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public HiveType uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public HiveType dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return this.dateModified;
    }

    public HiveType dateModified(Instant dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateSynched() {
        return this.dateSynched;
    }

    public HiveType dateSynched(Instant dateSynched) {
        this.setDateSynched(dateSynched);
        return this;
    }

    public void setDateSynched(Instant dateSynched) {
        this.dateSynched = dateSynched;
    }

    public Instant getDateDeleted() {
        return this.dateDeleted;
    }

    public HiveType dateDeleted(Instant dateDeleted) {
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

    public HiveType user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HiveType)) {
            return false;
        }
        return getId() != null && getId().equals(((HiveType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HiveType{" +
            "id=" + getId() +
            ", Name='" + getName() + "'" +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            ", dateDeleted='" + getDateDeleted() + "'" +
            "}";
    }
}
