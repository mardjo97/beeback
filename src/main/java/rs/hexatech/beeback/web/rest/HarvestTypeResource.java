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
import rs.hexatech.beeback.repository.HarvestTypeRepository;
import rs.hexatech.beeback.service.HarvestTypeService;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import rs.hexatech.beeback.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.HarvestType}.
 */
@RestController
@RequestMapping("/api/harvest-types")
public class HarvestTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(HarvestTypeResource.class);

    private static final String ENTITY_NAME = "harvestType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HarvestTypeService harvestTypeService;

    private final HarvestTypeRepository harvestTypeRepository;

    public HarvestTypeResource(HarvestTypeService harvestTypeService, HarvestTypeRepository harvestTypeRepository) {
        this.harvestTypeService = harvestTypeService;
        this.harvestTypeRepository = harvestTypeRepository;
    }

    /**
     * {@code POST  /harvest-types} : Create a new harvestType.
     *
     * @param harvestTypeDTO the harvestTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new harvestTypeDTO, or with status {@code 400 (Bad Request)} if the harvestType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HarvestTypeDTO> createHarvestType(@Valid @RequestBody HarvestTypeDTO harvestTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save HarvestType : {}", harvestTypeDTO);
        if (harvestTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new harvestType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        harvestTypeDTO = harvestTypeService.save(harvestTypeDTO);
        return ResponseEntity.created(new URI("/api/harvest-types/" + harvestTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, harvestTypeDTO.getId().toString()))
            .body(harvestTypeDTO);
    }

    /**
     * {@code PUT  /harvest-types/:id} : Updates an existing harvestType.
     *
     * @param id the id of the harvestTypeDTO to save.
     * @param harvestTypeDTO the harvestTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated harvestTypeDTO,
     * or with status {@code 400 (Bad Request)} if the harvestTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the harvestTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HarvestTypeDTO> updateHarvestType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HarvestTypeDTO harvestTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HarvestType : {}, {}", id, harvestTypeDTO);
        if (harvestTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, harvestTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!harvestTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        harvestTypeDTO = harvestTypeService.update(harvestTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, harvestTypeDTO.getId().toString()))
            .body(harvestTypeDTO);
    }

    /**
     * {@code PATCH  /harvest-types/:id} : Partial updates given fields of an existing harvestType, field will ignore if it is null
     *
     * @param id the id of the harvestTypeDTO to save.
     * @param harvestTypeDTO the harvestTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated harvestTypeDTO,
     * or with status {@code 400 (Bad Request)} if the harvestTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the harvestTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the harvestTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HarvestTypeDTO> partialUpdateHarvestType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HarvestTypeDTO harvestTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HarvestType partially : {}, {}", id, harvestTypeDTO);
        if (harvestTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, harvestTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!harvestTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HarvestTypeDTO> result = harvestTypeService.partialUpdate(harvestTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, harvestTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /harvest-types} : get all the harvestTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of harvestTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HarvestTypeDTO>> getAllHarvestTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of HarvestTypes");
        Page<HarvestTypeDTO> page = harvestTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /harvest-types/:id} : get the "id" harvestType.
     *
     * @param id the id of the harvestTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the harvestTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HarvestTypeDTO> getHarvestType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HarvestType : {}", id);
        Optional<HarvestTypeDTO> harvestTypeDTO = harvestTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(harvestTypeDTO);
    }

    /**
     * {@code DELETE  /harvest-types/:id} : delete the "id" harvestType.
     *
     * @param id the id of the harvestTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvestType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HarvestType : {}", id);
        harvestTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
