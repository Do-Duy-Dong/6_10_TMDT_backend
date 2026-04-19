package com.tmdt.shop_service.modules.laptop.infrastructure.jpa;

import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLaptopRepo extends JpaRepository<Laptop, Long> {
}
