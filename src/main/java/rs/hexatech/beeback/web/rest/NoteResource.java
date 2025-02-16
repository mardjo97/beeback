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
import rs.hexatech.beeback.service.NoteService;
import rs.hexatech.beeback.service.SecurityService;
import rs.hexatech.beeback.service.dto.NoteDTO;
import rs.hexatech.beeback.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link rs.hexatech.beeback.domain.Note}.
 */
@RestController
@RequestMapping("/api/notes")
public class NoteResource {

  private static final Logger LOG = LoggerFactory.getLogger(NoteResource.class);

  private static final String ENTITY_NAME = "note";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final NoteService service;

  @Autowired
  SecurityService securityService;

  public NoteResource(NoteService noteService) {
    this.service = noteService;
  }

  @PostMapping("/sync")
  public ResponseEntity<List<NoteDTO>> syncEntities(@RequestBody List<NoteDTO> entityDTOs, @RequestHeader("Device-Id") String deviceId) throws URISyntaxException {
    LOG.debug("REST request to sync Queens : {}", entityDTOs);
    securityService.checkUserDeviceId(deviceId);
    List<NoteDTO> syncedEntityDTOs = service.sync(entityDTOs);
    return ResponseEntity.ok().body(syncedEntityDTOs);
  }

  @GetMapping("/all")
  public ResponseEntity<List<NoteDTO>> userEntities(@RequestHeader(value = "Device-Id", required = false) String deviceId) {
    LOG.debug("REST request to retrieve user's Queens");
    securityService.checkUserDeviceId(deviceId);
    List<NoteDTO> userEntityDTOs = service.userEntities(deviceId);
    return ResponseEntity.ok().body(userEntityDTOs);
  }

  /**
   * {@code POST  /notes} : Create a new note.
   *
   * @param noteDTO the noteDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteDTO, or with status {@code 400 (Bad Request)} if the note has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) throws URISyntaxException {
    LOG.debug("REST request to save Note : {}", noteDTO);
    if (noteDTO.getId() != null) {
      throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME, "idexists");
    }
    noteDTO = service.save(noteDTO);
    return ResponseEntity.created(new URI("/api/notes/" + noteDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, noteDTO.getId().toString()))
        .body(noteDTO);
  }

  /**
   * {@code PUT  /notes/:id} : Updates an existing note.
   *
   * @param id      the id of the noteDTO to save.
   * @param noteDTO the noteDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
   * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<NoteDTO> updateNote(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody NoteDTO noteDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to update Note : {}, {}", id, noteDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (noteDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, noteDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!noteRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    noteDTO = service.update(noteDTO);
//    return ResponseEntity.ok()
//        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteDTO.getId().toString()))
//        .body(noteDTO);
  }

  /**
   * {@code PATCH  /notes/:id} : Partial updates given fields of an existing note, field will ignore if it is null
   *
   * @param id      the id of the noteDTO to save.
   * @param noteDTO the noteDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
   * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
   * or with status {@code 404 (Not Found)} if the noteDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<NoteDTO> partialUpdateNote(
      @PathVariable(value = "id", required = false) final Long id,
      @NotNull @RequestBody NoteDTO noteDTO
  ) throws URISyntaxException {
    LOG.debug("REST request to partial update Note partially : {}, {}", id, noteDTO);
    throw new DeviceIdForbiddenException("Can not create record from this device");
//    if (noteDTO.getId() == null) {
//      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//    }
//    if (!Objects.equals(id, noteDTO.getId())) {
//      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//    }
//
//    if (!noteRepository.existsById(id)) {
//      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//    }
//
//    Optional<NoteDTO> result = service.partialUpdate(noteDTO);
//
//    return ResponseUtil.wrapOrNotFound(
//        result,
//        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, noteDTO.getId().toString())
//    );
  }

  /**
   * {@code GET  /notes} : get all the notes.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
   */
  @GetMapping("")
  public ResponseEntity<List<NoteDTO>> getAllNotes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    LOG.debug("REST request to get a page of Notes");
    Page<NoteDTO> page = service.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /notes/:id} : get the "id" note.
   *
   * @param id the id of the noteDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<NoteDTO> getNote(@PathVariable("id") Long id) {
    LOG.debug("REST request to get Note : {}", id);
    Optional<NoteDTO> noteDTO = service.findOne(id);
    return ResponseUtil.wrapOrNotFound(noteDTO);
  }

  /**
   * {@code DELETE  /notes/:id} : delete the "id" note.
   *
   * @param id the id of the noteDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteNote(@PathVariable("id") Long id) {
    LOG.debug("REST request to delete Note : {}", id);
    throw new DeviceIdForbiddenException("Can not delete record from this device");
//    service.delete(id);
//    return ResponseEntity.noContent()
//        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//        .build();
  }
}
