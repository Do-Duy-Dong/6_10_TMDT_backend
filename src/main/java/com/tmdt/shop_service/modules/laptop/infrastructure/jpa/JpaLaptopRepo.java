package com.tmdt.shop_service.modules.laptop.infrastructure.jpa;

import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLaptopRepo extends JpaRepository<Laptop, Long> {
    List<Laptop> findByIdIn(List<Long> ids);

    Optional<Laptop> findLaptopBySlug(String slug);

    List<Laptop> findLaptopsByParentIdInOrIdIn(List<Long> parentId, List<Long> idIn);
}
