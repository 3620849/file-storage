package com.filestorage.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class AwsService {
    @Autowired
    AmazonS3 s3client;

    public String  saveAtS3(File tempFile) {
        PutObjectRequest putObjectRequest = new PutObjectRequest("dg-1987",
                tempFile.getName(),
                tempFile);
        putObjectRequest.setStorageClass(StorageClass.ReducedRedundancy);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult putObjectResult = s3client.putObject(putObjectRequest);

        URL url = s3client.getUrl("dg-1987",
                tempFile.getName());
       return url.getProtocol()+"://"+url.getHost()+url.getPath();
    }


}
