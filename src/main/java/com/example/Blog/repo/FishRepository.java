package com.example.Blog.repo;

import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FishRepository extends CrudRepository<Fish, Long> {
    List<Fish> findByNameContains(String name);
    List<Fish> findByName(String name);
    List<Fish> findByIdNotIn(List<Long> id);
    List<Fish> findByIdIn(List<Long> id);
    //List<Fish> findByPostFishes(List<Post> id);

}
