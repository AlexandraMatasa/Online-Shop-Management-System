package com.example.test2javafx;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private String username;
    private UserRole role;

    public User(int userId, String firstName, String lastName, String email, String password, String address, String phoneNumber, String username, UserRole role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
    }

    public User(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            this.userId = resultSet.getInt("userId");
            this.firstName = resultSet.getString("firstName");
            this.lastName = resultSet.getString("lastName");
            this.email = resultSet.getString("email");
            this.password = resultSet.getString("password");
            this.address = resultSet.getString("address");
            this.phoneNumber = resultSet.getString("phoneNumber");
            this.username = resultSet.getString("username");
            String userType = resultSet.getString("userType");
            this.role = setRoleByUserType(userType);
        } else {
            System.out.println("result set is null");
        }
    }

    public User(int userId, String username, String userType, String email) {
        this.userId = userId;
        this.username = username;
        this.role = setRoleByUserType(userType);
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public static User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("userId");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String address = resultSet.getString("address");
        String phoneNumber = resultSet.getString("phoneNumber");
        String username = resultSet.getString("username");
        String userType = resultSet.getString("userType");
        UserRole role = setRoleByUserType(userType);
        return new User(userId, firstName, lastName, email, password, address, phoneNumber, username, role);
    }

    public static UserRole setRoleByUserType(String userType) {
        switch(userType) {
            case "admin" :
                return UserRole.ADMIN;
            case "client" :
                return UserRole.CLIENT;
            default:
                throw new IllegalArgumentException("Invalid userType: " + userType);
        }
    }
    public String getUserTypeByRole(UserRole role) {
        switch(role) {
            case ADMIN:
                return "admin";
            case CLIENT:
                return "client";
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public String toString() {
        return "USERNAME: " + username + "   EMAIL: " + email + "   ROLE: " + role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return userId == other.userId;
    }
}
