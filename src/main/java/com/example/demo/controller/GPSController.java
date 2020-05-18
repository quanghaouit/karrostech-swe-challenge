package com.example.demo.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.dto.GPSDto;
import com.example.demo.service.GPSService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * GPS Controller
 * 
 * @author hao.cu
 * @since 2020/5/16
 */
@RestController
public class GPSController {

    private static final Logger logger = LoggerFactory.getLogger(GPSController.class);

    /**
     *  Extended file type allow for upload
     */
    private static final String[] FILE_TYPE_ALLOW_UPLOADED = {"KML","GPX", "PLT", "GDB"};


    /**
     * gps service
     */
    @Autowired
    private GPSService gpsService;
    
    /**
     * Upload file
     * 
     * @param username user upload
     * @param file file upload
     * @return file name
     */
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestHeader(value="user-name") String username ,@RequestParam("file") MultipartFile file){
        if(Arrays.asList(FILE_TYPE_ALLOW_UPLOADED).contains(getFileExtension(file.getOriginalFilename()).toUpperCase())){
            return ResponseEntity.ok().body(gpsService.storeFile(file, username));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("File is not allow to upload");
    }

    /**
     * Download file
     * 
     * @param userupload user upload
     * @param fileName download file name
     * @param request request
     * @return file download
     */
    @GetMapping("/downloadFile/{userupload}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String userupload, @PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = gpsService.loadFileAsResource(userupload,fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * get detail GPS file by id
     * 
     * @param id gsp file id
     * @return gps info
     */
    @GetMapping("/gps/{id}")
    GPSDto getGPSDetail(@PathVariable Integer id){
        return gpsService.getGPSById(id);
    }

    /**
     * get GPS page
     *  
     * @param pageable pageable
     * @return gps info
     */
    @GetMapping("/gps")
    Page<GPSDto> getGpsPage(Pageable pageable){
        return  gpsService.getGpsPage(pageable);
    }

    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}