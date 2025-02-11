package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.Queen;
import rs.hexatech.beeback.repository.QueenRepository;
import rs.hexatech.beeback.service.dto.QueenDTO;
import rs.hexatech.beeback.service.mapper.QueenMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.Queen}.
 */
@Service
@Transactional
public class QueenService {

    private static final Logger LOG = LoggerFactory.getLogger(QueenService.class);

    private final QueenRepository queenRepository;

    private final QueenMapper queenMapper;

    public QueenService(QueenRepository queenRepository, QueenMapper queenMapper) {
        this.queenRepository = queenRepository;
        this.queenMapper = queenMapper;
    }

    /**
     * Save a queen.
     *
     * @param queenDTO the entity to save.
     * @return the persisted entity.
     */
    public QueenDTO save(QueenDTO queenDTO) {
        LOG.debug("Request to save Queen : {}", queenDTO);
        Queen queen = queenMapper.toEntity(queenDTO);
        queen = queenRepository.save(queen);
        return queenMapper.toDto(queen);
    }

    /**
     * Update a queen.
     *
     * @param queenDTO the entity to save.
     * @return the persisted entity.
     */
    public QueenDTO update(QueenDTO queenDTO) {
        LOG.debug("Request to update Queen : {}", queenDTO);
        Queen queen = queenMapper.toEntity(queenDTO);
        queen = queenRepository.save(queen);
        return queenMapper.toDto(queen);
    }

    /**
     * Partially update a queen.
     *
     * @param queenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QueenDTO> partialUpdate(QueenDTO queenDTO) {
        LOG.debug("Request to partially update Queen : {}", queenDTO);

        return queenRepository
            .findById(queenDTO.getId())
            .map(existingQueen -> {
                queenMapper.partialUpdate(existingQueen, queenDTO);

                return existingQueen;
            })
            .map(queenRepository::save)
            .map(queenMapper::toDto);
    }

    /**
     * Get all the queens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QueenDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Queens");
        return queenRepository.findAll(pageable).map(queenMapper::toDto);
    }

    /**
     * Get one queen by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QueenDTO> findOne(Long id) {
        LOG.debug("Request to get Queen : {}", id);
        return queenRepository.findById(id).map(queenMapper::toDto);
    }

    /**
     * Delete the queen by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Queen : {}", id);
        queenRepository.deleteById(id);
    }
}
