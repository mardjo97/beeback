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
import rs.hexatech.beeback.service.GoodHarvestHiveService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.GoodHarvestHiveDTO;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.GoodHarvestHive}.
 */
@RestController
@RequestMapping("/api/good-harvest-hives")
public class GoodHarvestHiveResource {

  private static final Logger LOG = LoggerFactory.getLogger(GoodHarvestHiveResource.class);

  private static final String ENTITY_NAME = "goodHarvestHive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final GoodHarvestHiveService service;

  @Autowired
  SecurityService securityService;

  public GoodHarvestHiveResource(GoodHarvestHiveService goodHarvestHiveService) {
    this.service = goodHarvestHiveService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<GoodHarvestHiveDTO>> syncEntities(@RequestBody List<GoodHarvestHiveDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<GoodHarvestHiveDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<GoodHarvestHiveDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<GoodHarvestHiveDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
  }

  /**
   * {@code POST  /good-harvest-hives} : Create a new goodHarvestHive.
   *
   * @param goodHarvestHiveDTO the goodHarvestHiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goodHarvestHiveDTO, or with status {@code 400 (Bad Request)} if the goodHarvestHive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<GoodHarvestHiveDTO> createGoodHarvestHive(@Valid @RequestBody GoodHarvestHiveDTO goodHarvestHiveDTO)
      throws URISyntaxException {
    LOG.debug("REST request to save GoodHarvestHive : {}", goodHarvestHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (goodHarvestHiveDTO.getId() != null) {
//      throw new BadRequestAlertException("A new goodHarvestHive cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    goodHarvestHiveDTO = service.save(goodHarvestHiveDTO);
//    return ResponseEntity.created(new URI("/api/good-harvest-hives/" + goodHarvestHiveDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, goodHarvestHiveDTO.getId().toString()))
//        .body(goodHarvestHiveDTO);
  }

  /**
   * {@code PUT  /good-harvest-hives/:id} : Updates an existing goodHarvestHive.
   *
   * @param id                 the id of the goodHarvestHiveDTO to save.
   * @param goodHarvestHiveDTO the goodHarvestHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goodHarvestHiveDTO,
   * or with status {@code 400 (Bad Request)} if the goodHarvestHiveDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the goodHarvestHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<GoodHarvestHiveDTO> updateGoodHarvestHive(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody GoodHarvestHiveDTO goodHarvestHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update GoodHarvestHive : {}, {}", id, goodHarvestHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (goodHarvestHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, goodHarvestHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!goodHarvestHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    goodHarvestHiveDTO = service.update(goodHarvestHiveDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goodHarvestHiveDTO.getId().toString()))
//        .body(goodHarvestHiveDTO);
  }

  /**
   * {@code PATCH  /good-harvest-hives/:id} : Partial updates given fields of an existing goodHarvestHive, field will ignore if it is null
   *
   * @param id                 the id of the goodHarvestHiveDTO to save.
   * @param goodHarvestHiveDTO the goodHarvestHiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goodHarvestHiveDTO,
   * or with status {@code 400 (Bad Request)} if the goodHarvestHiveDTO is not valid,
   * or with status {@code 404 (Not Found)} if the goodHarvestHiveDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the goodHarvestHiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<GoodHarvestHiveDTO> partialUpdateGoodHarvestHive(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody GoodHarvestHiveDTO goodHarvestHiveDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update GoodHarvestHive partially : {}, {}", id, goodHarvestHiveDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (goodHarvestHiveDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, goodHarvestHiveDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!goodHarvestHiveRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<GoodHarvestHiveDTO> result = service.partialUpdate(goodHarvestHiveDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goodHarvestHiveDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /good-harvest-hives} : get all the goodHarvestHives.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goodHarvestHives in body.
   */
  @GetMapping("")
  public ResponseEntity<List<GoodHarvestHiveDTO>> getAllGoodHarvestHives(
      @org.springdoc.core.annotations.ParameterObject Pageable pageable
  ) {
    LOG.debug("REST request to get a page of GoodHarvestHives");
    Page<GoodHarvestHiveDTO> page = service.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /good-harvest-hives/:id} : get the "id" goodHarvestHive.
   *
   * @param id the id of the goodHarvestHiveDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goodHarvestHiveDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<GoodHarvestHiveDTO> getGoodHarvestHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to get GoodHarvestHive : {}", id);
    Optional<GoodHarvestHiveDTO> goodHarvestHiveDTO = service.findOne(id);
    return ResponseUtil.wrapOrNotFound(goodHarvestHiveDTO);
  }

  /**
   * {@code DELETE  /good-harvest-hives/:id} : delete the "id" goodHarvestHive.
   *
   * @param id the id of the goodHarvestHiveDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGoodHarvestHive(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete GoodHarvestHive : {}", id);
    throw new DeviceIdForbiddenException("Can not delete record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
