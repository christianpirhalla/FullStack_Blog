package com.example.BlogProject.repositories;

import com.example.BlogProject.entities.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    public List<BlogPost> findByUser_Id(Long userId);
    //How is this going to work?
    public List<BlogPost> findByTags_Name(String tagName);
}
