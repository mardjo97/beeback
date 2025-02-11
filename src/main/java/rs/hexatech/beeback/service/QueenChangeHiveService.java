package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.QueenChangeHive;
import rs.hexatech.beeback.repository.QueenChangeHiveRepository;
import rs.hexatech.beeback.service.dto.QueenChangeHiveDTO;
import rs.hexatech.beeback.service.mapper.QueenChangeHiveMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.QueenChangeHive}.
 */
@Service
@Transactional
public class QueenChangeHiveService {

    private static final Logger LOG = LoggerFactory.getLogger(QueenChangeHiveService.class);

    private final QueenChangeHiveRepository queenChangeHiveRepository;

    private final QueenChangeHiveMapper queenChangeHiveMapper;

    public QueenChangeHiveService(QueenChangeHiveRepository queenChangeHiveRepository, QueenChangeHiveMapper queenChangeHiveMapper) {
        this.queenChangeHiveRepository = queenChangeHiveRepository;
        this.queenChangeHiveMapper = queenChangeHiveMapper;
    }

    /**
     * Save a queenChangeHive.
     *
     * @param queenChangeHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public QueenChangeHiveDTO save(QueenChangeHiveDTO queenChangeHiveDTO) {
        LOG.debug("Request to save QueenChangeHive : {}", queenChangeHiveDTO);
        QueenChangeHive queenChangeHive = queenChangeHiveMapper.toEntity(queenChangeHiveDTO);
        queenChangeHive = queenChangeHiveRepository.save(queenChangeHive);
        return queenChangeHiveMapper.toDto(queenChangeHive);
    }

    /**
     * Update a queenChangeHive.
     *
     * @param queenChangeHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public QueenChangeHiveDTO update(QueenChangeHiveDTO queenChangeHiveDTO) {
        LOG.debug("Request to update QueenChangeHive : {}", queenChangeHiveDTO);
        QueenChangeHive queenChangeHive = queenChangeHiveMapper.toEntity(queenChangeHiveDTO);
        queenChangeHive = queenChangeHiveRepository.save(queenChangeHive);
        return queenChangeHiveMapper.toDto(queenChangeHive);
    }

    /**
     * Partially update a queenChangeHive.
     *
     * @param queenChangeHiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QueenChangeHiveDTO> partialUpdate(QueenChangeHiveDTO queenChangeHiveDTO) {
        LOG.debug("Request to partially update QueenChangeHive : {}", queenChangeHiveDTO);

        return queenChangeHiveRepository
            .findById(queenChangeHiveDTO.getId())
            .map(existingQueenChangeHive -> {
                queenChangeHiveMapper.partialUpdate(existingQueenChangeHive, queenChangeHiveDTO);

                return existingQueenChangeHive;
            })
            .map(queenChangeHiveRepository::save)
            .map(queenChangeHiveMapper::toDto);
    }

    /**
     * Get all the queenChangeHives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QueenChangeHiveDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all QueenChangeHives");
        return queenChangeHiveRepository.findAll(pageable).map(queenChangeHiveMapper::toDto);
    }

    /**
     * Get one queenChangeHive by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QueenChangeHiveDTO> findOne(Long id) {
        LOG.debug("Request to get QueenChangeHive : {}", id);
        return queenChangeHiveRepository.findById(id).map(queenChangeHiveMapper::toDto);
    }

    /**
     * Delete the queenChangeHive by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete QueenChangeHive : {}", id);
        queenChangeHiveRepository.deleteById(id);
    }
}
