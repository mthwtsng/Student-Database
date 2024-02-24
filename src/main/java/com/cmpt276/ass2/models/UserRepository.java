package com.cmpt276.ass2.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer>{
    List<User> findAll();
    List<User> findByUid(int uid);
}