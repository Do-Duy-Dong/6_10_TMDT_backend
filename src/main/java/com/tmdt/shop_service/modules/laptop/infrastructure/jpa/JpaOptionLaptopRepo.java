package com.tmdt.shop_service.modules.laptop.infrastructure.jpa;

import com.tmdt.shop_service.modules.laptop.domain.model.OptionLaptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOptionLaptopRepo extends JpaRepository<OptionLaptop, Long> {
    List<OptionLaptop> findByLaptopId(Long laptopId);
    void deleteByLaptopId(Long laptopId);
}
