package com.tmdt.shop_service.modules.laptop.domain.repo;

import com.tmdt.shop_service.modules.laptop.application.dto.OptionLaptopDto;
import com.tmdt.shop_service.modules.laptop.domain.model.OptionLaptop;

import java.util.List;
import java.util.Optional;

public interface OptionLaptopRepo {
    OptionLaptop save(OptionLaptop optionLaptop);

    List<OptionLaptop> saveAll(List<OptionLaptop> optionLaptops);

    Optional<OptionLaptop> findById(Long id);

    void delete(Long id);

    void deleteByLaptopId(Long laptopId);

    List<OptionLaptop> findByLaptopId(Long laptopId);
}
