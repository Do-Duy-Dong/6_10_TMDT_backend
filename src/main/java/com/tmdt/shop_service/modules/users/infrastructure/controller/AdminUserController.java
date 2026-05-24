package com.tmdt.shop_service.modules.users.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/users")
public class AdminUserController {
    final UserService userService;

    @RequestMapping
    public CollectionResponse<UserDto> getList(
            @ParameterObject Pageable pageable,
            @RequestParam(name = "name:ct", required = false) String nameCt,
            @RequestParam(name = "email:ct", required = false) String emailCt,
            @RequestParam(name = "phoneNumber:ct", required = false) String phoneNumberCt,
            @RequestParam(name = "isActive", required = false) Integer isActive) {
        var page = userService.getListByParams(pageable, nameCt, emailCt, phoneNumberCt, isActive);
        Integer nextPageToken = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(page.getContent(), nextPageToken, page.getTotalElements());
    }
}
