package messagerie.application.service;

import messagerie.application.entity.ConversationEntity;
import messagerie.application.entity.ConversationParticipantEntity;
import messagerie.application.enums.ConversationType;
import messagerie.application.dto.ConversationPageDTO;
import messagerie.application.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConversationServiceTest {

    @Test
    void getConversations_returnsNextCursorWhenMore() {
        ConversationRepository convRepo = Mockito.mock(ConversationRepository.class);
        ConversationParticipantRepository partRepo = Mockito.mock(ConversationParticipantRepository.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        SimpMessagingTemplate template = Mockito.mock(SimpMessagingTemplate.class);

        ConversationService svc = new ConversationService(convRepo, partRepo, userRepo, template);

        ConversationEntity c1 = new ConversationEntity();
        c1.setConversationId(200L);
        c1.setType(ConversationType.GROUP);
        c1.setCreatedAt(LocalDateTime.now());

        ConversationEntity c2 = new ConversationEntity();
        c2.setConversationId(199L);
        c2.setType(ConversationType.GROUP);
        c2.setCreatedAt(LocalDateTime.now());

        // Simulate repository returning fetchSize (limit+1) items
        Mockito.when(convRepo.findAll(Mockito.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(c1, c2)));

        // participants and users mocked to empty to keep mapping simple
        Mockito.when(partRepo.findByConversationId(Mockito.anyLong())).thenReturn(List.of());

        ConversationPageDTO page = svc.getConversations(null, 1);

        assertNotNull(page);
        assertEquals(1, page.getItems().size());
        // nextCursor is the id of the last returned item in the page (page size is 1 -> id 200)
        assertEquals(200L, page.getNextCursor());
    }
}


