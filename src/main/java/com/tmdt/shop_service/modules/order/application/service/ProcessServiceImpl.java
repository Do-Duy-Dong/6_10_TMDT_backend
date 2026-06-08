package com.tmdt.shop_service.modules.order.application.service;

import com.tmdt.shop_service.modules.order.domain.repo.ProcessRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService{
    final ProcessRepo processRepo;


}
