package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.exception.NotFoundException;
import rs.hexatech.beeback.repository.HarvestTypeRepository;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import rs.hexatech.beeback.service.mapper.HarvestTypeMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.HarvestType}.
 */
@Service
@Transactional
public class HarvestTypeService {

  private static final Logger LOG = LoggerFactory.getLogger(HarvestTypeService.class);

  private final HarvestTypeRepository repository;

  private final HarvestTypeMapper mapper;

  @Autowired
  SecurityService securityService;

  public HarvestTypeService(HarvestTypeRepository harvestTypeRepository, HarvestTypeMapper harvestTypeMapper) {
    this.repository = harvestTypeRepository;
    this.mapper = harvestTypeMapper;
  }

  public List<HarvestTypeDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's HarvestTypes! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(getUserHarvestTypesOrDefault(user));
  }

  public List<HarvestTypeDTO> sync(List<HarvestTypeDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private HarvestType syncEntity(final HarvestTypeDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      HarvestType existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        HarvestType mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    HarvestType existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    HarvestType toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final HarvestType entity, final HarvestTypeDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private boolean toCreate(final HarvestType entity, final HarvestTypeDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private void toReset(final HarvestType entity) {
    entity.uuid(null).dateSynched(null);
  }

  private List<HarvestType> getUserHarvestTypesOrDefault(User user) {
    List<HarvestType> harvestTypes = repository.findByUserIsCurrentUser();
    if (harvestTypes.isEmpty()) {
      List<HarvestType> harvestTypesToCreate = repository.findByUserIsNull().stream()
          .filter(Objects::nonNull)
          .map(el -> mapHarvestTypeToCreate(el, user))
          .toList();
      harvestTypes = repository.saveAll(harvestTypesToCreate);
    }
    return harvestTypes;
  }

  private HarvestType mapHarvestTypeToCreate(HarvestType harvestType, User user) {
    return new HarvestType()
        .user(user)
        .uuid(UUID.randomUUID().toString())
        .dateCreated(DateTimeUtil.now())
        .dateModified(DateTimeUtil.now())
        .dateSynched(DateTimeUtil.now())
        .externalId(harvestType.getExternalId())
        .name(harvestType.getName());
  }

  public HarvestType getHarvestTypeByUUIDOrUserAndExternalId(String uuid, User user, Long externalId) {
    if (uuid != null) {
      HarvestType e = repository.findByUuidIs(uuid);
      if (e != null) {
        return e;
      }
    }
    if (user != null && externalId != null) {
      HarvestType e = repository.findByUserAndExternalId(user, externalId.intValue());
      if (e != null) {
        return e;
      }
    }
    throw new NotFoundException("HarvestType not found for uuid: " + uuid + " or externalId: " + externalId + " and user: " + user);
  }

  /**
   * Save a harvestType.
   *
   * @param harvestTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public HarvestTypeDTO save(HarvestTypeDTO harvestTypeDTO) {
    LOG.debug("Request to save HarvestType : {}", harvestTypeDTO);
    HarvestType harvestType = mapper.toEntity(harvestTypeDTO);
    harvestType = repository.save(harvestType);
    return mapper.toDto(harvestType);
  }

  /**
   * Update a harvestType.
   *
   * @param harvestTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public HarvestTypeDTO update(HarvestTypeDTO harvestTypeDTO) {
    LOG.debug("Request to update HarvestType : {}", harvestTypeDTO);
    HarvestType harvestType = mapper.toEntity(harvestTypeDTO);
    harvestType = repository.save(harvestType);
    return mapper.toDto(harvestType);
  }

  /**
   * Partially update a harvestType.
   *
   * @param harvestTypeDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<HarvestTypeDTO> partialUpdate(HarvestTypeDTO harvestTypeDTO) {
    LOG.debug("Request to partially update HarvestType : {}", harvestTypeDTO);

    return repository
        .findById(harvestTypeDTO.getId())
        .map(existingHarvestType -> {
          mapper.partialUpdate(existingHarvestType, harvestTypeDTO);

          return existingHarvestType;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the harvestTypes.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<HarvestTypeDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all HarvestTypes");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one harvestType by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<HarvestTypeDTO> findOne(Long id) {
    LOG.debug("Request to get HarvestType : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the harvestType by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete HarvestType : {}", id);
    repository.deleteById(id);
  }
}
