package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Group;
import rs.hexatech.beeback.repository.GroupRepository;
import rs.hexatech.beeback.service.dto.GroupDTO;
import rs.hexatech.beeback.service.mapper.GroupMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Group}.
 */
@Service
@Transactional
public class GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    /**
     * Save a group.
     *
     * @param groupDTO the entity to save.
     * @return the persisted entity.
     */
    public GroupDTO save(GroupDTO groupDTO) {
        LOG.debug("Request to save Group : {}", groupDTO);
        Group group = groupMapper.toEntity(groupDTO);
        group = groupRepository.save(group);
        return groupMapper.toDto(group);
    }

    /**
     * Update a group.
     *
     * @param groupDTO the entity to save.
     * @return the persisted entity.
     */
    public GroupDTO update(GroupDTO groupDTO) {
        LOG.debug("Request to update Group : {}", groupDTO);
        Group group = groupMapper.toEntity(groupDTO);
        group = groupRepository.save(group);
        return groupMapper.toDto(group);
    }

    /**
     * Partially update a group.
     *
     * @param groupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GroupDTO> partialUpdate(GroupDTO groupDTO) {
        LOG.debug("Request to partially update Group : {}", groupDTO);

        return groupRepository
            .findById(groupDTO.getId())
            .map(existingGroup -> {
                groupMapper.partialUpdate(existingGroup, groupDTO);

                return existingGroup;
            })
            .map(groupRepository::save)
            .map(groupMapper::toDto);
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
        return groupRepository.findAll(pageable).map(groupMapper::toDto);
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
        return groupRepository.findById(id).map(groupMapper::toDto);
    }

    /**
     * Delete the group by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }
}
