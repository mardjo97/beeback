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
import rs.hexatech.beeback.repository.HiveTypeRepository;
import rs.hexatech.beeback.service.HiveTypeService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.HiveTypeDTO;
import rs.hexatech.beeback.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.HiveType}.
 */
@RestController
@RequestMapping("/api/hive-types")
public class HiveTypeResource {

  private static final Logger LOG = LoggerFactory.getLogger(HiveTypeResource.class);

  private static final String ENTITY_NAME = "hiveType";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final HiveTypeService hiveTypeService;

  private final HiveTypeRepository hiveTypeRepository;

  @Autowired
  private SecurityService securityService;

  public HiveTypeResource(HiveTypeService hiveTypeService, HiveTypeRepository hiveTypeRepository) {
    this.hiveTypeService = hiveTypeService;
    this.hiveTypeRepository = hiveTypeRepository;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<HiveTypeDTO>> syncHiveTypes(@RequestBody List<HiveTypeDTO> hiveTypeDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync HiveTypes : {}", hiveTypeDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<HiveTypeDTO> syncedHiveTypesDTOs = hiveTypeService.sync(hiveTypeDTOs);
    return ResponseEntity.ok().body(syncedHiveTypesDTOs);
  }

  /**
   * {@code GET  /hives/all} : Retrieve list of user's hives.
   *
   * @param deviceId to validate device id
   * @return the {@link ResponseEntity} with status {@code 200 } and with List of the hiveDTO, or with status {@code 400 (Bad Request)} if the hive has already an ID.
   */
  @GetMapping("/all")
  public ResponseEntity<List<HiveTypeDTO>> userHiveTypes(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's HiveTypes");
    securityService.checkUserDeviceId(deviceId);
    List<HiveTypeDTO> userHiveTypes = hiveTypeService.userHiveTypes(deviceId);
    return ResponseEntity.ok().body(userHiveTypes);
  }

  /**
   * {@code POST  /hive-types} : Create a new hiveType.
   *
   * @param hiveTypeDTO the hiveTypeDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hiveTypeDTO, or with status {@code 400 (Bad Request)} if the hiveType has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<HiveTypeDTO> createHiveType(@Valid @RequestBody HiveTypeDTO hiveTypeDTO) throws URISyntaxException {
    LOG.debug("REST request to save HiveType : {}", hiveTypeDTO);
    if (hiveTypeDTO.getId() != null) {
      throw new BadRequestAlertException("A new hiveType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    hiveTypeDTO = hiveTypeService.save(hiveTypeDTO);
    return ResponseEntity.created(new URI("/api/hive-types/" + hiveTypeDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hiveTypeDTO.getId().toString()))
        .body(hiveTypeDTO);
  }

  /**
   * {@code PUT  /hive-types/:id} : Updates an existing hiveType.
   *
   * @param id          the id of the hiveTypeDTO to save.
   * @param hiveTypeDTO the hiveTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiveTypeDTO,
   * or with status {@code 400 (Bad Request)} if the hiveTypeDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the hiveTypeDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<HiveTypeDTO> updateHiveType(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody HiveTypeDTO hiveTypeDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update HiveType : {}, {}", id, hiveTypeDTO);
    if (hiveTypeDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, hiveTypeDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!hiveTypeRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    hiveTypeDTO = hiveTypeService.update(hiveTypeDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hiveTypeDTO.getId().toString()))
        .body(hiveTypeDTO);
  }

  /**
   * {@code PATCH  /hive-types/:id} : Partial updates given fields of an existing hiveType, field will ignore if it is null
   *
   * @param id          the id of the hiveTypeDTO to save.
   * @param hiveTypeDTO the hiveTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiveTypeDTO,
   * or with status {@code 400 (Bad Request)} if the hiveTypeDTO is not valid,
   * or with status {@code 404 (Not Found)} if the hiveTypeDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the hiveTypeDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<HiveTypeDTO> partialUpdateHiveType(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody HiveTypeDTO hiveTypeDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update HiveType partially : {}, {}", id, hiveTypeDTO);
    if (hiveTypeDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, hiveTypeDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!hiveTypeRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<HiveTypeDTO> result = hiveTypeService.partialUpdate(hiveTypeDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hiveTypeDTO.getId().toString())
    );
  }

  /**
   * {@code GET  /hive-types} : get all the hiveTypes.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hiveTypes in body.
   */
  @GetMapping("")
  public ResponseEntity<List<HiveTypeDTO>> getAllHiveTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    LOG.debug("REST request to get a page of HiveTypes");
    Page<HiveTypeDTO> page = hiveTypeService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /hive-types/:id} : get the "id" hiveType.
   *
   * @param id the id of the hiveTypeDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hiveTypeDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<HiveTypeDTO> getHiveType(@PathVariable("id") Long id) {
    LOG.debug("REST request to get HiveType : {}", id);
    Optional<HiveTypeDTO> hiveTypeDTO = hiveTypeService.findOne(id);
    return ResponseUtil.wrapOrNotFound(hiveTypeDTO);
  }

  /**
   * {@code DELETE  /hive-types/:id} : delete the "id" hiveType.
   *
   * @param id the id of the hiveTypeDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHiveType(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete HiveType : {}", id);
    hiveTypeService.delete(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
