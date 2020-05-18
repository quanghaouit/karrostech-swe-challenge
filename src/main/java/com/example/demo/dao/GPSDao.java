package com.example.demo.dao;

import java.util.Optional;

import com.example.demo.entity.GPS;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * GPS Dao
 * 
 * @author hao.cu
 * @since 2020/5/16
 */
@Repository
public interface GPSDao extends PagingAndSortingRepository<GPS, Integer> {

    Optional<GPS> findById(Integer id);
    
    Page<GPS> findAll(Pageable pageable);
}