package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.mapper.RegistrationMapper;
import com.neusoft.hospital.service.impl.RegistrationServiceImpl;
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
 * RegistrationService 单元测试（挂号 CRUD + 状态流转）
 * <p>
 * 模拟 RegistrationMapper 层，验证挂号新增、修改、状态更新和分页查询的业务逻辑。
 * 挂号状态：0=待诊，1=已诊，2=已取消。
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationMapper registrationMapper;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Captor
    private ArgumentCaptor<Registration> regCaptor;

    private Registration sampleReg;

    @BeforeEach
    void setUp() {
        sampleReg = new Registration();
        sampleReg.setId(1L);
        sampleReg.setPatientId(1L);
        sampleReg.setDoctorId(2L);
        sampleReg.setDeptId(1L);
        sampleReg.setRegType("普通");
        sampleReg.setRegFee(new BigDecimal("25.00"));
        sampleReg.setStatus(0);
        sampleReg.setPatientName("张三");
        sampleReg.setDoctorName("张明");
        sampleReg.setDeptName("内科");
        sampleReg.setChiefComplaint("头痛3天");
        sampleReg.setPresentIllness("3天前开始头痛，伴有发热");
    }

    @Test
    @DisplayName("新增挂号 → 验证 Mapper.insert 被调用且参数透传")
    void testAddRegistration() {
        registrationService.add(sampleReg);

        verify(registrationMapper, times(1)).insert(regCaptor.capture());
        Registration captured = regCaptor.getValue();

        assertAll("挂号参数正确",
                () -> assertEquals(1L, captured.getPatientId()),
                () -> assertEquals(2L, captured.getDoctorId()),
                () -> assertEquals(1L, captured.getDeptId()),
                () -> assertEquals("普通", captured.getRegType()),
                () -> assertEquals(new BigDecimal("25.00"), captured.getRegFee()),
                () -> assertEquals("头痛3天", captured.getChiefComplaint())
        );
    }

    @Test
    @DisplayName("修改挂号 → 验证 Mapper.update 被调用")
    void testUpdateRegistration() {
        sampleReg.setRegType("专家");
        sampleReg.setRegFee(new BigDecimal("50.00"));

        registrationService.update(sampleReg);

        verify(registrationMapper, times(1)).update(regCaptor.capture());
        Registration captured = regCaptor.getValue();
        assertEquals("专家", captured.getRegType());
        assertEquals(new BigDecimal("50.00"), captured.getRegFee());
    }

    @Test
    @DisplayName("查询挂号详情 → 返回 Mock 数据")
    void testGetById() {
        when(registrationMapper.selectById(1L)).thenReturn(sampleReg);

        Registration result = registrationService.getById(1L);

        assertAll("详情字段正确",
                () -> assertNotNull(result),
                () -> assertEquals("张三", result.getPatientName()),
                () -> assertEquals("张明", result.getDoctorName()),
                () -> assertEquals("内科", result.getDeptName()),
                () -> assertEquals(0, result.getStatus())
        );
        verify(registrationMapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("查询不存在的挂号 → 返回 null")
    void testGetByIdNotFound() {
        when(registrationMapper.selectById(999L)).thenReturn(null);

        Registration result = registrationService.getById(999L);

        assertNull(result);
    }

    @Test
    @DisplayName("状态流转：待诊(0) → 已诊(1)")
    void testUpdateStatusToDone() {
        registrationService.updateStatus(1L, 1);

        verify(registrationMapper, times(1)).updateStatus(1L, 1);
    }

    @Test
    @DisplayName("状态流转：待诊(0) → 已取消(2)")
    void testUpdateStatusToCancelled() {
        registrationService.updateStatus(1L, 2);

        verify(registrationMapper, times(1)).updateStatus(1L, 2);
    }

    @Test
    @DisplayName("分页查询全部挂号 → 返回 PageResult")
    void testListAll() {
        Registration reg2 = new Registration();
        reg2.setId(2L);
        reg2.setPatientName("李四");
        reg2.setDoctorName("王强");
        reg2.setDeptName("外科");

        when(registrationMapper.selectList(isNull(), isNull(), isNull(), isNull(), isNull(), eq(0), eq(10)))
                .thenReturn(List.of(sampleReg, reg2));
        when(registrationMapper.selectCount(isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(2L);

        PageResult<Registration> result = registrationService.list(null, null, null, null, null, 1, 10);

        assertAll("全部挂号分页",
                () -> assertEquals(2L, result.getTotal()),
                () -> assertEquals(2, result.getRows().size()),
                () -> assertEquals("张三", result.getRows().get(0).getPatientName()),
                () -> assertEquals("李四", result.getRows().get(1).getPatientName())
        );
    }

    @Test
    @DisplayName("按医生+状态过滤分页查询")
    void testListWithFilters() {
        when(registrationMapper.selectList(eq(null), eq(2L), eq(0), eq(null), eq(null), eq(0), eq(10)))
                .thenReturn(List.of(sampleReg));
        when(registrationMapper.selectCount(eq(null), eq(2L), eq(0), eq(null), eq(null)))
                .thenReturn(1L);

        // 查询医生ID=2，状态=待诊的挂号
        PageResult<Registration> result = registrationService.list(null, 2L, 0, null, null, 1, 10);

        assertAll("按医生+状态过滤",
                () -> assertEquals(1L, result.getTotal()),
                () -> assertEquals(1, result.getRows().size()),
                () -> assertEquals("张三", result.getRows().get(0).getPatientName())
        );
        verify(registrationMapper).selectList(null, 2L, 0, null, null, 0, 10);
    }

    @Test
    @DisplayName("按患者姓名模糊查询分页")
    void testListByPatientName() {
        when(registrationMapper.selectList(isNull(), isNull(), isNull(), eq("张"), isNull(), eq(0), eq(10)))
                .thenReturn(List.of(sampleReg));
        when(registrationMapper.selectCount(isNull(), isNull(), isNull(), eq("张"), isNull()))
                .thenReturn(1L);

        PageResult<Registration> result = registrationService.list(null, null, null, "张", null, 1, 10);

        assertEquals(1L, result.getTotal());
        verify(registrationMapper).selectList(null, null, null, "张", null, 0, 10);
    }

    @Test
    @DisplayName("空查询结果 → total=0, rows=[]")
    void testListEmpty() {
        when(registrationMapper.selectList(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(registrationMapper.selectCount(any(), any(), any(), any(), any()))
                .thenReturn(0L);

        PageResult<Registration> result = registrationService.list(null, null, 2, "不存在", null, 1, 10);

        assertEquals(0L, result.getTotal());
        assertTrue(result.getRows().isEmpty());
    }
}
