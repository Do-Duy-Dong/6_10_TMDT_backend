package com.tmdt.shop_service.modules.attaches.application.request;

import com.tmdt.shop_service.modules.attaches.domain.model.AttachMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAttachRequest {
    @NotNull @NotBlank
    String name;

    @NotNull @NotBlank
    String description;

    @NotNull
    AttachMetadata attachMetadata;
}
