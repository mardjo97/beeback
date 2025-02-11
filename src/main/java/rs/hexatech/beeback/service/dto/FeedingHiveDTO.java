package rs.hexatech.beeback.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link rs.hexatech.beeback.domain.FeedingHive} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedingHiveDTO implements Serializable {

    private Long id;

    private Double foodAmount;

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

    private Instant dateDeleted;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(Double foodAmount) {
        this.foodAmount = foodAmount;
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

    public Instant getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Instant dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedingHiveDTO)) {
            return false;
        }

        FeedingHiveDTO feedingHiveDTO = (FeedingHiveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedingHiveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedingHiveDTO{" +
            "id=" + getId() +
            ", foodAmount=" + getFoodAmount() +
            ", externalId=" + getExternalId() +
            ", uuid='" + getUuid() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", dateSynched='" + getDateSynched() + "'" +
            ", dateDeleted='" + getDateDeleted() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
