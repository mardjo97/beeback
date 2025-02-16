package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rs.hexatech.beeback.domain.Group;
import rs.hexatech.beeback.service.dto.GroupDTO;

import java.util.List;

/**
 * Mapper for the entity {@link Group} and its DTO {@link GroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {
  @Named("queenToDto")
  @Mapping(target = "id", source = "externalId")
  GroupDTO toDto(Group s);

  @Named("queenToDtos")
  default List<GroupDTO> toDto(List<Group> s) {
    return s.stream()
        .map(this::toDto)
        .toList();
  }

  @Named("queenToEntity")
  @Mapping(target = "externalId", source = "id")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  Group toEntity(GroupDTO s);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "dateSynched", ignore = true)
  @Mapping(target = "externalId", ignore = true)
  @Mapping(target = "user", ignore = true)
  void partialUpdate(@MappingTarget Group entity, GroupDTO dto);
}
