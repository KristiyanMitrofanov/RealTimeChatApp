package devexperts.chatbackend.repositories;

import devexperts.chatbackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r from Role r where r.name = :name")
    Optional<Role> findByName(@Param("name") String name);
}
