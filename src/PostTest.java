import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import appException.Exceptions;

public class PostTest {
    private static Post post;

    @BeforeClass
    public static void setup() {
        post = new Post();
    }

    @AfterClass
    public static void tearDown() {
        post = null;
    }

    @Test
    public void testPostIDExists() {
        // Assuming post with ID 10 exists in the database
        assertTrue(post.postIDExists(122));
        
        // Assuming post with ID 999 does not exist in the database
        assertFalse(post.postIDExists(999));
    }

    @Test
    public void testDeletePost() {
        // Assuming you have a post with ID 10 in the database
        assertTrue(post.DeletePost(122));
        
        // Assuming you do not have a post with ID 999 in the database
        assertFalse(post.DeletePost(99900));
    }

    @Test
    public void testCreateNewPost() {
        Post newPost = new Post(101, "Test Content", "Test Author", 10, 5, "19/10/2023 12:30");
        // Assuming a post with ID 101 does not exist in the database
        try {
            assertTrue(post.CreateNewPost(newPost));
        } catch (Exceptions.PostIDInvalid | Exceptions.PostIDExists e) {
            fail("Exception should not be thrown.");
        }

        // Assuming a post with ID 1 already exists in the database
        try {
            post.CreateNewPost(new Post(122, "Test Content", "Test Author", 10, 5,"19/10/2023 12:30"));
            fail("Exception should be thrown for existing post ID.");
        } catch (Exceptions.PostIDInvalid e) {
            fail("PostIDInvalid exception should not be thrown.");
        } catch (Exceptions.PostIDExists e) {
            // PostIDExists exception is expected
        }
    }

    @Test
    public void testGetPostDetails() {
        // Assuming there is a post with ID 1 in the database
        assertNotNull(post.GetPostDetails(122));

        // Assuming there is no post with ID 999 in the database
        //assertNull(post.GetPostDetails(999));
        assertEquals(null,post.GetPostDetails(999));
    }

    @Test
    public void testGetTopPosts() {
        // Assuming you have posts in the database
        assertFalse(post.GetTopPosts("Likes", "2").isEmpty());

        // Assuming there are no posts with Type "InvalidType" and Count "999" in the database
        assertTrue(!post.GetTopPosts("Shares", "999").isEmpty());
    }

    @Test
    public void testGetSharesRange() {
        // Assuming there are posts with shares in the specified range
        assertTrue(post.GetSharesRange(0, 1000) > 0);

        // Assuming there are no posts with shares in the range 10000 to 20000
        assertFalse(post.GetSharesRange(100000, 200000) > 0);
    }

    // Add more test cases for other methods

}
