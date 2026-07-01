package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Drug;
import com.neusoft.hospital.mapper.DrugMapper;
import com.neusoft.hospital.service.impl.DrugServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * DrugService 单元测试（管理员操作药品 CRUD）
 * <p>
 * 模拟 DrugMapper 层，验证药品新增、修改、逻辑删除、查询和分页的业务逻辑。
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class DrugServiceTest {

    @Mock
    private DrugMapper drugMapper;

    @InjectMocks
    private DrugServiceImpl drugService;

    @Captor
    private ArgumentCaptor<Drug> drugCaptor;

    private Drug sampleDrug;

    @BeforeEach
    void setUp() {
        sampleDrug = new Drug();
        sampleDrug.setId(1L);
        sampleDrug.setDrugName("阿莫西林胶囊");
        sampleDrug.setDrugCode("AMX001");
        sampleDrug.setSpecification("250mg*24粒");
        sampleDrug.setUnit("盒");
        sampleDrug.setManufacturer("华北制药");
        sampleDrug.setUnitPrice(new BigDecimal("15.50"));
        sampleDrug.setStock(1000);
        sampleDrug.setStatus(1);
    }

    @Test
    @DisplayName("新增药品 → 验证 Mapper.insert 被调且参数透传")
    void testAddDrug() {
        drugService.add(sampleDrug);

        verify(drugMapper, times(1)).insert(drugCaptor.capture());
        Drug captured = drugCaptor.getValue();

        assertAll("新增药品参数正确",
                () -> assertEquals("阿莫西林胶囊", captured.getDrugName()),
                () -> assertEquals("AMX001", captured.getDrugCode()),
                () -> assertEquals("250mg*24粒", captured.getSpecification()),
                () -> assertEquals(new BigDecimal("15.50"), captured.getUnitPrice()),
                () -> assertEquals(1000, captured.getStock())
        );
    }

    @Test
    @DisplayName("修改药品 → 验证 Mapper.update 被调用")
    void testUpdateDrug() {
        sampleDrug.setUnitPrice(new BigDecimal("18.00"));
        sampleDrug.setStock(800);

        drugService.update(sampleDrug);

        verify(drugMapper, times(1)).update(drugCaptor.capture());
        Drug captured = drugCaptor.getValue();
        assertEquals(1L, captured.getId());
        assertEquals(new BigDecimal("18.00"), captured.getUnitPrice());
        assertEquals(800, captured.getStock());
    }

    @Test
    @DisplayName("删除药品（逻辑删除）→ 验证 Mapper.deleteById 被调用")
    void testDeleteDrug() {
        drugService.delete(1L);

        verify(drugMapper, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("查询药品详情 → 返回 Mock 数据")
    void testGetById() {
        when(drugMapper.selectById(1L)).thenReturn(sampleDrug);

        Drug result = drugService.getById(1L);

        assertNotNull(result);
        assertEquals("阿莫西林胶囊", result.getDrugName());
        assertEquals(new BigDecimal("15.50"), result.getUnitPrice());
        verify(drugMapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("查询不存在的药品 → 返回 null")
    void testGetByIdNotFound() {
        when(drugMapper.selectById(999L)).thenReturn(null);

        Drug result = drugService.getById(999L);

        assertNull(result);
    }

    @Test
    @DisplayName("分页查询药品 → 返回正确的 PageResult")
    void testListDrugs() {
        Drug drug2 = new Drug();
        drug2.setId(2L);
        drug2.setDrugName("布洛芬缓释胶囊");
        drug2.setUnitPrice(new BigDecimal("25.00"));

        when(drugMapper.selectList(eq("阿莫"), eq(0), eq(10)))
                .thenReturn(List.of(sampleDrug, drug2));
        when(drugMapper.selectCount(eq("阿莫")))
                .thenReturn(2L);

        PageResult<Drug> result = drugService.list("阿莫", 1, 10);

        assertAll("分页结果正确",
                () -> assertEquals(2L, result.getTotal()),
                () -> assertEquals(2, result.getRows().size()),
                () -> assertEquals("阿莫西林胶囊", result.getRows().get(0).getDrugName())
        );
        verify(drugMapper).selectList("阿莫", 0, 10);
        verify(drugMapper).selectCount("阿莫");
    }

    @Test
    @DisplayName("分页查询空结果 → total=0, rows=[]")
    void testListEmpty() {
        when(drugMapper.selectList(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(drugMapper.selectCount(anyString()))
                .thenReturn(0L);

        PageResult<Drug> result = drugService.list("不存在", 1, 10);

        assertEquals(0L, result.getTotal());
        assertTrue(result.getRows().isEmpty());
    }
}
