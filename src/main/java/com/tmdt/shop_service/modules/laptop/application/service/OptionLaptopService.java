package com.tmdt.shop_service.modules.laptop.application.service;

import com.tmdt.shop_service.modules.laptop.application.dto.OptionLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopOptionRequest;

import java.util.List;

public interface OptionLaptopService {
    public OptionLaptopDto save(CreateLaptopOptionRequest request, Long laptopId);

    public List<OptionLaptopDto> saveAll(List<CreateLaptopOptionRequest> requests, Long laptopId);

    OptionLaptopDto findById(Long id);

    void delete(Long id);

    void deleteByLaptopId(Long laptopId);

    List<OptionLaptopDto> findByLaptopId(Long laptopId);

    List<OptionLaptopDto> update(Long laptopId, List<CreateLaptopOptionRequest> requests);
}
