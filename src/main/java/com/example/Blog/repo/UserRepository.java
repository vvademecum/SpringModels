package com.example.Blog.repo;

import com.example.Blog.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Iterable<User> findAllByUsernameNot(String username);
}
