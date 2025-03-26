package com.example.user.services;

import com.example.user.entities.User;

import java.util.List;
import java.util.Optional;


public interface IUserService {
    List<User> getall();
    User adduser (User user);
    User updateuser (Long id , User user);
    User getuser(Long id);
    Optional<User> getbyid(Long id);
    void deleteuser(Long id);
}
