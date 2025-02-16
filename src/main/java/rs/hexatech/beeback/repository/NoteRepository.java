package rs.hexatech.beeback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.hexatech.beeback.domain.Note;
import rs.hexatech.beeback.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
  @Query("select note from Note note where note.user.login = ?#{authentication.name}")
  List<Note> findByUserIsCurrentUser();

  Page<Note> findDistinctByUser_id(Pageable pageable, Long userId);

  Note findByUuidIs(String uuid);

  Note findByUserAndExternalId(User user, Integer externalId);
}
