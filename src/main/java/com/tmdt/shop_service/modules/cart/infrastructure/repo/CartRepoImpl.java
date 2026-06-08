package com.tmdt.shop_service.modules.cart.infrastructure.repo;

import com.tmdt.shop_service.modules.cart.application.dto.CartDto;
import com.tmdt.shop_service.modules.cart.domain.model.Cart;
import com.tmdt.shop_service.modules.cart.domain.repo.CartRepo;
import com.tmdt.shop_service.modules.cart.infrastructure.jpa.JpaCartRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepoImpl implements CartRepo {
    private final JpaCartRepo jpaCartRepo;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Cart save(Cart cart) {
        return jpaCartRepo.save(cart);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return jpaCartRepo.findById(id);
    }

    @Override
    public Optional<Cart> findByOwnerIdAndOptionId(Long ownerId, Long optionId) {
        return jpaCartRepo.findByOwnerIdAndOptionId(ownerId, optionId);
    }

    @Override
    public List<CartDto> findByOwnerId(Long ownerId) {
        Map<String, Object> params = new HashedMap<>();
        StringBuilder sql = new StringBuilder("select cart.id,\n" +
                "       l.id as laptop_id,\n" +
                "       l.name as laptop_name,\n" +
                "       l.slug as laptop_slug,\n" +
                "       ol.id as option_id,\n" +
                "       ol.name as option_name,\n" +
                "       ol.price as price,\n" +
                "       l.original_price as original_price,\n" +
                "       cart.quantity as quantity,\n" +
                "       cate.name as brand_name,\n" +
                "       sum(\n" +
                "               case\n" +
                "                   when st.status = 0 and l.is_active = 1 then 1\n" +
                "                   else 0\n" +
                "                   end\n" +
                "       ) as total\n" +
                "from cart\n" +
                "join option_laptop ol\n" +
                "    on cart.option_id = ol.id\n" +
                "join laptop l\n" +
                "    on l.id = ol.laptop_id\n" +
                "join categories cate\n" +
                "    on cate.id = l.brand_id\n" +
                "join store_model st\n" +
                "    on ol.id = st.option_id and l.id = st.laptop_id\n" +
                "where cart.owner_id = :ownerId\n" +
                "group by cart.id, l.id, l.name, l.slug, ol.id, ol.name, ol.price, l.original_price, cart.quantity, cate.name;");
        params.put("ownerId", ownerId);

        return namedParameterJdbcTemplate.query(sql.toString(), params, (rs, rn) -> {
            return CartDto.builder()
                    .id(rs.getLong("id"))
                    .laptopId(rs.getLong("laptop_id"))
                    .laptopName(rs.getString("laptop_name"))
                    .laptopSlug(rs.getString("laptop_slug"))
                    .optionId(rs.getLong("option_id"))
                    .optionName(rs.getString("option_name"))
                    .price(rs.getLong("price"))
                    .originalPrice(rs.getLong("original_price"))
                    .quantity(rs.getInt("quantity"))
                    .brandName(rs.getString("brand_name"))
                    .total(rs.getLong("total"))
                    .build();
        });
    }

    @Override
    public void delete(Long id) {
        jpaCartRepo.deleteById(id);
    }
}
