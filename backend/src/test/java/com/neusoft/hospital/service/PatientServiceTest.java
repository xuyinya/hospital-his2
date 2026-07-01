package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Patient;
import com.neusoft.hospital.mapper.PatientMapper;
import com.neusoft.hospital.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * PatientService 单元测试（Mock Mapper 层）
 * <p>
 * 使用 Mockito 模拟 PatientMapper，不依赖真实数据库。
 * 验证 Service 层的业务逻辑是否正确调用了 Mapper 方法并处理了返回值。
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

    private Patient samplePatient;

    @BeforeEach
    void setUp() {
        samplePatient = new Patient();
        samplePatient.setId(1L);
        samplePatient.setPatientName("张三");
        samplePatient.setGender(1);
        samplePatient.setAge(30);
        samplePatient.setIdCard("110101199001011234");
        samplePatient.setPhone("13800138000");
    }

    @Test
    @DisplayName("新增患者 → 验证 Mapper.insert 被调用且参数透传")
    void testAddPatient() {
        patientService.add(samplePatient);

        verify(patientMapper, times(1)).insert(patientCaptor.capture());
        Patient captured = patientCaptor.getValue();
        assertEquals("张三", captured.getPatientName());
        assertEquals("110101199001011234", captured.getIdCard());
    }

    @Test
    @DisplayName("查询患者详情 → 返回 Mock 数据")
    void testGetById() {
        when(patientMapper.selectById(1L)).thenReturn(samplePatient);

        Patient result = patientService.getById(1L);

        assertNotNull(result);
        assertEquals("张三", result.getPatientName());
        assertEquals(30, result.getAge());
        verify(patientMapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("查询不存在的患者 → 返回 null")
    void testGetByIdNotFound() {
        when(patientMapper.selectById(999L)).thenReturn(null);

        Patient result = patientService.getById(999L);

        assertNull(result);
    }

    @Test
    @DisplayName("根据身份证号查询患者")
    void testGetByIdCard() {
        when(patientMapper.selectByIdCard("110101199001011234")).thenReturn(samplePatient);

        Patient result = patientService.getByIdCard("110101199001011234");

        assertNotNull(result);
        assertEquals("张三", result.getPatientName());
        verify(patientMapper, times(1)).selectByIdCard("110101199001011234");
    }

    @Test
    @DisplayName("删除患者 → 验证 Mapper.deleteById 被调用")
    void testDeletePatient() {
        patientService.delete(1L);

        verify(patientMapper, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("更新患者 → 验证 Mapper.update 被调用且参数透传")
    void testUpdatePatient() {
        patientService.update(samplePatient);

        verify(patientMapper, times(1)).update(patientCaptor.capture());
        assertEquals(1L, patientCaptor.getValue().getId());
        assertEquals("张三", patientCaptor.getValue().getPatientName());
    }

    @Test
    @DisplayName("分页查询 → 返回正确的 PageResult")
    void testListPatients() {
        // 模拟 Mapper 返回 2 条记录 + 总数 5
        Patient p2 = new Patient();
        p2.setId(2L);
        p2.setPatientName("李四");
        List<Patient> mockRows = List.of(samplePatient, p2);

        when(patientMapper.selectList(eq("张"), isNull(), eq(0), eq(10)))
                .thenReturn(mockRows);
        when(patientMapper.selectCount(eq("张"), isNull()))
                .thenReturn(5L);

        PageResult<Patient> result = patientService.list("张", null, 1, 10);

        assertAll("分页结果应正确",
                () -> assertEquals(5L, result.getTotal()),
                () -> assertEquals(2, result.getRows().size()),
                () -> assertEquals("张三", result.getRows().get(0).getPatientName()),
                () -> assertEquals("李四", result.getRows().get(1).getPatientName())
        );

        // 验证 Mapper 被调用的分页参数正确
        verify(patientMapper).selectList("张", null, 0, 10);
        verify(patientMapper).selectCount("张", null);
    }

    @Test
    @DisplayName("空分页 → 返回 total=0, rows=[]")
    void testListEmpty() {
        when(patientMapper.selectList(anyString(), isNull(), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(patientMapper.selectCount(anyString(), isNull()))
                .thenReturn(0L);

        PageResult<Patient> result = patientService.list("不存在", null, 1, 10);

        assertEquals(0L, result.getTotal());
        assertTrue(result.getRows().isEmpty());
    }
}
