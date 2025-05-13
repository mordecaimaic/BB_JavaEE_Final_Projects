package com.example.model;

public class User {
    private Integer id;
    private String name;
    // … 根据表结构添加其它字段

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // toString, equals, hashCode … 如需
}