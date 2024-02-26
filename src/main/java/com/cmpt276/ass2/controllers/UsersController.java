package com.cmpt276.ass2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


import com.cmpt276.ass2.models.User;
import com.cmpt276.ass2.models.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UsersController{
    @Autowired
    private UserRepository userRepo;

    // Redirects to the user view page when first start
    @GetMapping("/")
    public String redirectToUserView(Model model) {
        return "redirect:/users/view";
    }
    
    // Retrieves all users and displays them on the user view page
    @GetMapping("/users/view")
    public String getAllUsers(Model model){
        List<User> users = userRepo.findAll();

        model.addAttribute("us", users);
        return "users/showAll";
    }

    // Adds a new user based on the form data submitted
    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response){
        System.out.println("Adding user");
        String newName = newuser.get("name");
        int newWeight = Integer.parseInt(newuser.get("weight"));
        int newHeight = Integer.parseInt(newuser.get("height")); 
        String newHairColour = newuser.get("hair-colour");
        double newGPA = Double.parseDouble(newuser.get("gpa")); 
        userRepo.save(new User(newName, newWeight, newHeight, newHairColour, newGPA));
        response.setStatus(201);
        return "redirect:/users/view";
    }

    // Deletes a user with the specified user ID
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") int userid) {
        Optional<User> optionalUser = userRepo.findById(userid);
            userRepo.delete(optionalUser.get());
            return "redirect:/users/view";
    }

    // Displays the form to change user information
    @GetMapping("/users/change")
    public String showChangeUserForm(@RequestParam("userId") int userId, Model model) {
        Optional<User> optionalUser = userRepo.findById(userId);
            User user = optionalUser.get();
            model.addAttribute("user", user);
            return "users/change";
        
    }

    // Updates user information based on the form data submitted
    @PostMapping("/users/update")
    public String updateUser(@RequestParam("userId") int userId, @ModelAttribute User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(userId);

        User existingUser = optionalUser.get();
        // Update user information
        existingUser.setName(updatedUser.getName());
        existingUser.setWeight(updatedUser.getWeight());
        existingUser.setHeight(updatedUser.getHeight());
        existingUser.setHairColour(updatedUser.getHairColour());
        existingUser.setGpa(updatedUser.getGpa());

        userRepo.save(existingUser);
        return "redirect:/users/view";
        
    }


    

}