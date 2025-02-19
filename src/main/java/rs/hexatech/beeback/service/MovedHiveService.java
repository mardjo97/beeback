package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.MovedHive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.MovedHiveRepository;
import rs.hexatech.beeback.service.dto.MovedHiveDTO;
import rs.hexatech.beeback.service.mapper.MovedHiveMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.MovedHive}.
 */
@Service
@Transactional
public class MovedHiveService {

  private static final Logger LOG = LoggerFactory.getLogger(MovedHiveService.class);

  private final MovedHiveRepository repository;

  private final MovedHiveMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  @Autowired
  HarvestTypeService harvestTypeService;

  public MovedHiveService(MovedHiveRepository movedHiveRepository, MovedHiveMapper movedHiveMapper) {
    this.repository = movedHiveRepository;
    this.mapper = movedHiveMapper;
  }

  public List<MovedHiveDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's MovedHives! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<MovedHiveDTO> sync(List<MovedHiveDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private MovedHive syncEntity(final MovedHiveDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      MovedHive existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        MovedHive mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    MovedHive existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    MovedHive toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final MovedHive entity, final MovedHiveDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final MovedHive entity, final MovedHiveDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final MovedHive entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(MovedHive movedHive, MovedHiveDTO movedHiveDto, User user) {
    try {
      String hiveUUID = movedHiveDto.getHive() != null ? movedHiveDto.getHive().getUuid() : null;
      Long hiveExternalId = movedHiveDto.getHive() != null ? movedHiveDto.getHive().getId() : null;
      movedHive.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the movedHive: {}", movedHiveDto);
      return false;
    }
    try {
      String harvestTypeUUID = movedHiveDto.getHarvestType().getUuid() != null ? movedHiveDto.getHarvestType().getUuid() : null;
      Long harvestTypeExternalId = movedHiveDto.getHarvestType() != null ? movedHiveDto.getHarvestType().getId() : null;
      HarvestType ht = harvestTypeService.getHarvestTypeByUUIDOrUserAndExternalId(harvestTypeUUID, user, harvestTypeExternalId);
      movedHive.setHarvestType(ht);
    } catch (Exception e) {
      LOG.error("Can not map harvestType for the movedHive: {}", movedHive);
      return false;
    }
    return true;
  }


  /**
   * Save a movedHive.
   *
   * @param movedHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public MovedHiveDTO save(MovedHiveDTO movedHiveDTO) {
    LOG.debug("Request to save MovedHive : {}", movedHiveDTO);
    MovedHive movedHive = mapper.toEntity(movedHiveDTO);
    movedHive = repository.save(movedHive);
    return mapper.toDto(movedHive);
  }

  /**
   * Update a movedHive.
   *
   * @param movedHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public MovedHiveDTO update(MovedHiveDTO movedHiveDTO) {
    LOG.debug("Request to update MovedHive : {}", movedHiveDTO);
    MovedHive movedHive = mapper.toEntity(movedHiveDTO);
    movedHive = repository.save(movedHive);
    return mapper.toDto(movedHive);
  }

  /**
   * Partially update a movedHive.
   *
   * @param movedHiveDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<MovedHiveDTO> partialUpdate(MovedHiveDTO movedHiveDTO) {
    LOG.debug("Request to partially update MovedHive : {}", movedHiveDTO);

    return repository
        .findById(movedHiveDTO.getId())
        .map(existingMovedHive -> {
          mapper.partialUpdate(existingMovedHive, movedHiveDTO);

          return existingMovedHive;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the movedHives.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<MovedHiveDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all MovedHives");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one movedHive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<MovedHiveDTO> findOne(Long id) {
    LOG.debug("Request to get MovedHive : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the movedHive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete MovedHive : {}", id);
    repository.deleteById(id);
  }
}
