package messagerie.application.service;

import messagerie.application.entity.MessageEntity;
import messagerie.application.repository.*;
import messagerie.application.dto.MessagePageDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageServiceTest {

    @Test
    void getMessages_withoutCursor_returnsLatestAndNextCursor() {
        ConversationRepository convRepo = Mockito.mock(ConversationRepository.class);
        ConversationParticipantRepository partRepo = Mockito.mock(ConversationParticipantRepository.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        MessageRepository messageRepo = Mockito.mock(MessageRepository.class);

        MessageService svc = new MessageService(convRepo, partRepo, userRepo, messageRepo);

        // prepare 3 messages ordered by messageId desc: 103,102,101
        MessageEntity m1 = MessageEntity.builder().messageId(103L).conversationId(1L).userId(5L).content("a").createdAt(LocalDateTime.now()).build();
        MessageEntity m2 = MessageEntity.builder().messageId(102L).conversationId(1L).userId(5L).content("b").createdAt(LocalDateTime.now()).build();
        MessageEntity m3 = MessageEntity.builder().messageId(101L).conversationId(1L).userId(5L).content("c").createdAt(LocalDateTime.now()).build();

        Mockito.when(messageRepo.findByConversationIdOrderByMessageIdDesc(Mockito.eq(1L), Mockito.any(Pageable.class)))
                .thenReturn(List.of(m1, m2, m3));

        MessagePageDTO page = svc.getMessages(1L, null, 2);

        assertNotNull(page);
        assertEquals(2, page.getItems().size());
        // nextCursor should be id of last returned item (m2)
        assertEquals(102L, page.getNextCursor());
    }

    @Test
    void getMessages_withCursor_returnsOlder() {
        ConversationRepository convRepo = Mockito.mock(ConversationRepository.class);
        ConversationParticipantRepository partRepo = Mockito.mock(ConversationParticipantRepository.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        MessageRepository messageRepo = Mockito.mock(MessageRepository.class);

        MessageService svc = new MessageService(convRepo, partRepo, userRepo, messageRepo);

        MessageEntity m1 = MessageEntity.builder().messageId(99L).conversationId(1L).userId(5L).content("old").createdAt(LocalDateTime.now()).build();

        Mockito.when(messageRepo.findByConversationIdAndMessageIdLessThanOrderByMessageIdDesc(Mockito.eq(1L), Mockito.eq(100L), Mockito.any(Pageable.class)))
                .thenReturn(List.of(m1));

        MessagePageDTO page = svc.getMessages(1L, 100L, 20);

        assertNotNull(page);
        assertEquals(1, page.getItems().size());
        assertNull(page.getNextCursor()); // fewer than limit => no next cursor
    }
}



