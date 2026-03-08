package org.example.grad_gateway2.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

    private String location;

    private String url;

    private String description;

    private int size;

    private String logo;

    @OneToMany(mappedBy = "company")
    private List<JobPost> jobPosts;

    @OneToMany(mappedBy = "company")
    private List<Reviews> reviews;

    @OneToMany(mappedBy = "company")
    private List<User> users;
}
