package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Harvest;
import rs.hexatech.beeback.domain.HarvestType;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.HarvestRepository;
import rs.hexatech.beeback.service.dto.HarvestDTO;
import rs.hexatech.beeback.service.mapper.HarvestMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Harvest}.
 */
@Service
@Transactional
public class HarvestService {

  private static final Logger LOG = LoggerFactory.getLogger(HarvestService.class);

  private final HarvestRepository repository;

  private final HarvestMapper mapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  HiveService hiveService;

  @Autowired
  HarvestTypeService harvestTypeService;

  public HarvestService(HarvestRepository harvestRepository, HarvestMapper harvestMapper) {
    this.repository = harvestRepository;
    this.mapper = harvestMapper;
  }

  public List<HarvestDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's Harvests! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(repository.findByUserIsCurrentUser());
  }

  public List<HarvestDTO> sync(List<HarvestDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private Harvest syncEntity(final HarvestDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      Harvest existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        Harvest mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    Harvest existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    Harvest toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final Harvest entity, final HarvestDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
    return handleRelations(entity, entityDto, user);
  }

  private boolean toCreate(final Harvest entity, final HarvestDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(entity, entityDto, user);
  }

  private void toReset(final Harvest entity) {
    entity.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(Harvest harvest, HarvestDTO harvestDto, User user) {
    try {
      String hiveUUID = harvestDto.getHive() != null ? harvestDto.getHive().getUuid() : null;
      Long hiveExternalId = harvestDto.getHive() != null ? harvestDto.getHive().getId() : null;
      harvest.setHive(hiveService.getHiveByUUIDOrUserAndExternalId(hiveUUID, user, hiveExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive for the harvest: {}", harvestDto);
      return false;
    }
    try {
      String harvestTypeUUID = harvestDto.getHarvestType().getUuid() != null ? harvestDto.getHarvestType().getUuid() : null;
      Long harvestTypeExternalId = harvestDto.getHarvestType() != null ? harvestDto.getHarvestType().getId() : null;
      HarvestType ht = harvestTypeService.getHarvestTypeByUUIDOrUserAndExternalId(harvestTypeUUID, user, harvestTypeExternalId);
      harvest.setHarvestType(ht);
    } catch (Exception e) {
      LOG.error("Can not map harvestType for the harvest: {}", harvestDto);
      return false;
    }
    return true;
  }

  /**
   * Save a harvest.
   *
   * @param harvestDTO the entity to save.
   * @return the persisted entity.
   */
  public HarvestDTO save(HarvestDTO harvestDTO) {
    LOG.debug("Request to save Harvest : {}", harvestDTO);
    Harvest harvest = mapper.toEntity(harvestDTO);
    harvest = repository.save(harvest);
    return mapper.toDto(harvest);
  }

  /**
   * Update a harvest.
   *
   * @param harvestDTO the entity to save.
   * @return the persisted entity.
   */
  public HarvestDTO update(HarvestDTO harvestDTO) {
    LOG.debug("Request to update Harvest : {}", harvestDTO);
    Harvest harvest = mapper.toEntity(harvestDTO);
    harvest = repository.save(harvest);
    return mapper.toDto(harvest);
  }

  /**
   * Partially update a harvest.
   *
   * @param harvestDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<HarvestDTO> partialUpdate(HarvestDTO harvestDTO) {
    LOG.debug("Request to partially update Harvest : {}", harvestDTO);

    return repository
        .findById(harvestDTO.getId())
        .map(existingHarvest -> {
          mapper.partialUpdate(existingHarvest, harvestDTO);

          return existingHarvest;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the harvests.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<HarvestDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all Harvests");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one harvest by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<HarvestDTO> findOne(Long id) {
    LOG.debug("Request to get Harvest : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the harvest by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete Harvest : {}", id);
    repository.deleteById(id);
  }
}
