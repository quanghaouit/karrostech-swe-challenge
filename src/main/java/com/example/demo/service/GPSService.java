package com.example.demo.service;

import com.example.demo.dto.GPSDto;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * GPS Service
 * 
 * @author hao.cu
 * @since 2020/5/16
 *
 */
public interface GPSService {
    
    /**
     * Store gps file
     * 
     * @param file file upload
     * @param user user upload
     * @return file download link
     */
    public String storeFile(MultipartFile file, String user);

    /**
     * Load gps file as resource
     * 
     * @param userUpload user upload
     * @param fileName file name
     * @return resoure file
     */
    public Resource loadFileAsResource(String userUpload ,String fileName);

    /**
     * Get file gps info by id
     * 
     * @param id gps id
     * @return gps info
     */
    public GPSDto getGPSById(Integer id);

    /**
     * Get gps files as page
     * 
     * @param pageable
     * @return gps page
     */
    public Page<GPSDto> getGpsPage(Pageable pageable);
}