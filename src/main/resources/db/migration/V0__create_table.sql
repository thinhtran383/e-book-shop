-- Tạo bảng Categories (Danh mục sách)
CREATE TABLE Categories (
                            CategoryID INT AUTO_INCREMENT PRIMARY KEY,
                            CategoryName VARCHAR(255) NOT NULL
);

-- Tạo bảng Books (Sách)
CREATE TABLE Books (
                       BookID INT AUTO_INCREMENT PRIMARY KEY,
                       Title VARCHAR(255) NOT NULL,
                       Author VARCHAR(255),
                       Price DECIMAL(10, 2) NOT NULL,
                       Quantity INT NOT NULL,
                       CategoryID INT,
                       Description TEXT,
                       Publisher VARCHAR(255),
                       PublishedDate DATE,
                       Image VARCHAR(255),
                       AverageRating DECIMAL(2, 1),
                       CONSTRAINT FK_Book_Category FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID) ON DELETE SET NULL
);

-- Tạo bảng Customers (Khách hàng)
CREATE TABLE Customers (
                           CustomerID INT AUTO_INCREMENT PRIMARY KEY,
                           Username VARCHAR(255) NOT NULL,
                           Password VARCHAR(255) NOT NULL,
                           Email VARCHAR(255),
                           FullName VARCHAR(255),
                           Phone VARCHAR(50),
                           Address VARCHAR(255),
                           CreatedDate DATE
);

-- Tạo bảng ShoppingCart (Giỏ hàng)
CREATE TABLE ShoppingCart (
                              CartID INT AUTO_INCREMENT PRIMARY KEY,
                              CustomerID INT NOT NULL,
                              BookID INT NOT NULL,
                              Quantity INT NOT NULL,
                              CONSTRAINT FK_Cart_Customer FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE,
                              CONSTRAINT FK_Cart_Book FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE CASCADE
);

-- Tạo bảng Orders (Đơn hàng)
CREATE TABLE Orders (
                        OrderID INT AUTO_INCREMENT PRIMARY KEY,
                        CustomerID INT NOT NULL,
                        OrderDate DATE NOT NULL,
                        TotalAmount DECIMAL(10, 2) NOT NULL,
                        Status VARCHAR(50) NOT NULL,
                        CONSTRAINT FK_Order_Customer FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE
);

-- Tạo bảng OrderDetails (Chi tiết đơn hàng)
CREATE TABLE OrderDetails (
                              OrderDetailID INT AUTO_INCREMENT PRIMARY KEY,
                              OrderID INT NOT NULL,
                              BookID INT NOT NULL,
                              Quantity INT NOT NULL,
                              Price DECIMAL(10, 2) NOT NULL,
                              CONSTRAINT FK_OrderDetail_Order FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE,
                              CONSTRAINT FK_OrderDetail_Book FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE CASCADE
);

-- Tạo bảng Comments (Bình luận sách)
CREATE TABLE Comments (
                          CommentID INT AUTO_INCREMENT PRIMARY KEY,
                          CustomerID INT NOT NULL,
                          BookID INT NOT NULL,
                          Content TEXT NOT NULL,
                          CommentDate DATETIME NOT NULL,
                          CONSTRAINT FK_Comment_Customer FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE,
                          CONSTRAINT FK_Comment_Book FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE CASCADE
);

-- Tạo bảng Ratings (Đánh giá sao)
CREATE TABLE Ratings (
                         RatingID INT AUTO_INCREMENT PRIMARY KEY,
                         CustomerID INT NOT NULL,
                         BookID INT NOT NULL,
                         Rating INT NOT NULL CHECK (Rating >= 1 AND Rating <= 5),
                         RatingDate DATETIME NOT NULL,
                         CONSTRAINT FK_Rating_Customer FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID) ON DELETE CASCADE,
                         CONSTRAINT FK_Rating_Book FOREIGN KEY (BookID) REFERENCES Books(BookID) ON DELETE CASCADE,
                         CONSTRAINT UQ_Rating UNIQUE (CustomerID, BookID) -- Mỗi khách hàng chỉ có thể đánh giá một lần cho mỗi sách
);
