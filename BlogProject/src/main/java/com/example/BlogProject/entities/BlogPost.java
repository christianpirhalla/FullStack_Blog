package com.example.BlogProject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BLOGPOST_ID")
    private Long id;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "TIMESTAMP")
    private Date timestamp;
    @Column(name = "BLURB")
    private String blurb;
    @Column(name = "FULLTEXT")
    private String fulltext;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "IMAGELINK")
    private String imagelink;

    @ManyToMany
    @JoinTable(name="blogPost_tag",
            joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags;

    @OneToMany
    @Column(name = "USER")
    private User user;
}
