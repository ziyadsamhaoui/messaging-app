package messagerie.application.repository;
import messagerie.application.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {

    @Query("""
        SELECT c
        FROM ConversationEntity c
        WHERE c.type = messagerie.application.enums.ConversationType.PRIVATE
        AND EXISTS (
            SELECT 1
            FROM ConversationParticipantEntity p
            WHERE p.conversationId = c.conversationId
            AND p.userId = :userAId
        )
        AND EXISTS (
            SELECT 1
            FROM ConversationParticipantEntity p
            WHERE p.conversationId = c.conversationId
            AND p.userId = :userBId
        )
        AND (
            SELECT COUNT(p2)
            FROM ConversationParticipantEntity p2
            WHERE p2.conversationId = c.conversationId
        ) = 2
    """)
    Optional<ConversationEntity> findPrivateConversation(
            @Param("userAId") Long userAId,
            @Param("userBId") Long userBId
    );

    Optional<ConversationEntity> findByConversationId(Long conversationId);


    // Find conversations with id less than the given cursor id, ordered desc (id-based cursor pagination)
    List<ConversationEntity> findByConversationIdLessThanOrderByConversationIdDesc(Long conversationId, Pageable pageable);
}
