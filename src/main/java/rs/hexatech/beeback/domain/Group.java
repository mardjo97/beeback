package rs.hexatech.beeback.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "enum_value_name")
    private String enumValueName;

    @Column(name = "color")
    private String color;

    @Column(name = "hive_count")
    private Integer hiveCount;

    @Column(name = "hive_count_finished")
    private Integer hiveCountFinished;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "order_number")
    private Integer orderNumber;

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

    public Group id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Group name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnumValueName() {
        return this.enumValueName;
    }

    public Group enumValueName(String enumValueName) {
        this.setEnumValueName(enumValueName);
        return this;
    }

    public void setEnumValueName(String enumValueName) {
        this.enumValueName = enumValueName;
    }

    public String getColor() {
        return this.color;
    }

    public Group color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getHiveCount() {
        return this.hiveCount;
    }

    public Group hiveCount(Integer hiveCount) {
        this.setHiveCount(hiveCount);
        return this;
    }

    public void setHiveCount(Integer hiveCount) {
        this.hiveCount = hiveCount;
    }

    public Integer getHiveCountFinished() {
        return this.hiveCountFinished;
    }

    public Group hiveCountFinished(Integer hiveCountFinished) {
        this.setHiveCountFinished(hiveCountFinished);
        return this;
    }

    public void setHiveCountFinished(Integer hiveCountFinished) {
        this.hiveCountFinished = hiveCountFinished;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public Group additionalInfo(String additionalInfo) {
        this.setAdditionalInfo(additionalInfo);
        return this;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Integer getOrderNumber() {
        return this.orderNumber;
    }

    public Group orderNumber(Integer orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getExternalId() {
        return this.externalId;
    }

    public Group externalId(Integer externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Group uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Group dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return this.dateModified;
    }

    public Group dateModified(Instant dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateSynched() {
        return this.dateSynched;
    }

    public Group dateSynched(Instant dateSynched) {
        this.setDateSynched(dateSynched);
        return this;
    }

    public void setDateSynched(Instant dateSynched) {
        this.dateSynched = dateSynched;
    }

    public Instant getDateDeleted() {
        return this.dateDeleted;
    }

    public Group dateDeleted(Instant dateDeleted) {
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

    public Group user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return getId() != null && getId().equals(((Group) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enumValueName='" + getEnumValueName() + "'" +
            ", color='" + getColor() + "'" +
            ", hiveCount=" + getHiveCount() +
            ", hiveCountFinished=" + getHiveCountFinished() +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            ", orderNumber=" + getOrderNumber() +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            ", dateDeleted='" + getDateDeleted() + "'" +
            "}";
    }
}
