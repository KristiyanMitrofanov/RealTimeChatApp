package devexperts.chatbackend.repositories;

import devexperts.chatbackend.models.Chat;
import devexperts.chatbackend.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message as m where m.chat = :chat order by TO_TIMESTAMP(m.timestamp, 'DD-MM-YYYY HH24:MI:SS') DESC")
    List<Message> getMessages(@Param("chat") Chat chat);
}
