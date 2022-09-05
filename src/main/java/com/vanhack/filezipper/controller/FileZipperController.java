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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@Api(tags={"File Zipper Controller"})
public class FileZipperController {

	private FileZipperService fileZipperService;
	
	@Autowired
	public FileZipperController (FileZipperService fileZipperService) {
		this.fileZipperService = fileZipperService;
	}

	@ApiOperation(value = "Post a list of files to get a zip file", response = Iterable.class, tags = "fileZipper")
	@ApiResponses(value = { 
	      @ApiResponse(code = 200, message = "Success|OK"),
	      @ApiResponse(code = 401, message = "not authorized!"), 
	      @ApiResponse(code = 403, message = "forbidden!!!"),
	      @ApiResponse(code = 404, message = "not found!!!") })
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
        		.contentType(MediaType.parseMediaType("application/zip"))
                .contentLength(resource.contentLength())
                .header("Content-disposition", "attachment; filename=multiFiles.zip")
                .body(resource);
    }
}