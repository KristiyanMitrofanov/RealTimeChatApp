package devexperts.chatbackend.repositories;

import devexperts.chatbackend.models.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Page<Chat> findAll(Specification<Chat> filters, Pageable pageable);

    @Query("SELECT c from Chat c where c.name = :name")
    Optional<Chat> findByName(@Param("name") String name);

    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u.username = :username")
    Optional<List<Chat>> getAllByUsername(@Param("username") String username);

    @Query("select c from Chat c")
    Optional<List<Chat>> getAll();
}
