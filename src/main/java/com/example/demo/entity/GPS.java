package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * GPS entity
 * 
 * @author hao.cu
 * @since 2020/5/16
 *
 */
@Entity
@Table(name = "gps")
public class GPS {

    @Id
    @GeneratedValue
    @Column(name= "id")
    private Integer id;

    @Column(name="fileName")
    private String fileName;

    @Column(name="fileUrl")
    private String fileUrl;

    @Column(name="uploadUser")
    private String uploadUser;

    @Column(name="uploadDateTime")
    private Date uploadDateTime;

    public GPS() {
    }

    public GPS(Integer id, String fileName, String fileUrl, String uploadUser, Date uploadDateTime) {
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
