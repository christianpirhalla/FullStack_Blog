package com.example.BlogProject.controllers;

import com.example.BlogProject.entities.BlogPost;
import com.example.BlogProject.entities.Tag;
import com.example.BlogProject.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlogPostController {
    private BlogPostService blogPostService;
    @Autowired
    public BlogPostController(BlogPostService blogPostService){
        this.blogPostService = blogPostService;
    }

    @GetMapping("/blog/getall")
    public ResponseEntity<Iterable<BlogPost>> getAll(){
        return new ResponseEntity<Iterable<BlogPost>>(blogPostService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/blog/tag/{tag}")
    public ResponseEntity<Iterable<BlogPost>> getAllByTag(@PathVariable String tag){
        return new ResponseEntity<Iterable<BlogPost>>(blogPostService.getAllByTag(tag), HttpStatus.OK);
    }

    @GetMapping("/blog/id/{id}")
    public ResponseEntity<Iterable<BlogPost>> getAllById(@PathVariable Long id){
        return new ResponseEntity<Iterable<BlogPost>>(blogPostService.getAllById(id), HttpStatus.OK);
    }

    @GetMapping("/blog/userName/{userName}")
    public ResponseEntity<Iterable<BlogPost>> getAllByUserName(@PathVariable String userName){
        return new ResponseEntity<Iterable<BlogPost>>(blogPostService.getAllByUserName(userName), HttpStatus.OK);
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id){
        return new ResponseEntity<Boolean>(blogPostService.deleteById(id),HttpStatus.OK);
    }

    @PostMapping("/blog/")
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost){
        return new ResponseEntity<BlogPost>(blogPostService.createBlogPost(blogPost),HttpStatus.OK);
    }

    @PutMapping("/blog/{id}")
    public ResponseEntity<BlogPost> updateBlogPost(@PathVariable Long id,@RequestBody BlogPost blogPost){
        return new ResponseEntity<BlogPost>(blogPostService.updateBlogPost(id,blogPost),HttpStatus.OK);
    }

}
