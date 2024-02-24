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
    
    @GetMapping("/users/view")
    public String getAllUsers(Model model){
        List<User> users = userRepo.findAll();

        model.addAttribute("us", users);
        return "users/showAll";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response){
        System.out.println("Adding user");
        String newName = newuser.get("name");
        int newWeight = Integer.parseInt(newuser.get("weight"));
        int newHeight = Integer.parseInt(newuser.get("height")); // Corrected variable assignment
        String newHairColour = newuser.get("hair-colour");
        double newGPA = Double.parseDouble(newuser.get("gpa")); // Corrected data type conversion
        userRepo.save(new User(newName, newWeight, newHeight, newHairColour, newGPA));
        response.setStatus(201);
        return "redirect:/users/view";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") int userid) {
        Optional<User> optionalUser = userRepo.findById(userid);

        if (optionalUser.isPresent()) {
            userRepo.delete(optionalUser.get());
            return "redirect:/users/view";
        } else {
            // Handle the case where the user is not found
            return "error"; // You can customize this error handling as needed
        }
    }

    @GetMapping("/users/change")
    public String showChangeUserForm(@RequestParam("userId") int userId, Model model) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            return "users/change";
        } else {
            return "error"; // You can customize this error handling as needed
        }
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam("userId") int userId, @ModelAttribute User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            // Update user information
            existingUser.setName(updatedUser.getName());
            existingUser.setWeight(updatedUser.getWeight());
            existingUser.setHeight(updatedUser.getHeight());
            existingUser.setHairColour(updatedUser.getHairColour());
            existingUser.setGpa(updatedUser.getGpa());

            userRepo.save(existingUser);
            return "redirect:/users/view";
        } else {
            // Handle the case where the user is not found
            return "error"; // You can customize this error handling as needed
        }
    }


    

}