package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.GoodHarvestHive;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.GoodHarvestHiveRepository;
import rs.hexatech.beeback.service.dto.GoodHarvestHiveDTO;
import rs.hexatech.beeback.service.mapper.GoodHarvestHiveMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.GoodHarvestHive}.
 */
@Service
@Transactional
public class GoodHarvestHiveService {

  private static final Logger LOG = LoggerFactory.getLogger(GoodHarvestHiveService.class);

  private final GoodHarvestHiveRepository repository;

  private final GoodHarvestHiveMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  @Autowired
  HarvestTypeService harvestTypeService;

  public GoodHarvestHiveService(GoodHarvestHiveRepository goodHarvestHiveRepository, GoodHarvestHiveMapper goodHarvestHiveMapper) {
    this.repository = goodHarvestHiveRepository;
    this.mapper = goodHarvestHiveMapper;
  }

  public List<GoodHarvestHiveDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's GoodHarvestHives! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<GoodHarvestHiveDTO> sync(List<GoodHarvestHiveDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private GoodHarvestHive syncEntity(final GoodHarvestHiveDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      GoodHarvestHive existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        GoodHarvestHive mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    GoodHarvestHive existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    GoodHarvestHive toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final GoodHarvestHive entity, final GoodHarvestHiveDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final GoodHarvestHive entity, final GoodHarvestHiveDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final GoodHarvestHive entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(GoodHarvestHive goodHarvestHive, GoodHarvestHiveDTO goodHarvestHiveDto, User user) {
    try {
      String hiveUUID = goodHarvestHiveDto.getHive() != null ? goodHarvestHiveDto.getHive().getUuid() : null;
      Long hiveExternalId = goodHarvestHiveDto.getHive() != null ? goodHarvestHiveDto.getHive().getId() : null;
      goodHarvestHive.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the goodHarvestHive: {}", goodHarvestHiveDto);
      return false;
    }
    try {
      String harvestTypeUUID = goodHarvestHiveDto.getHarvestType().getUuid() != null ? goodHarvestHiveDto.getHarvestType().getUuid() : null;
      Long harvestTypeExternalId = goodHarvestHiveDto.getHarvestType() != null ? goodHarvestHiveDto.getHarvestType().getId() : null;
      HarvestType ht = harvestTypeService.getHarvestTypeByUUIDOrUserAndExternalId(harvestTypeUUID, user, harvestTypeExternalId);
      goodHarvestHive.setHarvestType(ht);
    } catch (Exception e) {
      LOG.error("Can not map harvestType for the goodHarvestHive: {}", goodHarvestHive);
      return false;
    }
    return true;
  }

  /**
   * Save a goodHarvestHive.
   *
   * @param goodHarvestHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public GoodHarvestHiveDTO save(GoodHarvestHiveDTO goodHarvestHiveDTO) {
    LOG.debug("Request to save GoodHarvestHive : {}", goodHarvestHiveDTO);
    GoodHarvestHive goodHarvestHive = mapper.toEntity(goodHarvestHiveDTO);
    goodHarvestHive = repository.save(goodHarvestHive);
    return mapper.toDto(goodHarvestHive);
  }

  /**
   * Update a goodHarvestHive.
   *
   * @param goodHarvestHiveDTO the entity to save.
   * @return the persisted entity.
   */
  public GoodHarvestHiveDTO update(GoodHarvestHiveDTO goodHarvestHiveDTO) {
    LOG.debug("Request to update GoodHarvestHive : {}", goodHarvestHiveDTO);
    GoodHarvestHive goodHarvestHive = mapper.toEntity(goodHarvestHiveDTO);
    goodHarvestHive = repository.save(goodHarvestHive);
    return mapper.toDto(goodHarvestHive);
  }

  /**
   * Partially update a goodHarvestHive.
   *
   * @param goodHarvestHiveDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<GoodHarvestHiveDTO> partialUpdate(GoodHarvestHiveDTO goodHarvestHiveDTO) {
    LOG.debug("Request to partially update GoodHarvestHive : {}", goodHarvestHiveDTO);

    return repository
        .findById(goodHarvestHiveDTO.getId())
        .map(existingGoodHarvestHive -> {
          mapper.partialUpdate(existingGoodHarvestHive, goodHarvestHiveDTO);

          return existingGoodHarvestHive;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the goodHarvestHives.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<GoodHarvestHiveDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all GoodHarvestHives");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one goodHarvestHive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<GoodHarvestHiveDTO> findOne(Long id) {
    LOG.debug("Request to get GoodHarvestHive : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the goodHarvestHive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete GoodHarvestHive : {}", id);
    repository.deleteById(id);
  }
}
