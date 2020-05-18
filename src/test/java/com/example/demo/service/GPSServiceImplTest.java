package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.dao.GPSDao;
import com.example.demo.entity.GPS;
import com.example.demo.exception.FileNotFoundException;
import com.example.demo.exception.FileStorageException;
import com.example.demo.property.FileStorageProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * GPS Service Impl Test
 * 
 * @author hao.cu
 * @since 2020/5/18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GPSServiceImplTest {

    private static final String FILE_NAME = "test.gpx";

    private static final String USE_NAME = "test";

    @Autowired
    GPSService gpsService;

    @Autowired
    FileStorageProperties fileStorateProperties;

    @MockBean
    GPSDao gpsDao;

    @Test
    public void storeFile_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", FILE_NAME, null, "some bytes".getBytes());

        when(gpsDao.save(any(GPS.class))).thenReturn(new GPS());
        String expectFileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                .path(USE_NAME + "/").path(FILE_NAME).toUriString();

        String actualFileDownloadUri = gpsService.storeFile(file, USE_NAME);
        
        verify(gpsDao, times(1)).save(any(GPS.class));
        assertEquals(expectFileDownloadUri, actualFileDownloadUri);
    }

    @Test(expected = FileStorageException.class)
    public void storeFile_error() throws Exception {
        String fileName = "test..gpx";
        String username = "test";

        MockMultipartFile file = new MockMultipartFile("file", fileName, null, "some bytes".getBytes());
        gpsService.storeFile(file, username);
    }

    @Test
    public void loadFileAsResource_success() throws Exception {
        Resource resource = gpsService.loadFileAsResource(USE_NAME, FILE_NAME);
        assertEquals(FILE_NAME, resource.getFilename());

    }

    @Test(expected = FileNotFoundException.class)
    public void loadFileAsResource_error() throws Exception {
        cleanUp();
        gpsService.loadFileAsResource(USE_NAME, FILE_NAME);
    }

    @Test
    public void getGPSById() {
        Optional<GPS> option = Optional.of(new GPS());
        when(gpsDao.findById(1)).thenReturn(option);
        gpsService.getGPSById(1);

        verify(gpsDao, times(1)).findById(1);
        verifyNoMoreInteractions(gpsDao);
    }

    @Test
    public void getGpsPage() {
        Pageable sortedByUploadDateTimeDesc = PageRequest.of(0, 3, Sort.by("uploadDateTime").descending());
        List<GPS> content = new ArrayList<>();
        Page<GPS> pageReturn = new PageImpl<>(content);
        when(gpsDao.findAll(sortedByUploadDateTimeDesc)).thenReturn(pageReturn);
        gpsService.getGpsPage(sortedByUploadDateTimeDesc);

        verify(gpsDao, times(1)).findAll(sortedByUploadDateTimeDesc);
        verifyNoMoreInteractions(gpsDao);
    }

    private void cleanUp() throws IOException {
        String test_Folder = fileStorateProperties.getUploadDir() + "/" + USE_NAME;
        String test_File = fileStorateProperties.getUploadDir() + "/" + USE_NAME +"/"+ FILE_NAME;
        Path sourceFolder = Paths.get(test_Folder).toAbsolutePath().normalize();
        Path sourceFile = Paths.get(test_File).toAbsolutePath().normalize();
        if(Files.exists(sourceFile)){
            Files.delete(sourceFile);
        }
        if(Files.exists(sourceFolder)){
            Files.delete(sourceFolder);
        }
    }
}