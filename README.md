# PHARMACY create prescription module 
Phần mềm quản lý nhà thuốc - module tạo đơn thuốc. Sử dụng ngôn ngữ java (java swing) kết hợp MongoDB
Đây là ứng dụng Java Swing quản lý hiệu thuốc, được phát triển cho mục đích học tập và mô phỏng quy trình tạo đơn thuốc tại quầy.

## 💊 Tính năng chính

- Tạo đơn thuốc với các bước chọn khách hàng, chọn thuốc, chọn chương trình khuyến mại
- Tạo khách hàng mới nếu chưa có
- chương trình khuyến mại dễ dàng thêm mới, chỉnh sửa với nhiều loại logic được tự động áp dụng
- Kết nối MongoDB để lưu trữ thông tin đơn thuốc, hỗ trợ tạo bản ghi khách hàng mới

## 🖼️ Giao diện minh họa

### 1. Giao diện tạo đơn thuốc
![Image](https://github.com/user-attachments/assets/fdd7ffc4-d02f-427a-a0a2-069c2efc7034)
![Image](https://github.com/user-attachments/assets/4b44d55b-9115-4bab-a47d-b83e8c6f61ab)

### 2. Giao diện chọn khách hàng & thêm khách hàng
![Image](https://github.com/user-attachments/assets/be8b66c4-c896-4ba1-b072-b53a2da6ae76)
![Image](https://github.com/user-attachments/assets/bfc57743-6d2a-49b6-a97e-c896176228fc)

### 3. Giao diện chọn thuốc
![Image](https://github.com/user-attachments/assets/b604a9ae-b632-4ba0-b858-a0d26b591c7f)

### 4. Giao diện chọn chương trình khuyến mại
![Image](https://github.com/user-attachments/assets/fb5c23e8-321e-40e3-b37c-b822c5dd54f1)

### 5. Giao diện sau khi lựa chọn đủ các thành phần (có thể viết thêm ghi chú nếu cần)
![Image](https://github.com/user-attachments/assets/f6c72aa0-97d7-4b3b-ae1f-9895323e6848)

### 6. Thanh toán và in hóa đơn
![Image](https://github.com/user-attachments/assets/89be274c-4f5e-4f5f-9c77-24be8f582813)
![Image](https://github.com/user-attachments/assets/f52062f9-7cad-49ed-b8c6-68c376b8df86)

## ⚙️ Công nghệ sử dụng

- Java Swing (với Ant - Java Application)
- MongoDB (sử dụng thư viện `mongodb-driver-sync`)
- MongoDB Compass để quản lý dữ liệu trực quan

## 🛠️ Hướng dẫn chạy ứng dụng

1. Mở project trong NetBeans
2. Thêm 3 file `.jar` sau vào `Libraries` (hoặc trong src đã có sẵn):
   - `mongodb-driver-sync-<version>.jar`
   - `mongodb-driver-core-<version>.jar`
   - `bson-<version>.jar`
3. Cài đặt mongoDB, load dữ liệu mongoDB đầy đủ (tải tại - `data`)
4. Đảm bảo MongoDB đang chạy tại `localhost:27017`
5. Clean & Build → Run Project

## 📌 Ghi chú

- Dữ liệu chỉ được lưu cục bộ trên MongoDB, không đồng bộ từ xa.
- Chỉ triển khai chức năng tạo đơn thuốc để phục vụ mục tiêu học tập.
---
