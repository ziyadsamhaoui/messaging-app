package messagerie.application.repository;
import messagerie.application.entity.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {


    // ID-based cursor pagination queries (use messageId as cursor)
    List<MessageEntity> findByConversationIdOrderByMessageIdDesc(Long conversationId, Pageable pageable);

    List<MessageEntity> findByConversationIdAndMessageIdLessThanOrderByMessageIdDesc(Long conversationId, Long messageId, Pageable pageable);

}
