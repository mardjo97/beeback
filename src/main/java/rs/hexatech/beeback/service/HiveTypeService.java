package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.HiveType;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.exception.NotFoundException;
import rs.hexatech.beeback.repository.HiveTypeRepository;
import rs.hexatech.beeback.service.dto.HiveTypeDTO;
import rs.hexatech.beeback.service.mapper.HiveTypeMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.HiveType}.
 */
@Service
@Transactional
public class HiveTypeService {

  private static final Logger LOG = LoggerFactory.getLogger(HiveTypeService.class);

  private final HiveTypeRepository hiveTypeRepository;

  private final HiveTypeMapper hiveTypeMapper;

  @Autowired
  private SecurityService securityService;

  public HiveTypeService(HiveTypeRepository hiveTypeRepository, HiveTypeMapper hiveTypeMapper) {
    this.hiveTypeRepository = hiveTypeRepository;
    this.hiveTypeMapper = hiveTypeMapper;
  }

  public List<HiveTypeDTO> userHiveTypes(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's HiveTypes! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return hiveTypeMapper.toDto(getUserHiveTypesOrDefault(user));
  }

  public List<HiveTypeDTO> sync(List<HiveTypeDTO> hiveTypeDTOs) {
    LOG.debug("Request to sync HiveTypes : {}", hiveTypeDTOs);

    User user = securityService.getCurrentUser();
    return hiveTypeDTOs.stream().map(el -> syncHiveType(el, user))
        .map(hiveTypeMapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private HiveType syncHiveType(final HiveTypeDTO hiveTypeDto, final User user) {
    if (hiveTypeDto.getUuid() != null) {
      HiveType existingHiveType = hiveTypeRepository.findByUuidIs(hiveTypeDto.getUuid());
      if (existingHiveType == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", hiveTypeDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        HiveType mappedHiveType = hiveTypeMapper.toEntity(hiveTypeDto);
        toReset(mappedHiveType);
        return mappedHiveType;
      }
      hiveTypeMapper.partialUpdate(existingHiveType, hiveTypeDto);
      toUpdate(existingHiveType);
      return hiveTypeRepository.save(existingHiveType);
    }
    HiveType existingHiveType = hiveTypeRepository.findByUserAndExternalId(user, hiveTypeDto.getId().intValue());
    if (existingHiveType != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHiveType, hiveTypeDto.getUuid());
      LOG.warn("The entity {} is not synched", hiveTypeDto);
      return null;
    }
    HiveType toCreateHiveType = hiveTypeMapper.toEntity(hiveTypeDto);
    toCreate(toCreateHiveType, user);
    return hiveTypeRepository.save(toCreateHiveType);
  }

  public HiveType getHiveTypeByUUIDOrUserAndExternalId(String uuid, User user, Long externalId) {
    if (uuid != null) {
      HiveType e = hiveTypeRepository.findByUuidIs(uuid);
      if (e != null) {
        return e;
      }
    }
    if (user != null && externalId != null) {
      HiveType e = hiveTypeRepository.findByUserAndExternalId(user, externalId.intValue());
      if (e != null) {
        return e;
      }
    }
    throw new NotFoundException("HiveType not found for");
  }

  /**
   * Save a hiveType.
   *
   * @param hiveTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public HiveTypeDTO save(HiveTypeDTO hiveTypeDTO) {
    LOG.debug("Request to save HiveType : {}", hiveTypeDTO);
    HiveType hiveType = hiveTypeMapper.toEntity(hiveTypeDTO);
    hiveType = hiveTypeRepository.save(hiveType);
    return hiveTypeMapper.toDto(hiveType);
  }

  /**
   * Update a hiveType.
   *
   * @param hiveTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public HiveTypeDTO update(HiveTypeDTO hiveTypeDTO) {
    LOG.debug("Request to update HiveType : {}", hiveTypeDTO);
    HiveType hiveType = hiveTypeMapper.toEntity(hiveTypeDTO);
    hiveType = hiveTypeRepository.save(hiveType);
    return hiveTypeMapper.toDto(hiveType);
  }

  /**
   * Partially update a hiveType.
   *
   * @param hiveTypeDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<HiveTypeDTO> partialUpdate(HiveTypeDTO hiveTypeDTO) {
    LOG.debug("Request to partially update HiveType : {}", hiveTypeDTO);

    return hiveTypeRepository
        .findById(hiveTypeDTO.getId())
        .map(existingHiveType -> {
          hiveTypeMapper.partialUpdate(existingHiveType, hiveTypeDTO);

          return existingHiveType;
        })
        .map(hiveTypeRepository::save)
        .map(hiveTypeMapper::toDto);
  }

  /**
   * Get all the hiveTypes.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<HiveTypeDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all HiveTypes");
    return hiveTypeRepository.findAll(pageable).map(hiveTypeMapper::toDto);
  }

  /**
   * Get one hiveType by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<HiveTypeDTO> findOne(Long id) {
    LOG.debug("Request to get HiveType : {}", id);
    return hiveTypeRepository.findById(id).map(hiveTypeMapper::toDto);
  }

  /**
   * Delete the hiveType by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete HiveType : {}", id);
    hiveTypeRepository.deleteById(id);
  }

  private void toUpdate(final HiveType hiveType) {
    hiveType.dateSynched(DateTimeUtil.getDateSynced(hiveType.getDateModified()));
  }

  private void toCreate(final HiveType hiveType, final User user) {
    hiveType.user(user).uuid(UUID.randomUUID().toString()).dateSynched(Instant.now());
  }

  private void toReset(final HiveType hiveType) {
    hiveType.uuid(null).dateSynched(null);
  }

  private List<HiveType> getUserHiveTypesOrDefault(User user) {
    List<HiveType> hiveTypes = hiveTypeRepository.findByUserIsCurrentUser();
    if (hiveTypes.isEmpty()) {
      List<HiveType> hiveTypesToCreate = hiveTypeRepository.findByUserIsNull().stream()
          .filter(Objects::nonNull)
          .map(el -> mapHiveTypeToCreate(el, user))
          .toList();
      hiveTypes = hiveTypeRepository.saveAll(hiveTypesToCreate);
    }
    return hiveTypes;
  }

  private HiveType mapHiveTypeToCreate(HiveType hiveType, User user) {
    return new HiveType()
        .user(user)
        .uuid(UUID.randomUUID().toString())
        .dateCreated(DateTimeUtil.now())
        .dateModified(DateTimeUtil.now())
        .dateSynched(DateTimeUtil.now())
        .Name(hiveType.getName())
        .externalId(hiveType.getExternalId());
  }
}
