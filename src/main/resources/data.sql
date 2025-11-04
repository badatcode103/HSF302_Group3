-- Xóa dữ liệu cũ theo đúng thứ tự (vì có khóa ngoại)
DELETE FROM students;
DELETE FROM departments;
DELETE FROM user_accounts;

-- Reset ID tự tăng (IDENTITY) về 0
DBCC CHECKIDENT ('students', RESEED, 0);
DBCC CHECKIDENT ('departments', RESEED, 0);
DBCC CHECKIDENT ('user_accounts', RESEED, 0);

-- Chèn dữ liệu mẫu cho user_accounts
INSERT INTO user_accounts (username, password, role)
VALUES
    ('manager1', '123', 1),
    ('manager2', '123', 1),
    ('staff1', '123', 2),
    ('staff2', '123', 2),
    ('guest1', '123', 3);

-- Chèn dữ liệu mẫu cho departments
INSERT INTO departments (departmentname)
VALUES
    (N'Công nghệ thông tin'),
    (N'Quản trị kinh doanh'),
    (N'Ngôn ngữ Anh');

-- Chèn dữ liệu mẫu cho students
-- (ĐÃ SỬA: Xóa DECLARE @today và dùng GETDATE() trực tiếp)
INSERT INTO students (studentid, name, gpa, department_id, created_at, updated_at, created_by)
VALUES
    (N'ST001', N'Nguyễn Văn An', 9.8, 1, GETDATE(), GETDATE(), 'staff1'),
    (N'ST002', N'Trần Thị Bình', 9.2, 1, GETDATE(), GETDATE(), 'staff1'),
    (N'ST003', N'Lê Văn Cường', 7.0, 2, GETDATE(), GETDATE(), 'staff1'),
    (N'ST004', N'Phạm Thị Dung', 9.0, 1, GETDATE(), GETDATE(), 'staff2'),
    (N'ST005', N'Hoàng Văn Em', 6.5, 3, GETDATE(), GETDATE(), 'staff2'),
    (N'ST006', N'Đỗ Thị Gấm', 8.5, 2, GETDATE(), GETDATE(), 'staff2');