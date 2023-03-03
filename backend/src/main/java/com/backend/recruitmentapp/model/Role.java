package com.backend.recruitmentapp.model;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role
    {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    public String getName()
        {
        return name;
        }

    public void setName(String name)
        {
        this.name = name;
        }

    public Integer getId()
        {
        return id;
        }

    public void setId(Integer id)
        {
        this.id = id;
        }
    }