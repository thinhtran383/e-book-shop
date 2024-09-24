-- Chèn dữ liệu vào bảng Roles
INSERT INTO Roles (RoleName)
VALUES ('customer'),
       ('admin');

-- Chèn dữ liệu vào bảng Categories
INSERT INTO Categories (CategoryName)
VALUES ('Fiction'),
       ('Science'),
       ('Biography'),
       ('Children'),
       ('Mystery');

-- Chèn dữ liệu vào bảng Users
INSERT INTO Users (Username, Password, Email, RoleID)
VALUES ('customer1', '1', 'customer1@example.com', 1), -- customer
       ('customer2', '1', 'customer2@example.com', 1), -- customer
       ('customer3', '1', 'customer3@example.com', 1), -- customer
       ('admin1', '1', 'admin1@example.com', 1),       -- admin
       ('admin2', '1', 'admin2@example.com', 1);
-- admin

update Users
set Password = '$2a$10$3Q4/.y3.xJBSBO6cYpB65enFTkq9Jp7NNx3dVW4ouKL38y1T9DJVG';


-- Chèn dữ liệu vào bảng Customers
-- Giả sử các UserID đã tồn tại trong bảng Users (ví dụ: UserID 1-5 là khách hàng)
INSERT INTO Customers (UserID, FullName, Phone, Address, CreatedDate)
VALUES (1, 'John Doe', '1234567890', '123 Elm Street', '2024-01-01'),
       (2, 'Jane Smith', '2345678901', '456 Oak Avenue', '2024-02-01'),
       (3, 'Alice Johnson', '3456789012', '789 Pine Road', '2024-03-01'),
       (4, 'Bob Brown', '4567890123', '101 Maple Boulevard', '2024-04-01'),
       (5, 'Charlie Davis', '5678901234', '202 Birch Lane', '2024-05-01');

-- Chèn dữ liệu vào bảng Books
INSERT INTO Books (Title, Author, Price, Quantity, CategoryID, Description, Publisher, PublishedDate, Image,
                   AverageRating)
VALUES ('The Great Adventure', 'John Writer', 15.99, 50, 1, 'A thrilling adventure novel.', 'Fiction Press',
        '2023-05-01', 'image1.jpg', 4.2),
       ('Science for Everyone', 'Jane Scientist', 22.50, 30, 2, 'A comprehensive guide to science.', 'Science Pub',
        '2022-08-15', 'image2.jpg', 4.5),
       ('Life of a Genius', 'Albert Author', 18.75, 20, 3, 'Biography of a famous scientist.', 'Biography House',
        '2021-11-10', 'image3.jpg', 4.8),
       ('Learning is Fun', 'Chris Educator', 10.00, 40, 4, 'Educational book for children.', 'Kids Press', '2020-03-20',
        'image4.jpg', 4.0),
       ('Mystery in the Night', 'Detective Story', 12.99, 25, 5, 'A gripping mystery novel.', 'Mystery Books',
        '2023-01-05', 'image5.jpg', 3.9);

-- Chèn dữ liệu vào bảng ShoppingCart
-- Giả sử mỗi người dùng có một vài sách trong giỏ hàng
INSERT INTO ShoppingCart (UserID, BookID, Quantity)
VALUES (1, 1, 2), -- John Doe có 2 quyển sách "The Great Adventure"
       (1, 3, 1), -- John Doe có 1 quyển sách "Life of a Genius"
       (2, 2, 1), -- Jane Smith có 1 quyển sách "Science for Everyone"
       (3, 5, 3), -- Alice Johnson có 3 quyển sách "Mystery in the Night"
       (4, 4, 1);
-- Bob Brown có 1 quyển sách "Learning is Fun"

-- Chèn dữ liệu vào bảng Orders
INSERT INTO Orders (UserID, OrderDate, TotalAmount, Status)
VALUES (1, '2024-06-01', 31.98, 'Completed'),
       (2, '2024-06-15', 22.50, 'Pending'),
       (3, '2024-07-10', 38.97, 'Shipped'),
       (4, '2024-08-05', 10.00, 'Delivered'),
       (5, '2024-08-20', 12.99, 'Canceled');

-- Chèn dữ liệu vào bảng OrderDetails
INSERT INTO OrderDetails (OrderID, BookID, Quantity, Price)
VALUES (1, 1, 2, 15.99), -- John Doe mua 2 quyển sách "The Great Adventure"
       (2, 2, 1, 22.50), -- Jane Smith mua 1 quyển sách "Science for Everyone"
       (3, 5, 3, 12.99), -- Alice Johnson mua 3 quyển sách "Mystery in the Night"
       (4, 4, 1, 10.00), -- Bob Brown mua 1 quyển sách "Learning is Fun"
       (5, 3, 1, 18.75);
-- Charlie Davis mua 1 quyển sách "Life of a Genius"

-- Chèn dữ liệu vào bảng Comments
INSERT INTO Comments (UserID, BookID, Content, CommentDate)
VALUES (1, 1, 'Amazing book, loved it!', '2024-06-02 14:35:00'),
       (2, 2, 'Very informative and well written.', '2024-06-16 09:20:00'),
       (3, 3, 'A must-read biography.', '2024-07-11 18:45:00'),
       (4, 4, 'Great for kids, my son loved it.', '2024-08-06 11:30:00'),
       (5, 5, 'Could have been better, not very suspenseful.', '2024-08-21 22:10:00');

-- Chèn dữ liệu vào bảng Ratings
INSERT INTO Ratings (UserID, BookID, Rating, RatingDate)
VALUES (1, 1, 5, '2024-06-02 15:00:00'),
       (2, 2, 4, '2024-06-16 10:00:00'),
       (3, 3, 5, '2024-07-11 19:00:00'),
       (4, 4, 4, '2024-08-06 12:00:00'),
       (5, 5, 3, '2024-08-21 22:30:00');