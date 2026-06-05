package com.tmdt.shop_service.modules.laptop.infrastructure.jpa;

import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.laptop.domain.model.OptionLaptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOptionLaptopRepo extends JpaRepository<OptionLaptop, Long> {
    List<OptionLaptop> findByLaptopId(Long laptopId);
    void deleteByLaptopId(Long laptopId);
    List<OptionLaptop> findByIdIn(List<Long> idIn);

    @Query(value = "select distinct laptop.* from laptop\n" +
            "join option_laptop on laptop.id = option_laptop.laptop_id\n" +
            "where option_laptop.id in :idIn", nativeQuery = true)
    List<Laptop> findLaptopByOptionIdIn(@Param("idIn") List<Long> optionId);
}
