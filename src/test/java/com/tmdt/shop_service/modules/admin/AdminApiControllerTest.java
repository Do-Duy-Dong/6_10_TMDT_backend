package com.tmdt.shop_service.modules.admin;

import com.tmdt.shop_service.core.exception.DuplicateResourceException;
import com.tmdt.shop_service.core.exception.GlobalExceptionHandler;
import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.auth.ApiKeyAuthenticationFilter;
import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.application.service.BaseCategoryService;
import com.tmdt.shop_service.modules.categories.application.service.CategoryService;
import com.tmdt.shop_service.modules.categories.application.service.ModuleCategoryService;
import com.tmdt.shop_service.modules.categories.infrastructure.controller.AdminBaseCategoryController;
import com.tmdt.shop_service.modules.categories.infrastructure.controller.AdminCategoriesController;
import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.application.service.DiscountService;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import com.tmdt.shop_service.modules.discount.infrastructure.controller.AdminDiscountController;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.application.service.LaptopService;
import com.tmdt.shop_service.modules.laptop.infrastructure.controller.AdminLaptopController;
import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.application.service.PostService;
import com.tmdt.shop_service.modules.post.domain.PostStatus;
import com.tmdt.shop_service.modules.post.infrastructure.controller.AdminPostController;
import com.tmdt.shop_service.modules.warehouse.application.dto.CountStoreModelResponse;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.application.service.StoreModelService;
import com.tmdt.shop_service.modules.warehouse.application.service.WarehouseService;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.infrastructure.controller.AdminStoreModelController;
import com.tmdt.shop_service.modules.warehouse.infrastructure.controller.AdminWarehouseController;
import com.tmdt.shop_service.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        AdminLaptopController.class,
        AdminDiscountController.class,
        AdminWarehouseController.class,
        AdminStoreModelController.class,
        AdminCategoriesController.class,
        AdminBaseCategoryController.class,
        AdminPostController.class
})
@Import({AdminApiControllerTest.AdminTestSecurityConfig.class, GlobalExceptionHandler.class})
class AdminApiControllerTest {
    private static final String ADMIN_API_KEY = "api-key-admin";
    private static final String USER_API_KEY = "api-key-user";

    @MockitoBean
    LaptopService laptopService;

    @MockitoBean
    DiscountService discountService;

    @MockitoBean
    WarehouseService warehouseService;

    @MockitoBean
    StoreModelService storeModelService;

    @MockitoBean
    CategoryService categoryService;

    @MockitoBean
    BaseCategoryService baseCategoryService;

    @MockitoBean
    ModuleCategoryService moduleCategoryService;

    @MockitoBean
    PostService postService;

    @MockitoBean
    JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;

    @Test
    void adminApiWithoutApiKeyReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/v1/admin/laptops"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminApiWithCustomerApiKeyReturnsForbidden() throws Exception {
        mockMvc.perform(get("/v1/admin/laptops").header("X-API-KEY", USER_API_KEY))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanListLaptopsWithFilters() throws Exception {
        var laptop = LaptopDto.builder()
                .id(10L)
                .name("Dell XPS 13")
                .description("Ultrabook")
                .isActive(1)
                .originalPrice(new BigDecimal("25000000"))
                .build();
        when(laptopService.getList(any(), eq("Dell"), eq(1), eq(new BigDecimal("10000000")), eq(new BigDecimal("30000000"))))
                .thenReturn(new PageImpl<>(List.of(laptop), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/v1/admin/laptops")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .param("name:ct", "Dell")
                        .param("isActive", "1")
                        .param("originalPrice:ge", "10000000")
                        .param("originalPrice:le", "30000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(1)))
                .andExpect(jsonPath("$.results[0].id").value(10))
                .andExpect(jsonPath("$.results[0].name").value("Dell XPS 13"))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void adminCanCreateLaptop() throws Exception {
        var request = validLaptopRequest();
        var response = LaptopDto.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .originalPrice(request.getOriginalPrice())
                .brandId(request.getBrandId())
                .ramId(request.getRamId())
                .cpuId(request.getCpuId())
                .slug(request.getSlug())
                .build();
        when(laptopService.create(any(CreateLaptopRequest.class), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/v1/admin/laptops")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validLaptopJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dell XPS 13"));
    }

    @Test
    void createLaptopWithoutNameReturnsBadRequest() throws Exception {
        var request = validLaptopRequest();
        request.setName("");

        mockMvc.perform(post("/v1/admin/laptops")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validLaptopJson().replace("\"name\": \"Dell XPS 13\"", "\"name\": \"\"")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void adminCanUpdateLaptopStatus() throws Exception {
        mockMvc.perform(patch("/v1/admin/laptops/1")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .param("status", "0"))
                .andExpect(status().isOk());

        verify(laptopService).updateStatus(1L, 0);
    }

    @Test
    void getMissingLaptopReturnsNotFound() throws Exception {
        when(laptopService.getById(999L)).thenThrow(new ResourceNotFoundException("Laptop Not Found"));

        mockMvc.perform(get("/v1/admin/laptops/999").header("X-API-KEY", ADMIN_API_KEY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Laptop Not Found"));
    }

    @Test
    void adminCanCreateWarehouse() throws Exception {
        var response = WarehouseDto.builder()
                .id(1L)
                .name("Kho Hà Nội")
                .address("Hà Nội")
                .isActive(1)
                .build();
        when(warehouseService.createWarehouse(any())).thenReturn(response);

        mockMvc.perform(post("/v1/admin/warehouses")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Kho Hà Nội",
                                  "address": "Hà Nội",
                                  "isActive": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kho Hà Nội"));
    }

    @Test
    void createWarehouseWithoutNameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/admin/warehouses")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "address": "Hà Nội",
                                  "isActive": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void adminCanImportStoreModelsBySerialNumber() throws Exception {
        mockMvc.perform(post("/v1/admin/store-models")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "warehouseId": 1,
                                  "laptopId": 10,
                                  "serialNumbers": ["SN-001", "SN-002"],
                                  "status": "NEW"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void duplicateSerialNumberReturnsConflict() throws Exception {
        doThrow(new DuplicateResourceException("SN của máy đã tồn tại"))
                .when(storeModelService).createStoreModel(any());

        mockMvc.perform(post("/v1/admin/store-models")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "warehouseId": 1,
                                  "laptopId": 10,
                                  "serialNumbers": ["SN-001"],
                                  "status": "NEW"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("SN của máy đã tồn tại"));
    }

    @Test
    void adminCanCountStoreModelsByParams() throws Exception {
        var count = CountStoreModelResponse.builder()
                .warehouseId(1L)
                .warehouseName("Kho Hà Nội")
                .laptopId(10L)
                .laptopName("Dell XPS 13")
                .status(StoreModelStatus.NEW)
                .quantity(2L)
                .build();
        when(storeModelService.getStoreModelsByParams(any(), eq("Dell"), eq(1L), eq(List.of(StoreModelStatus.NEW))))
                .thenReturn(new PageImpl<>(List.of(count), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/v1/admin/store-models")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .param("nameLaptop:ct", "Dell")
                        .param("warehouseId:eq", "1")
                        .param("status:in", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].quantity").value(2))
                .andExpect(jsonPath("$.results[0].status").value("NEW"));
    }

    @Test
    void adminCanUpdateStoreModelStatus() throws Exception {
        mockMvc.perform(patch("/v1/admin/store-models/5")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .param("status", "SOLD"))
                .andExpect(status().isNoContent());

        verify(storeModelService).updateStatusForStoreModel(5L, StoreModelStatus.SOLD);
    }

    @Test
    void adminCanCreateDiscount() throws Exception {
        var response = DiscountDto.builder()
                .id(1L)
                .name("Giảm 10%")
                .code("SALE10")
                .quantity(100)
                .type(DiscountType.PERCENT)
                .value(10L)
                .isActive(1)
                .build();
        when(discountService.create(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/v1/admin/discounts")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Giảm 10%",
                                  "code": "SALE10",
                                  "quantity": 100,
                                  "moduleIds": [10],
                                  "userIds": [2],
                                  "value": 10,
                                  "type": "PERCENT",
                                  "expiryFrom": "2026-05-01T00:00:00",
                                  "expiryTo": "2026-12-31T23:59:59",
                                  "isActive": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SALE10"))
                .andExpect(jsonPath("$.type").value("PERCENT"));
    }

    @Test
    void invalidDiscountDateReturnsBadRequest() throws Exception {
        when(discountService.create(any(), eq(1L)))
                .thenThrow(new IllegalArgumentException("Thời gian kết thúc không thể trước thời gian bắt đầu"));

        mockMvc.perform(post("/v1/admin/discounts")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Sai thời gian",
                                  "code": "BAD_DATE",
                                  "quantity": 10,
                                  "value": 100000,
                                  "type": "FIXED",
                                  "expiryFrom": "2026-12-31T00:00:00",
                                  "expiryTo": "2026-01-01T00:00:00",
                                  "isActive": 1
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Thời gian kết thúc không thể trước thời gian bắt đầu"));
    }

    @Test
    void adminCanCreateCategory() throws Exception {
        var response = CategoryDto.builder()
                .id(1L)
                .name("RAM 16GB")
                .code("RAM_16")
                .baseCodeId(2L)
                .baseCode("RAM")
                .isActive(1)
                .build();
        when(categoryService.create(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/v1/admin/categories")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "RAM 16GB",
                                  "code": "RAM_16",
                                  "baseCodeId": 2,
                                  "isActive": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("RAM_16"));
    }

    @Test
    void createCategoryWithoutBaseCodeReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/v1/admin/categories")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "RAM 16GB",
                                  "code": "RAM_16",
                                  "isActive": 1
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void adminCanCreateBaseCategory() throws Exception {
        var response = BaseCategoryDto.builder()
                .id(2L)
                .name("RAM")
                .code("RAM")
                .isActive(1)
                .build();
        when(baseCategoryService.create(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/v1/admin/base-categories")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "RAM",
                                  "code": "RAM",
                                  "isActive": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("RAM"));
    }

    @Test
    void adminCanCreatePost() throws Exception {
        var response = PostDto.builder()
                .id(1L)
                .title("Tin khuyến mãi")
                .description("Nội dung")
                .status(PostStatus.DRAFT)
                .build();
        when(postService.create(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/v1/admin/posts")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Tin khuyến mãi",
                                  "description": "Nội dung",
                                  "slug": "tin-khuyen-mai",
                                  "status": "DRAFT"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void adminCanFilterPostsByStatus() throws Exception {
        var response = PostDto.builder()
                .id(1L)
                .title("Tin khuyến mãi")
                .description("Nội dung")
                .status(PostStatus.ACTIVE)
                .build();
        when(postService.getList(any(), eq("Tin"), any(), any(), eq(List.of(PostStatus.ACTIVE))))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/v1/admin/posts")
                        .header("X-API-KEY", ADMIN_API_KEY)
                        .param("title:ct", "Tin")
                        .param("status:in", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].title").value("Tin khuyến mãi"))
                .andExpect(jsonPath("$.results[0].status").value("ACTIVE"));
    }

    @TestConfiguration
    static class AdminTestSecurityConfig {
        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(requests -> requests
                            .requestMatchers("/v1/admin/**").hasAuthority("ROLE_ADMIN")
                            .anyRequest().permitAll())
                    .exceptionHandling(e -> e
                            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                    .addFilterBefore(new ApiKeyAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
    }

    private CreateLaptopRequest validLaptopRequest() {
        var request = new CreateLaptopRequest();
        request.setName("Dell XPS 13");
        request.setDescription("Ultrabook");
        request.setIsActive(1);
        request.setOriginalPrice(new BigDecimal("25000000"));
        request.setBrandId(1L);
        request.setRamId(2L);
        request.setStorageId(3L);
        request.setScreenSizeId(4L);
        request.setGpuId(5L);
        request.setCpuId(6L);
        request.setScreenId(7L);
        request.setSlug("dell-xps-13");
        request.setAttachIds(List.of(100L));
        return request;
    }

    private String validLaptopJson() {
        return """
                {
                  "name": "Dell XPS 13",
                  "description": "Ultrabook",
                  "isActive": 1,
                  "originalPrice": 25000000,
                  "attachIds": [100],
                  "brandId": 1,
                  "ramId": 2,
                  "storageId": 3,
                  "screenSizeId": 4,
                  "gpuId": 5,
                  "cpuId": 6,
                  "screenId": 7,
                  "slug": "dell-xps-13"
                }
                """;
    }
}
