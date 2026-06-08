package com.tmdt.shop_service.modules.order.infrastructure.repo;

import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.domain.PaymentStatus;
import com.tmdt.shop_service.modules.order.domain.PaymentType;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import com.tmdt.shop_service.modules.order.domain.model.Order;
import com.tmdt.shop_service.modules.order.domain.repo.OrderRepo;
import com.tmdt.shop_service.modules.order.infrastructure.jpa.JpaOrderRepo;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class OrderRepoImpl implements OrderRepo {
    private final JpaOrderRepo jpaOrderRepo;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Order save(Order order) {
        return jpaOrderRepo.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaOrderRepo.findById(id);
    }

    @Override
    public List<Order> findByOwnerId(Long ownerId) {
        return jpaOrderRepo.findByOwnerId(ownerId);
    }

    @Override
    public List<Order> findAll() {
        return jpaOrderRepo.findAll();
    }

    @Override
    public Page<OrderDto> getOrderByParams(
            Pageable pageable,
            List<PaymentStatus> statusIn,
            List<ProcessStatus> processStatusIn,
            ShipmentType shipmentTypeEq,
            String emailEq) {
        Map<String, Object> params = new HashedMap<>();
        StringBuilder sql = new StringBuilder("WITH OrderQuantity AS (\n" +
                "    SELECT order_id, SUM(quantity) AS total_quantity\n" +
                "    FROM order_detail\n" +
                "    GROUP BY order_id\n" +
                "),\n" +
                "     LatestProcess AS (\n" +
                "         SELECT order_id, status,\n" +
                "                ROW_NUMBER() OVER (\n" +
                "                    PARTITION BY order_id\n" +
                "                    ORDER BY create_at DESC\n" +
                "                    ) AS rn\n" +
                "         FROM process\n" +
                "         where 1 = 1\n");
        if (processStatusIn != null && !processStatusIn.isEmpty()) {
            sql.append("and process.status in :processStatusIn\n");
            params.put("processStatusIn", processStatusIn.stream().map(ProcessStatus::getValue));
        }
        sql.append("     )\n");
        sql.append("SELECT o.id,\n" +
                "       o.owner_id,\n" +
                "       o.discount_id,\n" +
                "       o.full_name,\n" +
                "       o.phone_number,\n" +
                "       o.email AS order_email,\n" +
                "       o.shipment_type,\n" +
                "       o.payment_type,\n" +
                "       o.district,\n" +
                "       o.province,\n" +
                "       o.commune,\n" +
                "       o.address_detail,\n" +
                "       o.total,\n" +
                "       o.create_at,\n" +
                "       o.update_at,\n" +
                "       lp.status AS process_status,\n" +
                "       pd.status AS payment_status,\n" +
                "       oq.total_quantity AS quantity,\n" +
                "       count(*) over() as total_element\n" +
                "FROM orders o\n" +
                "         JOIN OrderQuantity oq\n" +
                "              ON oq.order_id = o.id\n" +
                "         JOIN payment_detail pd\n" +
                "              ON pd.order_id = o.id\n" +
                "         JOIN LatestProcess lp\n" +
                "              ON lp.order_id = o.id AND lp.rn = 1\n" +
                "where 1 = 1\n");
        if (statusIn != null && !statusIn.isEmpty()) {
            sql.append("and pd.status in :statusIn\n");
            params.put("statusIn", statusIn.stream().map(PaymentStatus::getValue));
        }
        if (shipmentTypeEq != null) {
            sql.append("and o.shipment_type = :shipTypeEq\n");
            params.put("shipTypeEq", shipmentTypeEq);
        }
        if (emailEq != null && !emailEq.isEmpty()) {
            sql.append("and lower(o.email) like :emailCt\n");
            params.put("emailCt", StringUtils.likeLowerContainString(emailEq));
        }
        List<String> sortField = List.of("create_at", "id", "total");
        String pagination = StringUtils.genDirection(sortField, pageable, "o");
        sql.append(pagination);

        AtomicLong total_element = new AtomicLong();
        ShipmentType.ShipmentTypeConverter shipmentTypeConverter = new ShipmentType.ShipmentTypeConverter();
        PaymentType.PaymentTypeConverter paymentTypeConverter = new PaymentType.PaymentTypeConverter();
        ProcessStatus.ProcessStatusConverter processStatusConverter = new ProcessStatus.ProcessStatusConverter();
        PaymentStatus.PaymentStatusConverter paymentStatusConverter = new PaymentStatus.PaymentStatusConverter();
        List<OrderDto> orderDtos = namedParameterJdbcTemplate.query(sql.toString(), params, (rs, rn) -> {
            total_element.set(rs.getLong("total_element"));
            return OrderDto.builder()
                    .id(rs.getLong("id"))
                    .ownerId(rs.getLong("owner_id"))
                    .discountId(rs.getObject("discount_id") != null ? rs.getLong("discount_id") : null)
                    .fullName(rs.getString("full_name"))
                    .phoneNumber(rs.getString("phone_number"))
                    .email(rs.getString("order_email"))
                    .shipmentType(shipmentTypeConverter.convertToEntityAttribute(rs.getInt("shipment_type")))
                    .paymentType(paymentTypeConverter.convertToEntityAttribute(rs.getInt("payment_type")))

                    .district(rs.getString("district"))
                    .province(rs.getString("province"))
                    .commune(rs.getString("commune"))
                    .addressDetail(rs.getString("address_detail"))
                    .total(rs.getBigDecimal("total"))
                    .createAt(rs.getTimestamp("create_at") != null ? rs.getTimestamp("create_at").toLocalDateTime() : null)
                    .updateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null)
                    .status(processStatusConverter.convertToEntityAttribute(rs.getInt("process_status")))
                    .paymentStatus(paymentStatusConverter.convertToEntityAttribute(rs.getInt("payment_status")))
                    .orderDetails(new ArrayList<>())
                    .build();
        });

        return new PageImpl<>(orderDtos, pageable, total_element.get());
    }
}
