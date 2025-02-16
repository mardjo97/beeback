package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.AppConfig;
import rs.hexatech.beeback.service.dto.AppConfigDTO;

import java.util.List;

/**
 * Mapper for the entity {@link AppConfig} and its DTO {@link AppConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppConfigMapper extends EntityMapper<AppConfigDTO, AppConfig> {
  @Named("appConfigToDto")
  @Mapping(target = "id", source = "externalId")
  AppConfigDTO toDto(AppConfig s);

  @Named("appConfigToDtos")
  default List<AppConfigDTO> toDto(List<AppConfig> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("appConfigToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  AppConfig toEntity(AppConfigDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget AppConfig entity, AppConfigDTO dto);
}
