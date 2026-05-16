package com.tmdt.shop_service.modules.fileupload.infrastructure.controller;

import com.tmdt.shop_service.modules.fileupload.application.dto.PresignedUrlDto;
import com.tmdt.shop_service.modules.fileupload.application.service.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/upload")
@Tag(name = "Upload Controller")
@RequiredArgsConstructor
public class UploadController {
    final UploadService uploadService;

    @GetMapping("/presigned-url")
    public ResponseEntity<PresignedUrlDto> getPresignedUrl(@RequestParam String fileName) {
        var result = uploadService.getPresignedUrl(fileName, 5, true);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public Object getObjectInfo(@RequestParam String fileName) {
        return uploadService.getObjectInfo(fileName);
    }
}
