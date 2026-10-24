package com.core2web.controller;

import java.util.List;

import com.core2web.dao.UserDao;
import com.core2web.model.User;

public class UserController {

    UserDao userDao = new UserDao();

    // Returns null on success, otherwise an error message to show on the screen
    public String addUser(String name, String email, String password, String role) {
        if (userDao.userExists(email)) {
            return "An account with this email already exists.";
        }
        User user = new User(name, email, password, role);
        return userDao.addUser(user) ? null : "Could not create account. Please try again.";
    }
    
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    // Returns the logged in user (role is "User", "Admin" or "Delivery") or null if login fails
    public User authenticateUser(String email, String password) {
        User user = userDao.getUserByEmail(email);
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
