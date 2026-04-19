package com.tmdt.shop_service.modules.attaches.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachStorageMetadata implements Serializable {
    String storage;
    String path;
    String region;
}
