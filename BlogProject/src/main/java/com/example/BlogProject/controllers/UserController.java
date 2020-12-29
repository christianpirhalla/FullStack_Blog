package com.example.BlogProject.controllers;

import com.example.BlogProject.entities.User;
import com.example.BlogProject.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    //======================== Create User ======================
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder){
        LOG.info("creating a user: {}", user);
        //no validation check for if a user already exists, this will happen with authentication
        userService.createUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/person/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //==========================GET USER BY ID  =========================
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity getUserById(@PathVariable("id") Long id){
        LOG.info("getting user with id: {}", id);
        User user = userService.findUserById(id);

        if(user == null){
            LOG.info("person with id {} not found", id);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    //========================GET ALL USERS========================
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUserList(){
        LOG.info("Getting all users");
        List<User> userList = userService.findAll();
        if(userList == null || userList.isEmpty()){
            LOG.info("no users found");
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }


    //==================Get User By User Name ====================
    @RequestMapping(value = {"username"}, method = RequestMethod.GET)
    public ResponseEntity getUserByUsername(@PathVariable("username") String username){
        LOG.info("getting user with username: {}", username);

        User user = userService.findByUsername(username);

        if(user == null){
            LOG.info("person with username {} not found", username);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    //==================Get User By Email Address ====================

    //seems like a bad idea to expose a users email address as an endpoint...
    @RequestMapping(value = {"emailaddress"}, method = RequestMethod.GET)
    public ResponseEntity getUserByEmailAddress(@PathVariable("emailaddress") String emailaddress){
        LOG.info("getting user with email address: {}", emailaddress);

        User user = userService.findByEmailAddress(emailaddress);

        if(user == null){
            LOG.info("person with email address {} not found", emailaddress);
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


}
