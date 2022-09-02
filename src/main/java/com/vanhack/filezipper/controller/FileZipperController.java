package com.vanhack.filezipper.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vanhack.filezipper.service.FileZipperService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class FileZipperController {

	private FileZipperService fileZipperService;
	
	@Autowired
	public FileZipperController (FileZipperService fileZipperService) {
		this.fileZipperService = fileZipperService;
	}


	@PostMapping(value = "/files/zip", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> uploadFiles(@RequestPart MultipartFile[] fileData) throws IOException {
        File uploadRootDir = new File("(directory)");

        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        
        Map<String, FileInputStream> uploadedFiles = new HashMap<String, FileInputStream>();
        List<String> failedFiles = new ArrayList<String>();

        for (MultipartFile file : fileData) {
            String name = file.getOriginalFilename();

            if (name != null && name.length() > 0) {
                try {
                	String fileName = uploadRootDir.getAbsolutePath() + File.separator + name;
                    File serverFile = new File(fileName);

                    BufferedOutputStream stream = new BufferedOutputStream(new 
                    FileOutputStream(serverFile));
                    stream.write(file.getBytes());
                    stream.close();

                    uploadedFiles.put(fileName, new FileInputStream(serverFile));
                } catch (Exception e) {
                    failedFiles.add(name);
                }
            }
        }
        
        ByteArrayResource resource = new ByteArrayResource(fileZipperService.zipFiles(uploadedFiles));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                            .filename("multiFiles.zip")
                            .build().toString())
                .body(resource);
    }
}