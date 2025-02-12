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
import rs.hexatech.beeback.service.QueenService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.QueenDTO;
import tech.jhipster.web.util.PaginationUtil;

import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.Queen}.
 */
@RestController
@RequestMapping("/api/queens")
public class QueenResource {

  private static final Logger LOG = LoggerFactory.getLogger(QueenResource.class);

  private static final String ENTITY_NAME = "queen";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final QueenService service;

  @Autowired
  private SecurityService securityService;

  public QueenResource(QueenService queenService) {
    this.service = queenService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<QueenDTO>> syncEntities(@RequestBody List<QueenDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<QueenDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<QueenDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<QueenDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
  }

  /**
   * {@code POST  /queens} : Create a new queen.
   *
   * @param queenDTO the queenDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new queenDTO, or with status {@code 400 (Bad Request)} if the queen has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<QueenDTO> createQueen(@Valid @RequestBody QueenDTO queenDTO) throws URISyntaxException {
    LOG.debug("REST request to save Queen : {}", queenDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (queenDTO.getId() != null) {
//      throw new BadRequestAlertException("A new queen cannot already have an ID", ENTITY_NAME, "idexists");
//    }
//    queenDTO = service.save(queenDTO);
//    return ResponseEntity.created(new URI("/api/queens/" + queenDTO.getId()))
//        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, queenDTO.getId().toString()))
//        .body(queenDTO);
  }

  /**
   * {@code PUT  /queens/:id} : Updates an existing queen.
   *
   * @param id       the id of the queenDTO to save.
   * @param queenDTO the queenDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queenDTO,
   * or with status {@code 400 (Bad Request)} if the queenDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the queenDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<QueenDTO> updateQueen(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody QueenDTO queenDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update Queen : {}, {}", id, queenDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (queenDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, queenDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!queenRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    queenDTO = service.update(queenDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, queenDTO.getId().toString()))
//        .body(queenDTO);
  }

  /**
   * {@code PATCH  /queens/:id} : Partial updates given fields of an existing queen, field will ignore if it is null
   *
   * @param id       the id of the queenDTO to save.
   * @param queenDTO the queenDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated queenDTO,
   * or with status {@code 400 (Bad Request)} if the queenDTO is not valid,
   * or with status {@code 404 (Not Found)} if the queenDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the queenDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<QueenDTO> partialUpdateQueen(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody QueenDTO queenDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update Queen partially : {}, {}", id, queenDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (queenDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, queenDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!queenRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<QueenDTO> result = service.partialUpdate(queenDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, queenDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /queens} : get all the queens.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of queens in body.
   */
  @GetMapping("")
  public ResponseEntity<List<QueenDTO>> getAllQueens(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    LOG.debug("REST request to get a page of Queens");
    Page<QueenDTO> page = service.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /queens/:id} : get the "id" queen.
   *
   * @param id the id of the queenDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the queenDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<QueenDTO> getQueen(@PathVariable("id") Long id) {
    LOG.debug("REST request to get Queen : {}", id);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    Optional<QueenDTO> queenDTO = service.findOne(id);
//    return ResponseUtil.wrapOrNotFound(queenDTO);
  }

  /**
   * {@code DELETE  /queens/:id} : delete the "id" queen.
   *
   * @param id the id of the queenDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQueen(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete Queen : {}", id);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
