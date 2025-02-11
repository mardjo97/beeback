package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.MovedHive;
import rs.hexatech.beeback.repository.MovedHiveRepository;
import rs.hexatech.beeback.service.dto.MovedHiveDTO;
import rs.hexatech.beeback.service.mapper.MovedHiveMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.MovedHive}.
 */
@Service
@Transactional
public class MovedHiveService {

    private static final Logger LOG = LoggerFactory.getLogger(MovedHiveService.class);

    private final MovedHiveRepository movedHiveRepository;

    private final MovedHiveMapper movedHiveMapper;

    public MovedHiveService(MovedHiveRepository movedHiveRepository, MovedHiveMapper movedHiveMapper) {
        this.movedHiveRepository = movedHiveRepository;
        this.movedHiveMapper = movedHiveMapper;
    }

    /**
     * Save a movedHive.
     *
     * @param movedHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public MovedHiveDTO save(MovedHiveDTO movedHiveDTO) {
        LOG.debug("Request to save MovedHive : {}", movedHiveDTO);
        MovedHive movedHive = movedHiveMapper.toEntity(movedHiveDTO);
        movedHive = movedHiveRepository.save(movedHive);
        return movedHiveMapper.toDto(movedHive);
    }

    /**
     * Update a movedHive.
     *
     * @param movedHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public MovedHiveDTO update(MovedHiveDTO movedHiveDTO) {
        LOG.debug("Request to update MovedHive : {}", movedHiveDTO);
        MovedHive movedHive = movedHiveMapper.toEntity(movedHiveDTO);
        movedHive = movedHiveRepository.save(movedHive);
        return movedHiveMapper.toDto(movedHive);
    }

    /**
     * Partially update a movedHive.
     *
     * @param movedHiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MovedHiveDTO> partialUpdate(MovedHiveDTO movedHiveDTO) {
        LOG.debug("Request to partially update MovedHive : {}", movedHiveDTO);

        return movedHiveRepository
            .findById(movedHiveDTO.getId())
            .map(existingMovedHive -> {
                movedHiveMapper.partialUpdate(existingMovedHive, movedHiveDTO);

                return existingMovedHive;
            })
            .map(movedHiveRepository::save)
            .map(movedHiveMapper::toDto);
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
        return movedHiveRepository.findAll(pageable).map(movedHiveMapper::toDto);
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
        return movedHiveRepository.findById(id).map(movedHiveMapper::toDto);
    }

    /**
     * Delete the movedHive by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MovedHive : {}", id);
        movedHiveRepository.deleteById(id);
    }
}
