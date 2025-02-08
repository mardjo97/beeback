package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.service.dto.ApiaryDTO;

import java.util.List;

/**
 * Mapper for the entity {@link Apiary} and its DTO {@link ApiaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApiaryMapper extends EntityMapper<ApiaryDTO, Apiary> {
  @Named("apiaryToDto")
  @Mapping(target = "id", source = "externalId")
  ApiaryDTO toDto(Apiary s);

  @Named("apiaryToDtos")
  default List<ApiaryDTO> toDto(List<Apiary> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("apiaryToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  Apiary toEntity(ApiaryDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget Apiary entity, ApiaryDTO dto);
}
