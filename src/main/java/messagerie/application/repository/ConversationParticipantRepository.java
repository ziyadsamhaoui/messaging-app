package messagerie.application.repository;
import messagerie.application.entity.ConversationParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipantEntity, Long> {

    Optional<ConversationParticipantEntity> findByConversation_ConversationIdAndUserId(Long conversationId, Long userId);

    List<ConversationParticipantEntity> findByUserId(Long userId);

    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);
}
