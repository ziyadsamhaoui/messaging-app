package messagerie.application.repository;
import messagerie.application.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);

    List<MessageEntity> findByConversationIdAndCreatedAtLessThanOrderByCreatedAtDesc(Long conversationId, LocalDateTime createdAt, Pageable pageable);

}
