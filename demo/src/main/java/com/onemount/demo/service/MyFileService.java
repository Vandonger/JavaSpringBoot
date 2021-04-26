package com.onemount.demo.service;

import java.io.IOException;
import java.util.List;

import com.onemount.demo.entity.MyFile;
import com.onemount.demo.repository.MyFileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MyFileService {

    @Autowired(required = true)
    private MyFileRepository fileRepository;

    public MyFile save(MultipartFile file, String description) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        MyFile myFile = new  MyFile(filename, description, file.getContentType(), file.getBytes());
        return fileRepository.save(myFile);
    }

    public MyFile getFile(Long id) {
        if(fileRepository.findById(id).isPresent()) {
            return fileRepository.findById(id).get();
        } else throw new ResourceNotFoundException();
    }

    public List<MyFile> findAllFiles() {
        return fileRepository.findAll();
    }  
}
