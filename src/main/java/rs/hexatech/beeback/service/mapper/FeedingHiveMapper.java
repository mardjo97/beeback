package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.FeedingHive;
import rs.hexatech.beeback.service.dto.FeedingHiveDTO;

import java.util.List;

/**
 * Mapper for the entity {@link FeedingHive} and its DTO {@link FeedingHiveDTO}.
 */
@Mapper(componentModel = "spring", uses = {HiveMapper.class})
public interface FeedingHiveMapper extends EntityMapper<FeedingHiveDTO, FeedingHive> {
  @Named("feedingHiveToDto")
  @Mapping(target = "id", source = "externalId")
  @Mapping(target = "hive", source = "hive", qualifiedByName = "hiveToDto")
  FeedingHiveDTO toDto(FeedingHive s);

  @Named("feedingHiveToDtos")
  default List<FeedingHiveDTO> toDto(List<FeedingHive> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("feedingHiveToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  FeedingHive toEntity(FeedingHiveDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "hive", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget FeedingHive entity, FeedingHiveDTO dto);
}
