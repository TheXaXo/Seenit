package com.thexaxo.seenit;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.services.PostServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PostServiceTests {
    private User userMock;

    @Before
    public void setUp() {
        userMock = new User();

        userMock.setUpvotedPosts(new ArrayList<>());
        userMock.setDownvotedPosts(new ArrayList<>());
        userMock.setSavedPosts(new ArrayList<>());
    }

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void testPopulateFields_upvoteFieldShouldBePopulated() {
        Post postMock = new Post();
        userMock.getUpvotedPosts().add(postMock);

        this.postService.populateUpvotedDownvotedSavedFields(postMock, userMock);

        assertEquals(
                "Upvoted field was not populated!",
                postMock.isUpvoted(),
                true
        );
    }

    @Test
    public void testPopulateFields_downvoteFieldShouldBePopulated() {
        Post postMock = new Post();
        userMock.getDownvotedPosts().add(postMock);

        this.postService.populateUpvotedDownvotedSavedFields(postMock, userMock);

        assertEquals(
                "Downvoted field was not populated!",
                postMock.isDownvoted(),
                true
        );
    }

    @Test
    public void testPopulateFields_savedFieldShouldBePopulated() {
        Post postMock = new Post();
        userMock.getSavedPosts().add(postMock);

        this.postService.populateUpvotedDownvotedSavedFields(postMock, userMock);

        assertEquals(
                "Saved field was not populated!",
                postMock.isSaved(),
                true
        );
    }
}