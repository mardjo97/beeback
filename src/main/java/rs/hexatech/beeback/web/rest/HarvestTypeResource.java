package rs.hexatech.beeback.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.hexatech.beeback.exception.DeviceIdForbiddenException;
import rs.hexatech.beeback.service.HarvestTypeService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.HarvestTypeDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

  private final HarvestTypeService service;

  @Autowired
  SecurityService securityService;

  public HarvestTypeResource(HarvestTypeService harvestTypeService) {
    this.service = harvestTypeService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<HarvestTypeDTO>> syncEntities(@RequestBody List<HarvestTypeDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<HarvestTypeDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<HarvestTypeDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<HarvestTypeDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
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
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (harvestTypeDTO.getId() != null) {
//      throw new BadRequestAlertException("A new harvestType cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    harvestTypeDTO = service.save(harvestTypeDTO);
//    return ResponseEntity.created(new URI("/api/harvest-types/" + harvestTypeDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, harvestTypeDTO.getId().toString()))
//        .body(harvestTypeDTO);
  }

  /**
   * {@code PUT  /harvest-types/:id} : Updates an existing harvestType.
   *
   * @param id             the id of the harvestTypeDTO to save.
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
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (harvestTypeDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, harvestTypeDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!harvestTypeRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    harvestTypeDTO = service.update(harvestTypeDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, harvestTypeDTO.getId().toString()))
//        .body(harvestTypeDTO);
  }

  /**
   * {@code PATCH  /harvest-types/:id} : Partial updates given fields of an existing harvestType, field will ignore if it is null
   *
   * @param id             the id of the harvestTypeDTO to save.
   * @param harvestTypeDTO the harvestTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated harvestTypeDTO,
   * or with status {@code 400 (Bad Request)} if the harvestTypeDTO is not valid,
   * or with status {@code 404 (Not Found)} if the harvestTypeDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the harvestTypeDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<HarvestTypeDTO> partialUpdateHarvestType(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody HarvestTypeDTO harvestTypeDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update HarvestType partially : {}, {}", id, harvestTypeDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (harvestTypeDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, harvestTypeDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!harvestTypeRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<HarvestTypeDTO> result = service.partialUpdate(harvestTypeDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, harvestTypeDTO.getId().toString())
//    );
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
    Page<HarvestTypeDTO> page = service.findAll(pageable);
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
    Optional<HarvestTypeDTO> harvestTypeDTO = service.findOne(id);
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
    throw new DeviceIdForbiddenException("Can not delete record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
