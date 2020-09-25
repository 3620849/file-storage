package com.filestorage.controller;

import com.filestorage.model.Media;
import com.filestorage.service.AwsService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {
    @Autowired
    AwsService awsService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Media> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String path = System.getProperty("java.io.tmpdir") + "/fs";
        File directory = new File(path);
        LOGGER.info(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
        UUID uuid = UUID.randomUUID();
        String newFilename = uuid.toString();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        File tempFile = new File(path + "/" + newFilename + "." + extension);
        String fileUrl = null;
        try {
            file.transferTo(tempFile);
            fileUrl = awsService.saveAtS3(tempFile);
            new ResponseEntity(Media.builder().url(fileUrl), HttpStatus.OK);
            LOGGER.info(fileUrl);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            boolean delete = tempFile.delete();
            if(!delete){
                LOGGER.error(tempFile.getName()+" was not deleted and clog tmp folder");
            }
        }
        return new ResponseEntity(Media.builder().url(fileUrl).build(), HttpStatus.OK);
    }
}
