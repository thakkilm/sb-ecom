package com.ecommerce.project.sbecom.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {



    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {

        String originalFileName = image.getOriginalFilename();
        String randomId= UUID.randomUUID().toString();
        String pathName=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String filePath=path+ File.separator+pathName;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return pathName;
    }
}
