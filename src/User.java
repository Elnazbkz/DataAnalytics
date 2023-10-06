import sqlitedb.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import appException.Exceptions;
import appException.Exceptions.EmailExistsException;
import appException.Exceptions.UserLoginInvalid;


public class User {
    private int UserID;
    private String EmailAddress;
    private String FirstName;
    private String LastName;
    private String Password;
    private Connection con;

    public User(int UserID, String EmailAddress, String FirstName, String LastName, String Password) throws SQLException {
        this.UserID = UserID;
        this.EmailAddress = EmailAddress;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Password = Password;
        this.con = DBConnection.getConnection();
    }

    public int getUserID() {
        return UserID;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setUserID(int userID) {
        this.UserID = userID;
    }

    public void setEmailAddress(String emailAddress) {
        this.EmailAddress = emailAddress;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
    
    public boolean userEmailExists(String EmailAddress) throws EmailExistsException {

        try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE EmailAddress = ?")) {

            stmt.setString(1, EmailAddress);
            ResultSet resultSet = stmt.executeQuery();

            // Check if the result set has any rows
            return resultSet.next();

        } catch (SQLException e) {
            System.out.println("Error while searching for the user: " + e.getMessage());
            return false;
        }
    }
    
    public boolean userLoginValidation(String EmailAddress, String Password) throws UserLoginInvalid {

        try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE EmailAddress = ?")) {

        	stmt.setString(1, EmailAddress);
            ResultSet resultSet = stmt.executeQuery();

            // Check if the result set has any rows
            while(resultSet.next()) {
            	if(resultSet.getString("Password") != Password) {
            		Exceptions exceptions = new Exceptions();  // Create an instance of Exceptions
            		throw exceptions.new UserLoginInvalid("Email address already exists.");
            	}
            }

        } catch (SQLException e) {
            System.out.println("Error while searching for the user: " + e.getMessage());
            return false;
        }
		return true;
    }

    public Map<String, String> GetUserInfo(int UserID) {
    	Map<String, String> userMap = null;
        final String TABLE_NAME = "users";
        try ( PreparedStatement stmt = con.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?")) {
        	stmt.setInt(1, UserID);
            ResultSet resultSet = stmt.executeQuery();
            userMap = new HashMap<>();
            while (resultSet.next()) {
				userMap.put("ID", String.valueOf(resultSet.getInt("ID")));
				userMap.put("FirstName", String.valueOf(resultSet.getString("FirstName")));
				userMap.put("LastName", String.valueOf(resultSet.getString("LastName")));
				userMap.put("Password", String.valueOf(resultSet.getString("Password")));
				userMap.put("AccountType", String.valueOf(resultSet.getString("AccountType")));
            }

        } catch (SQLException e) {
        	System.out.println("Error while searching for the user: " + e.getMessage());
        }
        
		return userMap;
    }

    public boolean createUser(User user) throws EmailExistsException {
        boolean userExists = userEmailExists(user.getEmailAddress());

        if (userExists) {
            Exceptions exceptions = new Exceptions();  // Create an instance of Exceptions
            throw exceptions.new EmailExistsException("Email address already exists.");
        }

        String EmailAddress = user.getEmailAddress();
        String FirstName = user.getFirstName();
        String LastName = user.getLastName();
        String Password = user.getPassword();
        String AccountType = "Basic";

        try {
            String query = "INSERT INTO users (EmailAddress, FirstName, LastName, Password, AccountType) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, EmailAddress);
            pstmt.setString(2, FirstName);
            pstmt.setString(3, LastName);
            pstmt.setString(4, Password);
            pstmt.setString(5, AccountType);

            int result = pstmt.executeUpdate();

            if (result == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error while inserting user: " + e.getMessage());
        }

        return false;
    }
}
