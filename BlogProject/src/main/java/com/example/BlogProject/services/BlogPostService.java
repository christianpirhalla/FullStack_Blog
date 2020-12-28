package com.example.BlogProject.services;

import com.example.BlogProject.entities.BlogPost;
import com.example.BlogProject.entities.Tag;
import com.example.BlogProject.repositories.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    BlogPost blogPost;

    public List<BlogPost> findAll(){
        return blogPostRepository.findAll();
    }


    public BlogPost FindAllByTag(String tagName){

        return null;
    }

    public void deleteByID(Long id){
        blogPostRepository.delete(id);
    }


    public BlogPost createBlogPost(BlogPost blogPost){
        return blogPostRepository.save(blogPost);
    }

    public BlogPost updateBlogPost(BlogPost post){
        return blogPostRepository.save(post);


    }
    public BlogPost findAllById(Long userID){
        return null;
    }
    public BlogPost findAllByUsername(String username){
        return null;
    }





}
