-- V1: Thêm bảng Roles và cập nhật bảng Users
-- Tạo bảng Roles (Vai trò người dùng)
CREATE TABLE Roles (
                       RoleID INT AUTO_INCREMENT PRIMARY KEY,
                       RoleName VARCHAR(255) NOT NULL UNIQUE
);

-- Cập nhật bảng Users (Thêm RoleID)
ALTER TABLE Users
    ADD RoleID INT NOT NULL,
ADD CONSTRAINT FK_User_Role FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) ON DELETE CASCADE;


