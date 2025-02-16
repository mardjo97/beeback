package rs.hexatech.beeback.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Group;
import rs.hexatech.beeback.domain.User;
import rs.hexatech.beeback.repository.GroupRepository;
import rs.hexatech.beeback.service.dto.GroupDTO;
import rs.hexatech.beeback.service.mapper.GroupMapper;
import rs.hexatech.beeback.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Group}.
 */
@Service
@Transactional
public class GroupService {

  private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);

  private final GroupRepository repository;

  private final GroupMapper mapper;

  @Autowired
  SecurityService securityService;

  public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
    this.repository = groupRepository;
    this.mapper = groupMapper;
  }

  public List<GroupDTO> userEntities(String deviceId) {
    User user = securityService.getCurrentUser();
    LOG.debug("Request to retrieve user's Groups! User {}, DeviceId: {}", user.getLogin(), deviceId);
    return mapper.toDto(getUserGroupsOrDefault(user));
  }

  public List<GroupDTO> sync(List<GroupDTO> entityDTOs) {
    User user = securityService.getCurrentUser();
    return entityDTOs.stream().map(el -> syncEntity(el, user))
        .map(mapper::toDto)
        .filter(Objects::nonNull)
        .toList();
  }

  private Group syncEntity(final GroupDTO entityDto, final User user) {
    if (entityDto.getUuid() != null) {
      Group existingEntity = repository.findByUuidIs(entityDto.getUuid());
      if (existingEntity == null) {
        LOG.error("The entity does not exist, but there is uuid in the request body {}", entityDto);
        LOG.error("Returning the response with uuid and dateSynched as nulls!");
        Group mappedEntity = mapper.toEntity(entityDto);
        toReset(mappedEntity);
        return mappedEntity;
      }
      mapper.partialUpdate(existingEntity, entityDto);
      if (!toUpdate(existingEntity, entityDto, user)) {
        return null;
      }
      return repository.save(existingEntity);
    }
    Group existingHive = repository.findByUserAndExternalId(user, entityDto.getId().intValue());
    if (existingHive != null) {
      LOG.error("The entity {} exists but in the request the uuid is {}", existingHive, entityDto.getUuid());
      LOG.warn("The entity {} is not synched", entityDto);
      return null;
    }
    Group toCreateEntity = mapper.toEntity(entityDto);
    if (!toCreate(toCreateEntity, entityDto, user)) {
      return null;
    }
    return repository.save(toCreateEntity);
  }

  private boolean toUpdate(final Group entity, final GroupDTO entityDto, final User user) {
    entity.dateSynched(DateTimeUtil.getDateSynced(entity.getDateModified()));
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private boolean toCreate(final Group entity, final GroupDTO entityDto, final User user) {
    entity.user(user).uuid(UUID.randomUUID().toString()).dateSynched(DateTimeUtil.now());
//    return handleRelations(entity, entityDto, user);
    return true;
  }

  private void toReset(final Group entity) {
    entity.uuid(null).dateSynched(null);
  }

  private List<Group> getUserGroupsOrDefault(User user) {
    List<Group> groups = repository.findByUserIsCurrentUser();
    if (groups.isEmpty()) {
      List<Group> groupsToCreate = repository.findByUserIsNull().stream()
          .filter(Objects::nonNull)
          .map(el -> mapGroupToCreate(el, user))
          .toList();
      groups = repository.saveAll(groupsToCreate);
    }
    return groups;
  }

  private Group mapGroupToCreate(Group group, User user) {
    return new Group()
        .user(user)
        .uuid(UUID.randomUUID().toString())
        .dateCreated(DateTimeUtil.now())
        .dateModified(DateTimeUtil.now())
        .dateSynched(DateTimeUtil.now())
        .externalId(group.getExternalId())
        .name(group.getName())
        .enumValueName(group.getEnumValueName())
        .color(group.getColor())
        .orderNumber(group.getOrderNumber())
        .hiveCount(group.getHiveCount())
        .hiveCountFinished(group.getHiveCountFinished())
        .additionalInfo(group.getAdditionalInfo());
  }

  /**
   * Save a group.
   *
   * @param groupDTO the entity to save.
   * @return the persisted entity.
   */
  public GroupDTO save(GroupDTO groupDTO) {
    LOG.debug("Request to save Group : {}", groupDTO);
    Group group = mapper.toEntity(groupDTO);
    group = repository.save(group);
    return mapper.toDto(group);
  }

  /**
   * Update a group.
   *
   * @param groupDTO the entity to save.
   * @return the persisted entity.
   */
  public GroupDTO update(GroupDTO groupDTO) {
    LOG.debug("Request to update Group : {}", groupDTO);
    Group group = mapper.toEntity(groupDTO);
    group = repository.save(group);
    return mapper.toDto(group);
  }

  /**
   * Partially update a group.
   *
   * @param groupDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<GroupDTO> partialUpdate(GroupDTO groupDTO) {
    LOG.debug("Request to partially update Group : {}", groupDTO);

    return repository
        .findById(groupDTO.getId())
        .map(existingGroup -> {
          mapper.partialUpdate(existingGroup, groupDTO);

          return existingGroup;
        })
        .map(repository::save)
        .map(mapper::toDto);
  }

  /**
   * Get all the groups.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<GroupDTO> findAll(Pageable pageable) {
    LOG.debug("Request to get all Groups");
    return repository.findAll(pageable).map(mapper::toDto);
  }

  /**
   * Get one group by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<GroupDTO> findOne(Long id) {
    LOG.debug("Request to get Group : {}", id);
    return repository.findById(id).map(mapper::toDto);
  }

  /**
   * Delete the group by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    LOG.debug("Request to delete Group : {}", id);
    repository.deleteById(id);
  }
}
