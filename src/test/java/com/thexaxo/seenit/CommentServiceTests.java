package com.thexaxo.seenit;

import com.thexaxo.seenit.entities.Comment;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.AddCommentBindingModel;
import com.thexaxo.seenit.models.RegisterUserBindingModel;
import com.thexaxo.seenit.repositories.CommentRepository;
import com.thexaxo.seenit.services.CommentServiceImpl;
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
public class CommentServiceTests {
    private static final String COMMENT_CONTENT = "random content";

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void testAddComment_withContent_contentShouldBeAdded() {
        when(this.commentRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        AddCommentBindingModel bindingModel = new AddCommentBindingModel();
        bindingModel.setContent(COMMENT_CONTENT);

        Comment comment = this.commentService.addComment(bindingModel, null, null);

        assertEquals(
                "Content was not added!",
                comment.getContent(),
                COMMENT_CONTENT
        );
    }
}