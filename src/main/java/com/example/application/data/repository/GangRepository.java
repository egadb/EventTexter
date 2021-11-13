package com.example.application.data.repository;


import com.example.application.data.entity.Gang;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GangRepository extends JpaRepository<Gang, Integer> {

}
