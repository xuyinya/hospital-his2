-- 东软云医院系统数据库脚本
-- 数据库: hospital_his

CREATE DATABASE IF NOT EXISTS hospital_his DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE hospital_his;

-- 患者表
DROP TABLE IF EXISTS patient;
CREATE TABLE patient (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_name VARCHAR(50) NOT NULL,
    gender TINYINT DEFAULT 0 COMMENT '性别 0女 1男',
    age INT,
    id_card VARCHAR(18),
    phone VARCHAR(11),
    address VARCHAR(200),
    password VARCHAR(255) DEFAULT NULL COMMENT '登录密码（BCrypt加密）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '患者表';

-- 科室表
DROP TABLE IF EXISTS department;
CREATE TABLE department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(50) NOT NULL,
    dept_code VARCHAR(20) UNIQUE,
    location VARCHAR(100),
    status TINYINT DEFAULT 1 COMMENT '状态 0停用 1启用'
) COMMENT '科室表';

-- 医生表
DROP TABLE IF EXISTS doctor;
CREATE TABLE doctor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    doctor_name VARCHAR(50) NOT NULL,
    dept_id BIGINT NOT NULL,
    title VARCHAR(20),
    specialty VARCHAR(100),
    status TINYINT DEFAULT 1,
    FOREIGN KEY (dept_id) REFERENCES department(id)
) COMMENT '医生表';

-- 挂号表
DROP TABLE IF EXISTS registration;
CREATE TABLE registration (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    dept_id BIGINT NOT NULL,
    reg_type VARCHAR(10) DEFAULT '普通' COMMENT '挂号类型 普通/专家',
    reg_fee DECIMAL(10,2),
    reg_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status TINYINT DEFAULT 0 COMMENT '状态 0待诊 1已诊 2取消',
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    FOREIGN KEY (dept_id) REFERENCES department(id)
) COMMENT '挂号表';

-- 药品表
DROP TABLE IF EXISTS drug;
CREATE TABLE drug (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    drug_name VARCHAR(100) NOT NULL,
    drug_code VARCHAR(20) UNIQUE,
    specification VARCHAR(100),
    unit VARCHAR(10),
    manufacturer VARCHAR(100),
    unit_price DECIMAL(10,2),
    stock INT DEFAULT 0,
    status TINYINT DEFAULT 1
) COMMENT '药品表';

-- 病历表
DROP TABLE IF EXISTS medical_record;
CREATE TABLE medical_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    registration_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    chief_complaint VARCHAR(500) COMMENT '主诉',
    present_illness VARCHAR(1000) COMMENT '现病史',
    diagnosis VARCHAR(500) COMMENT '诊断',
    treatment_plan VARCHAR(500) COMMENT '处理方案',
    record_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (registration_id) REFERENCES registration(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
) COMMENT '病历表';

-- 处方表
DROP TABLE IF EXISTS prescription;
CREATE TABLE prescription (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    registration_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) DEFAULT 0,
    status TINYINT DEFAULT 0 COMMENT '状态 0未取药 1已取药',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (registration_id) REFERENCES registration(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
) COMMENT '处方表';

-- 处方明细表
DROP TABLE IF EXISTS prescription_detail;
CREATE TABLE prescription_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    prescription_id BIGINT NOT NULL,
    drug_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2),
    amount DECIMAL(10,2),
    usage_method VARCHAR(100) COMMENT '用法用量',
    FOREIGN KEY (prescription_id) REFERENCES prescription(id),
    FOREIGN KEY (drug_id) REFERENCES drug(id)
) COMMENT '处方明细表';

-- 检查表
DROP TABLE IF EXISTS examination;
CREATE TABLE examination (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    registration_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    exam_type VARCHAR(50),
    exam_name VARCHAR(100),
    result VARCHAR(500),
    fee DECIMAL(10,2),
    status TINYINT DEFAULT 0 COMMENT '状态 0未完成 1已完成',
    exam_time DATETIME,
    FOREIGN KEY (registration_id) REFERENCES registration(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
) COMMENT '检查表';

-- 检验表
DROP TABLE IF EXISTS laboratory;
CREATE TABLE laboratory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    registration_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    lab_type VARCHAR(50),
    lab_name VARCHAR(100),
    result VARCHAR(500),
    reference_range VARCHAR(100) COMMENT '参考范围',
    fee DECIMAL(10,2),
    status TINYINT DEFAULT 0,
    lab_time DATETIME,
    FOREIGN KEY (registration_id) REFERENCES registration(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
) COMMENT '检验表';

-- 处置表
DROP TABLE IF EXISTS treatment;
CREATE TABLE treatment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    registration_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    treatment_name VARCHAR(100),
    treatment_desc VARCHAR(500),
    fee DECIMAL(10,2),
    status TINYINT DEFAULT 0,
    treatment_time DATETIME,
    FOREIGN KEY (registration_id) REFERENCES registration(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
) COMMENT '处置表';

-- 收费表
DROP TABLE IF EXISTS payment;
CREATE TABLE payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    registration_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    payment_type VARCHAR(20) COMMENT '收费类型 药品/检查/检验/处置',
    total_amount DECIMAL(10,2),
    payment_method VARCHAR(20) DEFAULT '现金',
    payment_time DATETIME,
    status TINYINT DEFAULT 0 COMMENT '状态 0未支付 1已支付',
    FOREIGN KEY (registration_id) REFERENCES registration(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id)
) COMMENT '收费表';

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50) COMMENT '真实姓名',
    role VARCHAR(20) DEFAULT 'admin' COMMENT '角色 admin/doctor',
    doctor_id BIGINT DEFAULT NULL COMMENT '关联医生ID（仅doctor角色）',
    status TINYINT DEFAULT 1 COMMENT '状态 0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '用户表';

-- ==================== 初始化数据 ====================

-- 科室数据
INSERT INTO department (dept_name, dept_code, location) VALUES
('内科', 'NK', '门诊楼2层'),
('外科', 'WK', '门诊楼3层'),
('儿科', 'EK', '门诊楼1层'),
('妇产科', 'FCK', '门诊楼4层'),
('骨科', 'GK', '门诊楼3层'),
('眼科', 'YK', '门诊楼2层'),
('口腔科', 'KQK', '门诊楼1层'),
('皮肤科', 'PFK', '门诊楼2层');

-- 医生数据
INSERT INTO doctor (doctor_name, dept_id, title, specialty) VALUES
('张明', 1, '主任医师', '心血管内科'),
('李华', 1, '副主任医师', '呼吸内科'),
('王强', 2, '主任医师', '普外科'),
('刘芳', 2, '副主任医师', '骨外科'),
('陈静', 3, '主任医师', '小儿内科'),
('赵伟', 4, '副主任医师', '妇产科'),
('孙丽', 5, '主任医师', '骨科'),
('周敏', 6, '主治医师', '眼科');

-- 药品数据
INSERT INTO drug (drug_name, drug_code, specification, unit, manufacturer, unit_price, stock) VALUES
('阿莫西林胶囊', 'AMX001', '250mg*24粒', '盒', '华北制药', 15.50, 1000),
('布洛芬缓释胶囊', 'BLF001', '300mg*20粒', '盒', '中美史克', 22.00, 800),
('头孢克肟颗粒', 'TBX001', '50mg*6袋', '盒', '广州白云山', 28.50, 500),
('阿奇霉素片', 'AQM001', '250mg*6片', '盒', '辉瑞制药', 35.00, 600),
('蒙脱石散', 'MTS001', '3g*10袋', '盒', '博福-益普生', 25.80, 700),
('复方甘草片', 'FGC001', '100片', '瓶', '上海医药', 8.50, 2000),
('氯雷他定片', 'LLT001', '10mg*6片', '盒', '拜耳', 18.00, 400),
('奥美拉唑肠溶胶囊', 'AML001', '20mg*14粒', '盒', '阿斯利康', 42.00, 350);

-- 患者数据（密码由系统启动时自动初始化为 123456）
INSERT INTO patient (patient_name, gender, age, id_card, phone, address) VALUES
('张三', 1, 35, '110101199001011234', '13800138001', '北京市朝阳区建国路1号'),
('李四', 0, 28, '110101199601012345', '13800138002', '北京市海淀区中关村大街2号'),
('王五', 1, 45, '110101198001013456', '13800138003', '北京市西城区西单大街3号'),
('赵六', 0, 52, '110101197301014567', '13800138004', '北京市东城区东直门4号'),
('钱七', 1, 8, '110101201801015678', '13800138005', '北京市丰台区南三环5号');

-- 挂号数据
INSERT INTO registration (patient_id, doctor_id, dept_id, reg_type, reg_fee, status) VALUES
(1, 1, 1, '专家', 50.00, 1),
(2, 2, 1, '普通', 25.00, 0),
(3, 3, 2, '专家', 50.00, 1),
(4, 5, 3, '普通', 25.00, 0),
(5, 7, 5, '专家', 50.00, 0),
(4, 1, 1, '普通', 25.00, 0),
(1, 3, 2, '专家', 50.00, 0),
(2, 1, 1, '普通', 25.00, 0);

-- 病历数据
INSERT INTO medical_record (registration_id, patient_id, doctor_id, chief_complaint, present_illness, diagnosis, treatment_plan) VALUES
(1, 1, 1, '胸闷气短3天', '患者3天前无明显诱因出现胸闷气短，活动后加重', '冠心病', '口服阿司匹林、阿托伐他汀，低盐低脂饮食'),
(3, 3, 3, '右下腹疼痛1天', '患者1天前出现右下腹疼痛，伴恶心', '急性阑尾炎', '建议手术治疗，完善术前检查'),
(7, 1, 3, '胸闷原因待查，申请外科会诊', '患者冠心病病史，近期出现胸痛放射至左肩，疑有外科情况', '心绞痛待查', '建议冠脉CTA检查，心内科随诊');

-- 处方数据
INSERT INTO prescription (registration_id, patient_id, doctor_id, total_amount, status) VALUES
(1, 1, 1, 57.50, 0);

-- 处方明细
INSERT INTO prescription_detail (prescription_id, drug_id, quantity, unit_price, amount, usage_method) VALUES
(1, 1, 2, 15.50, 31.00, '每次2粒，每日3次，饭后服用'),
(1, 2, 1, 22.00, 22.00, '每次1粒，每日2次，饭后服用'),
(1, 3, 1, 28.50, 28.50, '每次1袋，每日3次');

-- 检查数据
INSERT INTO examination (registration_id, patient_id, exam_type, exam_name, result, fee, status, exam_time) VALUES
(1, 1, '心电图', '常规心电图', '窦性心律，ST段轻度压低', 120.00, 1, '2026-06-22 09:30:00'),
(1, 1, '超声', '心脏彩超', '左心室射血分数正常，未见明显异常', 280.00, 1, '2026-06-22 10:00:00'),
(3, 3, '超声', '腹部B超', '阑尾区可见低回声，直径约8mm，考虑急性阑尾炎', 200.00, 1, '2026-06-22 14:00:00');

-- 检验数据
INSERT INTO laboratory (registration_id, patient_id, lab_type, lab_name, result, reference_range, fee, status, lab_time) VALUES
(1, 1, '血液检查', '血常规', '白细胞9.5×10^9/L', '4.0-10.0×10^9/L', 40.00, 1, '2026-06-22 09:15:00'),
(1, 1, '血液检查', '心肌酶谱', 'CK-MB 18U/L', '0-25U/L', 80.00, 1, '2026-06-22 09:20:00'),
(3, 3, '血液检查', '血常规', '白细胞13.2×10^9/L ↑', '4.0-10.0×10^9/L', 40.00, 1, '2026-06-22 13:30:00'),
(3, 3, '血液检查', 'C反应蛋白', '28.5mg/L ↑', '0-10mg/L', 60.00, 1, '2026-06-22 13:35:00');

-- 处置数据
INSERT INTO treatment (registration_id, patient_id, treatment_name, treatment_desc, fee, status, treatment_time) VALUES
(3, 3, '伤口换药', '术后切口换药，碘伏消毒，无菌敷料覆盖', 50.00, 1, '2026-06-23 08:00:00'),
(3, 3, '拆线', '术后7天拆线', 30.00, 0, NULL);

-- 收费数据
INSERT INTO payment (registration_id, patient_id, payment_type, total_amount, payment_method, payment_time, status) VALUES
(1, 1, '挂号费', 50.00, '微信', '2026-06-22 08:00:00', 1),
(1, 1, '检查费', 400.00, '微信', '2026-06-22 09:35:00', 1),
(1, 1, '检验费', 120.00, '微信', '2026-06-22 09:25:00', 1),
(1, 1, '药费', 81.50, '微信', '2026-06-22 11:00:00', 1),
(3, 3, '挂号费', 50.00, '现金', '2026-06-22 08:30:00', 1),
(3, 3, '检查费', 200.00, '现金', '2026-06-22 14:10:00', 1),
(3, 3, '检验费', 100.00, '现金', '2026-06-22 13:40:00', 1);
