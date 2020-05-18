package com.example.demo.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.dao.GPSDao;
import com.example.demo.entity.GPS;
import com.example.demo.property.FileStorageProperties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GPSServiceImplTest {

    @Autowired
    GPSService gpsService;

    @MockBean
    FileStorageProperties fileStorageProperties;
        
    @MockBean
    GPSDao gpsDao;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void storeFile() throws Exception {
        String fileName = "sampleTest.gpx";
        String userTest = "UserTest";

        MockMultipartFile file = new MockMultipartFile("file", fileName, null, "some bytes".getBytes()); 
        String upload_dir_temp = tempFolder.newFolder().getAbsolutePath();
        when(fileStorageProperties.getUploadDir()).thenReturn(upload_dir_temp);

        when(gpsDao.save(any(GPS.class))).thenReturn(new GPS());

        gpsService.storeFile(file, userTest);
        Path uploadFilePath = Paths.get(upload_dir_temp + "/" + userTest + "/" + fileName).toAbsolutePath().normalize();
        // File storeFile = fil
        assertTrue(Files.exists(uploadFilePath));
        // verify(gpsDao, times(1)).save(any(GPS.class));
    }

    @Test
    public void getGPSById(){
        Optional<GPS> option = Optional.of(new GPS());
        when(gpsDao.findById(1)).thenReturn(option);
        gpsService.getGPSById(1);

        verify(gpsDao, times(1)).findById(1);
        verifyNoMoreInteractions(gpsDao);
    }

    @Test
    public void getGpsPage(){
        Pageable sortedByUploadDateTimeDesc = PageRequest.of(0, 3, Sort.by("uploadDateTime").descending());
        List<GPS> content = new ArrayList<>();
        Page<GPS> pageReturn = new PageImpl<>(content);
        when(gpsDao.findAll(sortedByUploadDateTimeDesc)).thenReturn(pageReturn);
        gpsService.getGpsPage(sortedByUploadDateTimeDesc);
    
        verify(gpsDao, times(1)).findAll(sortedByUploadDateTimeDesc);
        verifyNoMoreInteractions(gpsDao);
    }
}