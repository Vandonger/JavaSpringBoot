package com.onemount.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.onemount.demo.entity.MyFile;
import com.onemount.demo.response.ResponseFile;
import com.onemount.demo.response.ResponseMessage;
import com.onemount.demo.service.MyFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin("http://localhost:8080")
public class APIController {
    @Autowired
    private MyFileService fileService;

    @PostMapping("/")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {
        String message = "";
        try {
            fileService.save(file, description);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping(value = "/files")
    public ResponseEntity<List<ResponseFile>> findAllFiles() {
        List<ResponseFile> files = fileService.findAllFiles().stream().map(file-> {
            String fileDownloadUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/static/")
            .path(file.getId() + "-" + file.getFileName())
            .toUriString();
            return new ResponseFile(
                file.getFileName(),
                fileDownloadUri,
                file.getDescription());
            }).collect(Collectors.toList());
            return ResponseEntity.ok().body(files);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseFile> findById(@PathVariable Long id) {
        MyFile file = fileService.getFile(id);
        String fileDownloadUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/static/")
            .path(file.getId() + "-" + file.getFileName())
            .toUriString();
        ResponseFile responseFile = new ResponseFile(file.getFileName(), fileDownloadUri,file.getDescription());       
            return ResponseEntity.ok().body(responseFile);
    }

}
