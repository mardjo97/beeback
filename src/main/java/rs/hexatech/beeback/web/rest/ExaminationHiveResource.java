package rs.hexatech.beeback.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.hexatech.beeback.repository.ExaminationHiveRepository;
import rs.hexatech.beeback.service.ExaminationHiveService;
import rs.hexatech.beeback.service.dto.ExaminationHiveDTO;
import rs.hexatech.beeback.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.ExaminationHive}.
 */
@RestController
@RequestMapping("/api/examination-hives")
public class ExaminationHiveResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExaminationHiveResource.class);

    private static final String ENTITY_NAME = "examinationHive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExaminationHiveService examinationHiveService;

    private final ExaminationHiveRepository examinationHiveRepository;

    public ExaminationHiveResource(ExaminationHiveService examinationHiveService, ExaminationHiveRepository examinationHiveRepository) {
        this.examinationHiveService = examinationHiveService;
        this.examinationHiveRepository = examinationHiveRepository;
    }

    /**
     * {@code POST  /examination-hives} : Create a new examinationHive.
     *
     * @param examinationHiveDTO the examinationHiveDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examinationHiveDTO, or with status {@code 400 (Bad Request)} if the examinationHive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExaminationHiveDTO> createExaminationHive(@Valid @RequestBody ExaminationHiveDTO examinationHiveDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExaminationHive : {}", examinationHiveDTO);
        if (examinationHiveDTO.getId() != null) {
            throw new BadRequestAlertException("A new examinationHive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        examinationHiveDTO = examinationHiveService.save(examinationHiveDTO);
        return ResponseEntity.created(new URI("/api/examination-hives/" + examinationHiveDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, examinationHiveDTO.getId().toString()))
            .body(examinationHiveDTO);
    }

    /**
     * {@code PUT  /examination-hives/:id} : Updates an existing examinationHive.
     *
     * @param id the id of the examinationHiveDTO to save.
     * @param examinationHiveDTO the examinationHiveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationHiveDTO,
     * or with status {@code 400 (Bad Request)} if the examinationHiveDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examinationHiveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExaminationHiveDTO> updateExaminationHive(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExaminationHiveDTO examinationHiveDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExaminationHive : {}, {}", id, examinationHiveDTO);
        if (examinationHiveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationHiveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationHiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        examinationHiveDTO = examinationHiveService.update(examinationHiveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examinationHiveDTO.getId().toString()))
            .body(examinationHiveDTO);
    }

    /**
     * {@code PATCH  /examination-hives/:id} : Partial updates given fields of an existing examinationHive, field will ignore if it is null
     *
     * @param id the id of the examinationHiveDTO to save.
     * @param examinationHiveDTO the examinationHiveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examinationHiveDTO,
     * or with status {@code 400 (Bad Request)} if the examinationHiveDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examinationHiveDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examinationHiveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExaminationHiveDTO> partialUpdateExaminationHive(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExaminationHiveDTO examinationHiveDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExaminationHive partially : {}, {}", id, examinationHiveDTO);
        if (examinationHiveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examinationHiveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examinationHiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExaminationHiveDTO> result = examinationHiveService.partialUpdate(examinationHiveDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examinationHiveDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /examination-hives} : get all the examinationHives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examinationHives in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExaminationHiveDTO>> getAllExaminationHives(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ExaminationHives");
        Page<ExaminationHiveDTO> page = examinationHiveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /examination-hives/:id} : get the "id" examinationHive.
     *
     * @param id the id of the examinationHiveDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examinationHiveDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExaminationHiveDTO> getExaminationHive(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExaminationHive : {}", id);
        Optional<ExaminationHiveDTO> examinationHiveDTO = examinationHiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examinationHiveDTO);
    }

    /**
     * {@code DELETE  /examination-hives/:id} : delete the "id" examinationHive.
     *
     * @param id the id of the examinationHiveDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExaminationHive(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExaminationHive : {}", id);
        examinationHiveService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
