import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import appException.Exceptions.EmailExistsException;
import appException.Exceptions.UserLoginInvalid;
import appException.Exceptions.FailedUpdateException;
import sqlitedb.SQLiteJDBC;

public class User {
	private int UserID;
	private String EmailAddress;
	private String FirstName;
	private String LastName;
	private String Password;
	private String AccountType;
	SQLiteJDBC dbConnection = new SQLiteJDBC();
	Connection con = dbConnection.getConnection();

	public User(int UserID, String FirstName, String LastName, String EmailAddress, String Password, String AccountType)
			throws SQLException {
		this.UserID = UserID;
		this.EmailAddress = EmailAddress;
		this.FirstName = FirstName;
		this.LastName = LastName;
		this.Password = Password;
		this.AccountType = AccountType;

	}

	public User() {
		// TODO Auto-generated constructor stub
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

	public String getAccoubntType() {
		return AccountType;
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

	public void setAccountType(String AccountType) {
		this.AccountType = AccountType;
	}

	// method to return user id if user id exists
	public int userEmailExists(String EmailAddress) throws EmailExistsException {

		try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE EmailAddress = ?")) {

			stmt.setString(1, EmailAddress);
			ResultSet resultSet = stmt.executeQuery();

			// Check if the result set has any rows
			return resultSet.getInt("ID");

		} catch (SQLException e) {
			System.out.println("Error while searching for the user: " + e.getMessage());
			return 0;
		}
	}

	// methoud to validate login details
	public boolean userLoginValidation(String EmailAddress, String Password) throws UserLoginInvalid {

		try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE EmailAddress = ?")) {
			stmt.setString(1, EmailAddress);
			ResultSet resultSet = stmt.executeQuery();

			// Check if the result set has any rows
			while (resultSet.next()) {
				if (resultSet.getString("Password").equals(Password)) {
					return true;
				}
			}

		} catch (SQLException e) {
			System.out.println("Error while searching for the user: " + e.getMessage());
		}
		return false;
	}

	// method to get user details by user id
	public Map<String, String> GetUserInfo(int UserID) {
		Map<String, String> userMap = null;
		try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE ID = " + UserID)) {
			ResultSet resultSet = stmt.executeQuery();
			userMap = new HashMap<>();
			while (resultSet.next()) {
				userMap.put("ID", String.valueOf(resultSet.getInt("ID")));
				userMap.put("FirstName", String.valueOf(resultSet.getString("FirstName")));
				userMap.put("LastName", String.valueOf(resultSet.getString("LastName")));
				userMap.put("EmailAddress", String.valueOf(resultSet.getString("EmailAddress")));
				userMap.put("Password", String.valueOf(resultSet.getString("Password")));
				userMap.put("AccountType", String.valueOf(resultSet.getString("AccountType")));
			}

		} catch (SQLException e) {
			System.out.println("Error while searching for the user: " + e.getMessage());
		}

		return userMap;

	}

	// method to create a new user
	public boolean createUser(User user) throws EmailExistsException {
		String EmailAddress = user.getEmailAddress();
		String FirstName = user.getFirstName();
		String LastName = user.getLastName();
		String Password = user.getPassword();
		String AccountType = "Basic";

		try {
			String query = "INSERT INTO users ( FirstName, LastName, EmailAddress, Password, AccountType) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, FirstName);
			pstmt.setString(2, LastName);
			pstmt.setString(3, EmailAddress);
			pstmt.setString(4, Password);
			pstmt.setString(5, AccountType);

			int result = pstmt.executeUpdate();

			if (result == 1) { // return true if user created
				return true;
			}
		} catch (SQLException e) {

		}

		return false;
	}

	// method to update user details
	public boolean UpdateUser(User user) throws FailedUpdateException {
		int userID = user.getUserID();
		String EmailAddress = user.getEmailAddress();
		String FirstName = user.getFirstName();
		String LastName = user.getLastName();
		try {
			String query = "UPDATE users SET FirstName = ?, LastName = ?, EmailAddress = ? WHERE ID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, FirstName);
			pstmt.setString(2, LastName);
			pstmt.setString(3, EmailAddress);
			pstmt.setInt(4, userID);

			int result = pstmt.executeUpdate();

			if (result == 1) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error while updating user: " + e.getMessage());
		}

		return false;
	}

	// method to change user account type to VIP
	public boolean SetUserVIP(int UserID) throws FailedUpdateException {
		try {
			String query = "UPDATE users SET AccountType = 'VIP' WHERE ID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, UserID);
			int result = pstmt.executeUpdate();
			if (result == 1) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Error while updating user: " + e.getMessage());
		}
		return false;
	}

	// method to get User Account type
	public String UserVIPStatus(int UserID) {
		String UserVIPStatus = null;
		try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE ID = " + UserID)) {
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				UserVIPStatus = String.valueOf(resultSet.getString("AccountType"));
			}

		} catch (SQLException e) {
			System.out.println("Error while searching for the user: " + e.getMessage());
		}

		return UserVIPStatus;

	}
}
