package com.example.demo.controller;

import com.example.demo.dto.GPSDto;
import com.example.demo.service.GPSService;

import org.aspectj.util.FileUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@AutoConfigureMockMvc
@WebMvcTest(controllers = GPSController.class)
public class GPSControllerTest {
    
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GPSService gpsService;

    @Before
    public void setup() {
      DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
      this.mockMvc = builder.build();
    }

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void uploadFile_Success() throws Exception {
        String fileName = "sampleTest.gpx";
        MockMultipartFile file = new MockMultipartFile("file", fileName, null, "some bytes".getBytes());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/uploadFile").file(file).header("user-name", "test");
        when(gpsService.storeFile(file, "test")).thenReturn(fileName);
        this.mockMvc.perform(builder).andExpect(status().isOk())
        .andExpect(content().string(fileName));
    }

    @Test
    public void uploadFile_NotAllow() throws Exception {
        String fileName = "sampleTest.png";
        MockMultipartFile file = new MockMultipartFile("file", fileName, null, "some bytes".getBytes());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/uploadFile").file(file).header("user-name", "test");
        when(gpsService.storeFile(file, "test")).thenReturn("success");
        this.mockMvc.perform(builder).andExpect(status().isForbidden())
        .andExpect(content().string("File is not allow to upload"));
    }

    @Test
    public void downloadFile() throws Exception {
        String contentFile = "this is location file";
        // Create a temporary file.
        File tempFile = tempFolder.newFile("text.gpx");
        FileUtil.writeAsString(tempFile, contentFile);
        Path filePath = tempFile.toPath();
        Resource resource = new UrlResource(filePath.toUri());

        when(gpsService.loadFileAsResource("test", "test.gpx")).thenReturn(resource);

        MvcResult result = this.mockMvc.perform(get("/downloadFile/test/test.gpx")).andExpect(status().isOk()).andReturn();
        Assert.assertEquals(contentFile, result.getResponse().getContentAsString());
    }

    @Test
    public void getGPSDetail() throws Exception{
        Date today = new Date();
        GPSDto found = new GPSDto(1,"test.gpx","download/test.gpx","testuser",today);
        when(gpsService.getGPSById(1)).thenReturn(found);
        this.mockMvc.perform(get("/gps/{id}",1)).andExpect(status().isOk());
        
        verify(gpsService, times(1)).getGPSById(1);
        verifyNoMoreInteractions(gpsService);
    }

    @Test
    public void getGpsPage() throws Exception{
        Date today = new Date();
        GPSDto dto = new GPSDto(1,"test.gpx","download/test.gpx","testuser",today);
        List<GPSDto> listGps = new ArrayList<>();
        listGps.add(dto);
        Page<GPSDto> found = new PageImpl<>(listGps);

        when(gpsService.getGpsPage(any())).thenReturn(found);
        this.mockMvc.perform(get("/gps")).andExpect(status().isOk());
        
        verify(gpsService, times(1)).getGpsPage(any());
        verifyNoMoreInteractions(gpsService);
    }

}