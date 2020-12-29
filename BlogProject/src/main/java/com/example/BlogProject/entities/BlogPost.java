package com.example.BlogProject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
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
    private Instant timestamp;
    @Column(name = "BLURB")
    private String blurb;
    @Column(name = "FULLTEXT")
    private String fulltext;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "IMAGELINK")
    private String imagelink;

    @ManyToMany
    @JoinTable(name="BLOGPOST_TAG",
            joinColumns = @JoinColumn(name = "BLOG_ID", referencedColumnName = "BLOGPOST_ID"),
    inverseJoinColumns = @JoinColumn(name = "TAG_ID", referencedColumnName = "BLOGPOST_ID"))
    private Set<Tag> tags;

    @ManyToOne
    @Column(name = "USER")
    private User user;
}
