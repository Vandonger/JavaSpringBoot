package com.onemount.demo.repository;

import com.onemount.demo.entity.MyFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFileRepository extends JpaRepository<MyFile, Long> {
    
}
