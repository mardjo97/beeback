package rs.hexatech.beeback.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hexatech.beeback.domain.ExaminationHive;
import rs.hexatech.beeback.repository.ExaminationHiveRepository;
import rs.hexatech.beeback.service.dto.ExaminationHiveDTO;
import rs.hexatech.beeback.service.mapper.ExaminationHiveMapper;

/**
 * Service Implementation for managing {@link rs.hexatech.beeback.domain.ExaminationHive}.
 */
@Service
@Transactional
public class ExaminationHiveService {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationHiveService.class);

    private final ExaminationHiveRepository examinationHiveRepository;

    private final ExaminationHiveMapper examinationHiveMapper;

    public ExaminationHiveService(ExaminationHiveRepository examinationHiveRepository, ExaminationHiveMapper examinationHiveMapper) {
        this.examinationHiveRepository = examinationHiveRepository;
        this.examinationHiveMapper = examinationHiveMapper;
    }

    /**
     * Save a examinationHive.
     *
     * @param examinationHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public ExaminationHiveDTO save(ExaminationHiveDTO examinationHiveDTO) {
        LOG.debug("Request to save ExaminationHive : {}", examinationHiveDTO);
        ExaminationHive examinationHive = examinationHiveMapper.toEntity(examinationHiveDTO);
        examinationHive = examinationHiveRepository.save(examinationHive);
        return examinationHiveMapper.toDto(examinationHive);
    }

    /**
     * Update a examinationHive.
     *
     * @param examinationHiveDTO the entity to save.
     * @return the persisted entity.
     */
    public ExaminationHiveDTO update(ExaminationHiveDTO examinationHiveDTO) {
        LOG.debug("Request to update ExaminationHive : {}", examinationHiveDTO);
        ExaminationHive examinationHive = examinationHiveMapper.toEntity(examinationHiveDTO);
        examinationHive = examinationHiveRepository.save(examinationHive);
        return examinationHiveMapper.toDto(examinationHive);
    }

    /**
     * Partially update a examinationHive.
     *
     * @param examinationHiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExaminationHiveDTO> partialUpdate(ExaminationHiveDTO examinationHiveDTO) {
        LOG.debug("Request to partially update ExaminationHive : {}", examinationHiveDTO);

        return examinationHiveRepository
            .findById(examinationHiveDTO.getId())
            .map(existingExaminationHive -> {
                examinationHiveMapper.partialUpdate(existingExaminationHive, examinationHiveDTO);

                return existingExaminationHive;
            })
            .map(examinationHiveRepository::save)
            .map(examinationHiveMapper::toDto);
    }

    /**
     * Get all the examinationHives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExaminationHiveDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ExaminationHives");
        return examinationHiveRepository.findAll(pageable).map(examinationHiveMapper::toDto);
    }

    /**
     * Get one examinationHive by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExaminationHiveDTO> findOne(Long id) {
        LOG.debug("Request to get ExaminationHive : {}", id);
        return examinationHiveRepository.findById(id).map(examinationHiveMapper::toDto);
    }

    /**
     * Delete the examinationHive by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ExaminationHive : {}", id);
        examinationHiveRepository.deleteById(id);
    }
}
