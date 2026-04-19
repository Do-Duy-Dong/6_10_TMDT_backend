package com.tmdt.shop_service.modules.users.infrastructure.jpa;

import com.tmdt.shop_service.modules.users.domain.model.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaObjectRepo extends JpaRepository<Object, Long> {
}
