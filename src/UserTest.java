import org.junit.*;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserEmailExists() throws Exception {
        User user = new User();
        int result = user.userEmailExists("test@example.com");
        assertEquals(0, result); // Modify the expected value based on your test data
    }

    @Test
    public void testUserLoginValidation() throws Exception {
        User user = new User();
        boolean result = user.userLoginValidation("eli@gmail.com", "123456");
        assertTrue(result); // Modify based on your test data
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        User newUser = new User(4, "John", "Doe", "johndoe@example.com", "123456789", "Basic");
        boolean result = user.createUser(newUser);
        assertTrue(result);
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        User updatedUser = new User(4, "Updated", "User", "johndoe@example.com", "987654321", "Basic");
        boolean result = user.UpdateUser(updatedUser);
        assertTrue(result);
    }

    @Test
    public void testSetUserVIP() throws Exception {
        User user = new User();
        boolean result = user.SetUserVIP(1);
        assertTrue(result);
    }

    @Test
    public void testUserVIPStatus() throws Exception {
        User user = new User();
        String result = user.UserVIPStatus(1);
        assertEquals("VIP", result); // Modify based on your test data
    }

    // Add more test methods as needed for other functionalities

}
