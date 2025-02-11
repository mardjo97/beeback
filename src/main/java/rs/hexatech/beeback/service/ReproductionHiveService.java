package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.ReproductionHive;
import rs.hexatech.beeback.repository.ReproductionHiveRepository;
import rs.hexatech.beeback.service.dto.ReproductionHiveDTO;
import rs.hexatech.beeback.service.mapper.ReproductionHiveMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.ReproductionHive}.
 */
@Service
@Transactional
public class ReproductionHiveService {

    private static final Logger LOG = LoggerFactory.getLogger(ReproductionHiveService.class);

    private final ReproductionHiveRepository reproductionHiveRepository;

    private final ReproductionHiveMapper reproductionHiveMapper;

    public ReproductionHiveService(ReproductionHiveRepository reproductionHiveRepository, ReproductionHiveMapper reproductionHiveMapper) {
        this.reproductionHiveRepository = reproductionHiveRepository;
        this.reproductionHiveMapper = reproductionHiveMapper;
    }

    /**
     * Save a reproductionHive.
     *
     * @param reproductionHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public ReproductionHiveDTO save(ReproductionHiveDTO reproductionHiveDTO) {
        LOG.debug("Request to save ReproductionHive : {}", reproductionHiveDTO);
        ReproductionHive reproductionHive = reproductionHiveMapper.toEntity(reproductionHiveDTO);
        reproductionHive = reproductionHiveRepository.save(reproductionHive);
        return reproductionHiveMapper.toDto(reproductionHive);
    }

    /**
     * Update a reproductionHive.
     *
     * @param reproductionHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public ReproductionHiveDTO update(ReproductionHiveDTO reproductionHiveDTO) {
        LOG.debug("Request to update ReproductionHive : {}", reproductionHiveDTO);
        ReproductionHive reproductionHive = reproductionHiveMapper.toEntity(reproductionHiveDTO);
        reproductionHive = reproductionHiveRepository.save(reproductionHive);
        return reproductionHiveMapper.toDto(reproductionHive);
    }

    /**
     * Partially update a reproductionHive.
     *
     * @param reproductionHiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReproductionHiveDTO> partialUpdate(ReproductionHiveDTO reproductionHiveDTO) {
        LOG.debug("Request to partially update ReproductionHive : {}", reproductionHiveDTO);

        return reproductionHiveRepository
            .findById(reproductionHiveDTO.getId())
            .map(existingReproductionHive -> {
                reproductionHiveMapper.partialUpdate(existingReproductionHive, reproductionHiveDTO);

                return existingReproductionHive;
            })
            .map(reproductionHiveRepository::save)
            .map(reproductionHiveMapper::toDto);
    }

    /**
     * Get all the reproductionHives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReproductionHiveDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ReproductionHives");
        return reproductionHiveRepository.findAll(pageable).map(reproductionHiveMapper::toDto);
    }

    /**
     * Get one reproductionHive by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReproductionHiveDTO> findOne(Long id) {
        LOG.debug("Request to get ReproductionHive : {}", id);
        return reproductionHiveRepository.findById(id).map(reproductionHiveMapper::toDto);
    }

    /**
     * Delete the reproductionHive by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReproductionHive : {}", id);
        reproductionHiveRepository.deleteById(id);
    }
}
