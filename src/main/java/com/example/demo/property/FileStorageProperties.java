package com.example.demo.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * File storage properties
 * 
 * @author hao.cu
 * @since 2020/5/16
 *
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
