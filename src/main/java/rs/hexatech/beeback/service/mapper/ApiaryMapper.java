package rs.hexatech.beeback.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rs.hexatech.beeback.domain.Apiary;
import rs.hexatech.beeback.service.dto.ApiaryDTO;

/**
 * Mapper for the entity {@link Apiary} and its DTO {@link ApiaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApiaryMapper extends EntityMapper<ApiaryDTO, Apiary> {
    @Mapping(target = "id", source = "externalId")
    ApiaryDTO toDto(Apiary s);

    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    Apiary toEntity(ApiaryDTO s);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "dateSynched", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    void partialUpdate(@MappingTarget Apiary entity, ApiaryDTO dto);
}
