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
import rs.hexatech.beeback.service.HiveService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.HiveDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.Hive}.
 */
@RestController
@RequestMapping("/api/hives")
public class HiveResource {

  private static final Logger LOG = LoggerFactory.getLogger(HiveResource.class);

  private static final String ENTITY_NAME = "hive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final HiveService hiveService;

  @Autowired
  private SecurityService securityService;

  public HiveResource(HiveService hiveService) {
    this.hiveService = hiveService;
  }

  /**
   * {@code POST  /hives/sync} : Sync list of hives.
   *
   * @param hiveDTOs the hiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 200 } and with List of the hiveDTO, or with status {@code 400 (Bad Request)} if the hive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/sync")
  public ResponseEntity<List<HiveDTO>> syncHives(@RequestBody List<HiveDTO> hiveDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Hives : {}", hiveDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<HiveDTO> syncedHivesDTOs = hiveService.sync(hiveDTOs);
    return ResponseEntity.ok().body(syncedHivesDTOs);
  }

  /**
   * {@code GET  /hives/all} : Retrieve list of user's hives.
   *
   * @param deviceId to validate device id
   * @return the {@link ResponseEntity} with status {@code 200 } and with List of the hiveDTO, or with status {@code 400 (Bad Request)} if the hive has already an ID.
   */
  @GetMapping("/all")
  public ResponseEntity<List<HiveDTO>> userHives(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Hives");
    securityService.checkUserDeviceId(deviceId);
    List<HiveDTO> userHives = hiveService.userHives(deviceId);
    return ResponseEntity.ok().body(userHives);
  }

  /**
   * {@code POST  /hives} : Create a new hive.
   *
   * @param hiveDTO the hiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hiveDTO, or with status {@code 400 (Bad Request)} if the hive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<HiveDTO> createHive(@Valid @RequestBody HiveDTO hiveDTO) throws URISyntaxException {
    LOG.debug("REST request to save Hive : {}", hiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (hiveDTO.getId() != null) {
//      throw new BadRequestAlertException("A new hive cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    hiveDTO = hiveService.save(hiveDTO);
//    return ResponseEntity.created(new URI("/api/hives/" + hiveDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hiveDTO.getId().toString()))
//        .body(hiveDTO);
  }

  /**
   * {@code PUT  /hives/:id} : Updates an existing hive.
   *
   * @param id      the id of the hiveDTO to save.
   * @param hiveDTO the hiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiveDTO,
   * or with status {@code 400 (Bad Request)} if the hiveDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the hiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<HiveDTO> updateHive(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody HiveDTO hiveDTO
  ) throws URISyntaxException {
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    LOG.debug("REST request to update Hive : {}, {}", id, hiveDTO);
//    if (hiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, hiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!hiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    hiveDTO = hiveService.update(hiveDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hiveDTO.getId().toString()))
//        .body(hiveDTO);
  }

  /**
   * {@code PATCH  /hives/:id} : Partial updates given fields of an existing hive, field will ignore if it is null
   *
   * @param id      the id of the hiveDTO to save.
   * @param hiveDTO the hiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hiveDTO,
   * or with status {@code 400 (Bad Request)} if the hiveDTO is not valid,
   * or with status {@code 404 (Not Found)} if the hiveDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the hiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<HiveDTO> partialUpdateHive(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody HiveDTO hiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update Hive partially : {}, {}", id, hiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (hiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, hiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!hiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<HiveDTO> result = hiveService.partialUpdate(hiveDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hiveDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /hives} : get all the hives.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hives in body.
   */
  @GetMapping("")
  public ResponseEntity<List<HiveDTO>> getAllHives(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    LOG.debug("REST request to get a page of Hives");
    Page<HiveDTO> page = hiveService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /hives/:id} : get the "id" hive.
   *
   * @param id the id of the hiveDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hiveDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<HiveDTO> getHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to get Hive : {}", id);
    Optional<HiveDTO> hiveDTO = hiveService.findOne(id);
    return ResponseUtil.wrapOrNotFound(hiveDTO);
  }

  /**
   * {@code DELETE  /hives/:id} : delete the "id" hive.
   *
   * @param id the id of the hiveDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete Hive : {}", id);
    throw new DeviceIdForbiddenException("Can not delete record from this device");
//    hiveService.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
