package com.example.BlogProject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USERNAME")
    @Column(unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    @Column(unique = true)
    private String email;

    @Column()
    private Instant timestamp;

    @OneToMany(mappedBy = "Tag", cascade = CascadeType.ALL)
    @Column(name = "BLOGPOSTS")
    private List<BlogPosts> blogPosts;
}
