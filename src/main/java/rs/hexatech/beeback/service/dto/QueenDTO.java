package rs.hexatech.beeback.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.Queen} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QueenDTO implements Serializable {

    private Long id;

    private String origin;

    private Integer year;

    private Boolean isMarked;

    private Boolean active;

    private Instant activeFromDate;

    private Instant activeToDate;

    private Instant queenChangeDate;

    @NotNull
    private Integer externalId;

    @NotNull
    private String uuid;

    @NotNull
    private Instant dateCreated;

    @NotNull
    private Instant dateModified;

    @NotNull
    private Instant dateSynched;

    private UserDTO user;

    private HiveDTO hive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getIsMarked() {
        return isMarked;
    }

    public void setIsMarked(Boolean isMarked) {
        this.isMarked = isMarked;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getActiveFromDate() {
        return activeFromDate;
    }

    public void setActiveFromDate(Instant activeFromDate) {
        this.activeFromDate = activeFromDate;
    }

    public Instant getActiveToDate() {
        return activeToDate;
    }

    public void setActiveToDate(Instant activeToDate) {
        this.activeToDate = activeToDate;
    }

    public Instant getQueenChangeDate() {
        return queenChangeDate;
    }

    public void setQueenChangeDate(Instant queenChangeDate) {
        this.queenChangeDate = queenChangeDate;
    }

    public Integer getExternalId() {
        return externalId;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public HiveDTO getHive() {
        return hive;
    }

    public void setHive(HiveDTO hive) {
        this.hive = hive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueenDTO)) {
            return false;
        }

        QueenDTO queenDTO = (QueenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, queenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QueenDTO{" +
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
            ", user=" + getUser() +
            ", hive=" + getHive() +
            "}";
    }
}
