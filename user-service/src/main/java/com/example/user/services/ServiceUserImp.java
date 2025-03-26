package com.example.user.services;

import com.example.user.entities.User;
import com.example.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceUserImp implements IUserService {
    private final UserRepository userRepository;

    public ServiceUserImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getall() {
        return userRepository.findAll();
    }

    @Override
    public User adduser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateuser(Long id, User user) {
        User updatedUser = userRepository.findById(id).orElse(null);
        if (updatedUser != null) {
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            return userRepository.save(updatedUser);
        }
        return null;
    }

    @Override
    public User getuser(Long id) {
        return userRepository.findById(id).get();
    }
    @Override
    public Optional<User> getbyid(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public void deleteuser(Long id) {
        userRepository.deleteById(id);
    }
}
