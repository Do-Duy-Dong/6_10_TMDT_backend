package com.tmdt.shop_service.modules.attaches.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachMetadata implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    String fileName;
    String filePath;
    String contentType;
    String sizeOfBytes;
}
