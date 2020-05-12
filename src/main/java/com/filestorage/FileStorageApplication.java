package com.filestorage;

import com.filestorage.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileStorageApplication  {

	public static void main(String[] args) {
		SpringApplication.run(FileStorageApplication.class, args);
	}

}
