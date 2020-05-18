package com.example.demo.dto;

import java.util.Date;

/**
 * GPS Dto
 * 
 * @author hao.cu
 * @since 2020/5/16
 */
public class GPSDto {
    private Integer id;
    private String fileName;
    private String fileUrl;
    private String uploadUser;
    private Date uploadDateTime;

    public GPSDto() {
    }

    public GPSDto(Integer id, String fileName, String fileUrl, String uploadUser, Date uploadDateTime) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.uploadUser = uploadUser;
        this.uploadDateTime = uploadDateTime;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUploadUser() {
        return this.uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public Date getUploadDateTime() {
        return this.uploadDateTime;
    }

    public void setUploadDateTime(Date uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }
}