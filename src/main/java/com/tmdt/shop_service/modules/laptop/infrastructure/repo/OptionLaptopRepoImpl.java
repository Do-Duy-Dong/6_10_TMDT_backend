package com.tmdt.shop_service.modules.laptop.infrastructure.repo;

import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.laptop.domain.model.OptionLaptop;
import com.tmdt.shop_service.modules.laptop.domain.repo.OptionLaptopRepo;
import com.tmdt.shop_service.modules.laptop.infrastructure.jpa.JpaOptionLaptopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OptionLaptopRepoImpl implements OptionLaptopRepo {
    private final JpaOptionLaptopRepo jpaOptionLaptopRepo;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public OptionLaptop save(OptionLaptop optionLaptop) {
        return jpaOptionLaptopRepo.save(optionLaptop);
    }

    @Override
    public List<OptionLaptop> saveAll(List<OptionLaptop> optionLaptops) {
        return jpaOptionLaptopRepo.saveAll(optionLaptops);
    }

    @Override
    public Optional<OptionLaptop> findById(Long id) {
        return jpaOptionLaptopRepo.findById(id);
    }

    @Override
    public void delete(Long id) {
        jpaOptionLaptopRepo.deleteById(id);
    }

    @Override
    public void deleteByLaptopId(Long laptopId) {
        jpaOptionLaptopRepo.deleteByLaptopId(laptopId);
    }

    @Override
    public List<OptionLaptop> findByLaptopId(Long laptopId) {
        return jpaOptionLaptopRepo.findByLaptopId(laptopId);
    }

    @Override
    public List<OptionLaptop> findByIdIn(List<Long> idIn) {
        return jpaOptionLaptopRepo.findByIdIn(idIn);
    }

    @Override
    public List<Laptop> findLaptopByOptionIdIn(List<Long> optionIds) {
        return jpaOptionLaptopRepo.findLaptopByOptionIdIn(optionIds);
    }
}
