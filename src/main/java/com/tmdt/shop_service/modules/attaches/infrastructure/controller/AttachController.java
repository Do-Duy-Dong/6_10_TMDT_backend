package com.tmdt.shop_service.modules.attaches.infrastructure.controller;

import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.request.CreateAttachRequest;
import com.tmdt.shop_service.modules.attaches.application.service.AttachService;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Attaches controller")
@RequestMapping("/v1/attaches")
public class AttachController {
    final AttachService attachService;

    @PostMapping
    public AttachDto create(
            @RequestBody @Valid CreateAttachRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetails) {
        return attachService.create(request, userDetails.getId());
    }
}
