package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.HiveLocation;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.HiveLocationRepository;
import rs.hexatech.beeback.service.dto.HiveLocationDTO;
import rs.hexatech.beeback.service.mapper.HiveLocationMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.HiveLocation}.
 */
@Service
@Transactional
public class HiveLocationService {

  private static final Logger LOG = LoggerFactory.getLogger(HiveLocationService.class);

  private final HiveLocationRepository repository;

  private final HiveLocationMapper mapper;

  @Autowired
  SecurityService securityService;

  public HiveLocationService(HiveLocationRepository hiveLocationRepository, HiveLocationMapper hiveLocationMapper) {
    this.repository = hiveLocationRepository;
    this.mapper = hiveLocationMapper;
  }
  
  public List<HiveLocationDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's HiveLocations! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(getUserHiveLocationsOrDefault(user));
  }

  public List<HiveLocationDTO> sync(List<HiveLocationDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private HiveLocation syncEntity(final HiveLocationDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      HiveLocation existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        HiveLocation mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    HiveLocation existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    HiveLocation toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final HiveLocation entity, final HiveLocationDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private boolean toCreate(final HiveLocation entity, final HiveLocationDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private void toReset(final HiveLocation entity) {
    entity.uuid(null).dateSynched(null);
  }

  private List<HiveLocation> getUserHiveLocationsOrDefault(User user) {
    List<HiveLocation> hiveLocations = repository.findByUserIsCurrentUser();
    if (hiveLocations.isEmpty()) {
      List<HiveLocation> hiveLocationsToCreate = repository.findByUserIsNull().stream()
          .filter(Objects::nonNull)
          .map(el -> mapHiveLocationToCreate(el, user))
          .toList();
      hiveLocations = repository.saveAll(hiveLocationsToCreate);
    }
    return hiveLocations;
  }

  private HiveLocation mapHiveLocationToCreate(HiveLocation hiveLocation, User user) {
    return new HiveLocation()
        .user(user)
        .uuid(UUID.randomUUID().toString())
        .dateCreated(DateTimeUtil.now())
        .dateModified(DateTimeUtil.now())
        .dateSynched(DateTimeUtil.now())
        .externalId(hiveLocation.getExternalId())
        .name(hiveLocation.getName());
  }

  /**
   * Save a hiveLocation.
   *
   * @param hiveLocationDTO the entity to save.
   * @return the persisted entity.
   */
  public HiveLocationDTO save(HiveLocationDTO hiveLocationDTO) {
    LOG.debug("Request to save HiveLocation : {}", hiveLocationDTO);
    HiveLocation hiveLocation = mapper.toEntity(hiveLocationDTO);
    hiveLocation = repository.save(hiveLocation);
    return mapper.toDto(hiveLocation);
  }

  /**
   * Update a hiveLocation.
   *
   * @param hiveLocationDTO the entity to save.
   * @return the persisted entity.
   */
  public HiveLocationDTO update(HiveLocationDTO hiveLocationDTO) {
    LOG.debug("Request to update HiveLocation : {}", hiveLocationDTO);
    HiveLocation hiveLocation = mapper.toEntity(hiveLocationDTO);
    hiveLocation = repository.save(hiveLocation);
    return mapper.toDto(hiveLocation);
  }

  /**
   * Partially update a hiveLocation.
   *
   * @param hiveLocationDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<HiveLocationDTO> partialUpdate(HiveLocationDTO hiveLocationDTO) {
    LOG.debug("Request to partially update HiveLocation : {}", hiveLocationDTO);

    return repository
        .findById(hiveLocationDTO.getId())
        .map(existingHiveLocation -> {
          mapper.partialUpdate(existingHiveLocation, hiveLocationDTO);

          return existingHiveLocation;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the hiveLocations.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<HiveLocationDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all HiveLocations");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one hiveLocation by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<HiveLocationDTO> findOne(Long id) {
    LOG.debug("Request to get HiveLocation : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the hiveLocation by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete HiveLocation : {}", id);
    repository.deleteById(id);
  }
}
