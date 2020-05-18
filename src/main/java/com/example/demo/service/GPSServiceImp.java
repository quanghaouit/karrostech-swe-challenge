package com.example.demo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import com.example.demo.dao.GPSDao;
import com.example.demo.entity.GPS;
import com.example.demo.dto.GPSDto;
import com.example.demo.exception.FileNotFoundException;
import com.example.demo.exception.FileStorageException;
import com.example.demo.property.FileStorageProperties;
import com.example.demo.utils.ObjectUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * GPS service implement
 * 
 * @author hao.cu
 * @since 2020/5/16
 */
@Service
public class GPSServiceImp implements GPSService {

    /**
     * File storage location
     */
    private Path fileStorageLocation;

    /**
     * GPS service impl
     * 
     * @param fileStorateProperties file storate properties
     */
    @Autowired
    public GPSServiceImp( FileStorageProperties fileStorateProperties) {
        this.fileStorageLocation = Paths.get(fileStorateProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    ex);
        }
    }

    /**
     * Gps Dao
     */
    @Autowired
    public GPSDao gpsDao;

    @Override
    public String storeFile(MultipartFile file, String username) {
        final String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path fileStorageLocationOfUser = Paths.get(this.fileStorageLocation.toString(), username);
            if (Files.notExists(fileStorageLocationOfUser)) {
                Files.createDirectories(fileStorageLocationOfUser);
            }

            Path targetLocation = fileStorageLocationOfUser.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                    .path(username + "/").path(fileName).toUriString();

            GPS gpx = new GPS();
            gpx.setFileName(fileName);
            gpx.setFileUrl(fileDownloadUri);
            gpx.setUploadUser(username);
            gpx.setUploadDateTime(new Date());

            gpsDao.save(gpx);

            return fileDownloadUri;
        } catch (final IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String userUpload, String fileName) {
        try {
            Path filePath = Paths.get(this.fileStorageLocation.toString(), userUpload).resolve(fileName)
                    .normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    @Override
    public GPSDto getGPSById(Integer id) {
        return ObjectUtility.map(gpsDao.findById(id).get(), GPSDto.class);
    }

    @Override
    public Page<GPSDto> getGpsPage(Pageable pageable) {
        return gpsDao.findAll(pageable).map(GPS-> ObjectUtility.map(GPS, GPSDto.class));
    }
    
}