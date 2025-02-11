package rs.hexatech.beeback.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "has_reminder")
    private Boolean hasReminder;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "jhi_group")
    private String group;

    @Column(name = "group_record_id")
    private Integer groupRecordId;

    @Column(name = "date_hidden")
    private Instant dateHidden;

    @Column(name = "reminder_date")
    private Instant reminderDate;

    @Column(name = "reminder_id")
    private Integer reminderId;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "hiveType", "apiary" }, allowSetters = true)
    private Hive hive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Note id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHasReminder() {
        return this.hasReminder;
    }

    public Note hasReminder(Boolean hasReminder) {
        this.setHasReminder(hasReminder);
        return this;
    }

    public void setHasReminder(Boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public String getTitle() {
        return this.title;
    }

    public Note title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public Note content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroup() {
        return this.group;
    }

    public Note group(String group) {
        this.setGroup(group);
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getGroupRecordId() {
        return this.groupRecordId;
    }

    public Note groupRecordId(Integer groupRecordId) {
        this.setGroupRecordId(groupRecordId);
        return this;
    }

    public void setGroupRecordId(Integer groupRecordId) {
        this.groupRecordId = groupRecordId;
    }

    public Instant getDateHidden() {
        return this.dateHidden;
    }

    public Note dateHidden(Instant dateHidden) {
        this.setDateHidden(dateHidden);
        return this;
    }

    public void setDateHidden(Instant dateHidden) {
        this.dateHidden = dateHidden;
    }

    public Instant getReminderDate() {
        return this.reminderDate;
    }

    public Note reminderDate(Instant reminderDate) {
        this.setReminderDate(reminderDate);
        return this;
    }

    public void setReminderDate(Instant reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Integer getReminderId() {
        return this.reminderId;
    }

    public Note reminderId(Integer reminderId) {
        this.setReminderId(reminderId);
        return this;
    }

    public void setReminderId(Integer reminderId) {
        this.reminderId = reminderId;
    }

    public Integer getExternalId() {
        return this.externalId;
    }

    public Note externalId(Integer externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Note uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Note dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return this.dateModified;
    }

    public Note dateModified(Instant dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateSynched() {
        return this.dateSynched;
    }

    public Note dateSynched(Instant dateSynched) {
        this.setDateSynched(dateSynched);
        return this;
    }

    public void setDateSynched(Instant dateSynched) {
        this.dateSynched = dateSynched;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Note user(User user) {
        this.setUser(user);
        return this;
    }

    public Hive getHive() {
        return this.hive;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    public Note hive(Hive hive) {
        this.setHive(hive);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return getId() != null && getId().equals(((Note) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", hasReminder='" + getHasReminder() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", group='" + getGroup() + "'" +
            ", groupRecordId=" + getGroupRecordId() +
            ", dateHidden='" + getDateHidden() + "'" +
            ", reminderDate='" + getReminderDate() + "'" +
            ", reminderId=" + getReminderId() +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            "}";
    }
}
