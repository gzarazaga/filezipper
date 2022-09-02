package com.vanhack.filezipper.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class FileZipperService {

    public byte[] zipFiles (Map<String, FileInputStream> filesMap) throws IOException {
        Set<String> srcFiles = filesMap.keySet();
        FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
        
        File zipFile = new File("multiCompressed.zip");
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) zipFile.length()];
        try
        {
           //convert file into array of bytes
           fileInputStream = new FileInputStream(zipFile);
           fileInputStream.read(bFile);
           fileInputStream.close();
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }

        return bFile;
    }
}
