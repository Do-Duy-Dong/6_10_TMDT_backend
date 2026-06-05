package com.tmdt.shop_service.modules.warehouse.infrastructure.jpa;

import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaStoreModelRepo extends JpaRepository<StoreModel, Long> {
    List<StoreModel> findByWarehouseId(Long warehouseId);
    Optional<StoreModel> findBySerialNumber(String serialNumber);
    Page<StoreModel> findByWarehouseIdAndStatus(Pageable pageable, Long warehouseId, StoreModelStatus status);
    List<StoreModel> findBySerialNumberIn(List<String> serialNumbers);

    @Query(value = "SELECT * FROM store_model sm where sm.option_id = :optionId and sm.status = :status limit :number offset 0", nativeQuery = true)
    List<StoreModel> getListStoreModelByParams(
            @Param("optionId") Long optionId,
            @Param("number") int number,
            @Param("status") StoreModelStatus status);

    @Modifying
    @Query(value = "update StoreModel set status = :status where id in :ids")
    void updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") StoreModelStatus status);

    List<StoreModel> findAllByIdIn(Collection<Long> ids);
}
