package com.vanhack.filezipper.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileZipperServiceTest {
	
	private FileZipperService fileZipperService;
    private static Path workingDir;
    
	@Autowired
	public FileZipperServiceTest(FileZipperService fileZipperService) {
		this.fileZipperService = fileZipperService;
	}

   @BeforeAll
    public static void init() {
        workingDir = Path.of("", "src/test/resources/files");
    }
	
	@Test
	void testZipFiles() throws IOException {
        Path file1 = workingDir.resolve("test1.txt");
        Path file2 = workingDir.resolve("test2.txt");
        
		Map<String, FileInputStream> filesMap = new HashMap();
		filesMap.put(file1.toString(), new FileInputStream(file1.toFile()));
		filesMap.put(file2.toString(), new FileInputStream(file2.toFile()));
		this.fileZipperService.zipFiles(filesMap);
	}
}
