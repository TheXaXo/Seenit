package com.thexaxo.seenit;

import com.thexaxo.seenit.entities.Message;
import com.thexaxo.seenit.models.SendMessageBindingModel;
import com.thexaxo.seenit.repositories.MessageRepository;
import com.thexaxo.seenit.services.MessageServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MessageServiceTests {
    private static final String MESSAGE_CONTENT = "random content";

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    public void testAddComment_withContent_contentShouldBeAdded() {
        when(this.messageRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        SendMessageBindingModel bindingModel = new SendMessageBindingModel();
        bindingModel.setContent(MESSAGE_CONTENT);

        Message message = this.messageService.createMessage(bindingModel, null, null);

        assertEquals(
                "Content was not added!",
                message.getContent(),
                MESSAGE_CONTENT
        );
    }
}