package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Hive;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.exception.NotFoundException;
import rs.hexatech.beeback.repository.HiveRepository;
import rs.hexatech.beeback.service.dto.HiveDTO;
import rs.hexatech.beeback.service.mapper.HiveMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Hive}.
 */
@Service
@Transactional
public class HiveService {

  private static final Logger LOG = LoggerFactory.getLogger(HiveService.class);

  private final HiveRepository hiveRepository;

  private final HiveMapper hiveMapper;

  @Autowired
  SecurityService securityService;

  @Autowired
  ApiaryService apiaryService;

  @Autowired
  HiveTypeService hiveTypeService;

  public HiveService(HiveRepository hiveRepository, HiveMapper hiveMapper) {
    this.hiveRepository = hiveRepository;
    this.hiveMapper = hiveMapper;
  }

  /**
   * Retrieve hives.
   *
   * @param deviceId to check the device.
   * @return the persisted entity.
   */
  public List<HiveDTO> userHives(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's Hives! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return hiveMapper.toDto(hiveRepository.findByUserIsCurrentUser());
  }

  /**
   * Sync hives.
   *
   * @param hiveDTOs the entity to save.
   * @return the persisted entity.
   */
  public List<HiveDTO> sync(List<HiveDTO> hiveDTOs) {
    LOG.debug("Request to sync Hives : {}", hiveDTOs);

    User user = securityService.getCurrentUser();
    return hiveDTOs.stream().map(el -> syncHive(el, user))
        .map(hiveMapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private Hive syncHive(final HiveDTO hiveDto, final User user) {
    if (hiveDto.getUuid() != null) {
      Hive existingHive = hiveRepository.findByUuidIs(hiveDto.getUuid());
      if (existingHive == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", hiveDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        Hive mappedHive = hiveMapper.toEntity(hiveDto);
        toReset(mappedHive);
        return mappedHive;
      }
      hiveMapper.partialUpdate(existingHive, hiveDto);
      if (!toUpdate(existingHive, hiveDto, user)) {
        return null;
      }
      return hiveRepository.save(existingHive);
    }
    Hive existingHive = hiveRepository.findByUserAndExternalId(user, hiveDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, hiveDto.getUuid());
      LOG.warn("The entity {} is not synched", hiveDto);
      return null;
    }
    Hive toCreateHive = hiveMapper.toEntity(hiveDto);
    if (!toCreate(toCreateHive, hiveDto, user)) {
      return null;
    }
    return hiveRepository.save(toCreateHive);
  }

  public Hive getHiveByUUIDOrUserAndExternalId(String uuid, User user, Long externalId) {
    if (uuid != null) {
      Hive e = hiveRepository.findByUuidIs(uuid);
      if (e != null) {
        return e;
      }
    }
    if (user != null && externalId != null) {
      Hive e = hiveRepository.findByUserAndExternalId(user, externalId.intValue());
      if (e != null) {
        return e;
      }
    }
    throw new NotFoundException("Hive not found for uuid: " + uuid + " or externalId: " + externalId + " and user: " + user);
  }

  /**
   * Save a hive.
   *
   * @param hiveDTO the entity to save.
   * @return the persisted entity.
   */
  public HiveDTO save(HiveDTO hiveDTO) {
    LOG.debug("Request to save Hive : {}", hiveDTO);
    Hive hive = hiveMapper.toEntity(hiveDTO);
    hive = hiveRepository.save(hive);
    return hiveMapper.toDto(hive);
  }

  /**
   * Update a hive.
   *
   * @param hiveDTO the entity to save.
   * @return the persisted entity.
   */
  public HiveDTO update(HiveDTO hiveDTO) {
    LOG.debug("Request to update Hive : {}", hiveDTO);
    Hive hive = hiveMapper.toEntity(hiveDTO);
    hive = hiveRepository.save(hive);
    return hiveMapper.toDto(hive);
  }

  /**
   * Partially update a hive.
   *
   * @param hiveDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<HiveDTO> partialUpdate(HiveDTO hiveDTO) {
    LOG.debug("Request to partially update Hive : {}", hiveDTO);

    return hiveRepository
        .findById(hiveDTO.getId())
        .map(existingHive -> {
          hiveMapper.partialUpdate(existingHive, hiveDTO);

          return existingHive;
        })
        .map(hiveRepository::save)
        .map(hiveMapper::toDto);
  }

  /**
   * Get all the hives.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<HiveDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all Hives");
    return hiveRepository.findAll(pageable).map(hiveMapper::toDto);
  }

  /**
   * Get one hive by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<HiveDTO> findOne(Long id) {
    LOG.debug("Request to get Hive : {}", id);
    return hiveRepository.findById(id).map(hiveMapper::toDto);
  }

  /**
   * Delete the hive by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete Hive : {}", id);
    hiveRepository.deleteById(id);
  }

  private boolean toUpdate(final Hive hive, final HiveDTO hiveDTO, final User user) {
    hive.dateSynched(DateTimeUtil.getDateSynced(hive.getDateModified()));
    return handleRelations(hive, hiveDTO, user);
  }

  private boolean toCreate(final Hive hive, final HiveDTO hiveDTO, final User user) {
    hive.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
    return handleRelations(hive, hiveDTO, user);
  }

  private void toReset(final Hive hive) {
    hive.uuid(null).dateSynched(null);
  }

  private boolean handleRelations(Hive hive, HiveDTO hiveDto, User user) {
    try {
      String apiaryUUID = hiveDto.getBeeyard() != null ? hiveDto.getBeeyard().getUuid() : null;
      Long apiaryExternalId = hiveDto.getBeeyard() != null ? hiveDto.getBeeyard().getId() : null;
      hive.setApiary(apiaryService.getApiaryByUUIDOrUserAndExternalId(apiaryUUID, user, apiaryExternalId));
    } catch (Exception e) {
      LOG.error("Can not map apiary for the hive: {}", hiveDto);
      return false;
    }
    try {
      String hiveTypeUUID = hiveDto.getHiveType() != null ? hiveDto.getHiveType().getUuid() : null;
      Long hiveTypeExternalId = hiveDto.getHiveType() != null ? hiveDto.getHiveType().getId() : null;
      hive.setHiveType(hiveTypeService.getHiveTypeByUUIDOrUserAndExternalId(hiveTypeUUID, user, hiveTypeExternalId));
    } catch (Exception e) {
      LOG.error("Can not map hive type for the hive: {}", hiveDto);
      return false;
    }
    return true;
  }
}
