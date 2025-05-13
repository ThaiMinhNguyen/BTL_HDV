# ĐỀ TÀI: HỆ THỐNG ĐẶT HÀNG TRỰC TUYẾN

## I. Giới thiệu thành viên:
- Nguyễn Thị Lan - B21DCCN818
- Nguyễn Đình Hậu - B21DCCN333

## II. Giới thiệu đề tài:
> Hệ thống đặt hàng trực tuyến cho phép người dùng xem sản phẩm, thêm sản phẩm vào giỏ hàng, đặt hàng, và nhận email xác nhận. Hệ thống xác minh thông tin sản phẩm, kiểm tra tồn kho, thực hiện thao tác đặt hàng, cập nhật trạng thái sản phẩm, và gửi thông báo xác nhận cho người dùng.

- **Usecase**:
  - Xem sản phẩm
  - Thêm sản phẩm vào giỏ hàng
  - Đặt hàng và thanh toán
  - Nhận thông báo qua email

## III. Công nghệ sử dụng:
- **Back-end**: Spring Boot (Java)
- **Front-end**: HTML, JavaScript, Tailwind CSS
- **Database**: MySQL
- **API Communication**: RESTful APIs
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Email Service**: Resend API

## IV. Phân tích:
### 1. Quy trình đặt hàng gồm các hoạt động chi tiết sau:
- **Bước 1**: Người dùng truy cập vào hệ thống.
- **Bước 2**: Người dùng duyệt xem danh sách sản phẩm.
- **Bước 3**: Người dùng thêm sản phẩm vào giỏ hàng.
- **Bước 4**: Người dùng bấm nút "Đặt hàng".
- **Bước 5**: Người dùng nhập thông tin giao hàng.
- **Bước 6**: Hệ thống truy xuất thông tin sản phẩm từ cơ sở dữ liệu.
- **Bước 7**:
  - Nếu thông tin hợp lệ (sản phẩm tồn tại, hàng tồn kho đủ), chuyển sang bước 8.
  - Nếu thông tin không hợp lệ (sản phẩm không đủ hàng), hiển thị lỗi và yêu cầu người dùng cập nhật giỏ hàng.
- **Bước 8**: Hệ thống thực hiện thao tác đặt hàng, cập nhật trạng thái sản phẩm, gửi email xác nhận và hiển thị kết quả.

### 2. Phân tích quy trình nghiệp vụ:
- Quy trình đặt hàng được chia thành các hành động sau:
  - Bắt đầu quy trình đặt hàng
  - Người dùng duyệt sản phẩm
  - Người dùng thêm sản phẩm vào giỏ hàng
  - Người dùng bấm nút đặt hàng
  - Người dùng nhập thông tin giao hàng
  - Nhận thông tin chi tiết về sản phẩm và kiểm tra tồn kho
  - Xác minh thông tin (sản phẩm có sẵn và đủ số lượng)
  - Nếu thông tin không hợp lệ, kết thúc quá trình và hiển thị lỗi
  - Nếu thông tin hợp lệ, thực hiện thao tác đặt hàng và cập nhật trạng thái sản phẩm
  - Gửi thông báo xác nhận qua email cho người dùng
  - Hiển thị kết quả đặt hàng thành công

### 3. Lọc ra các hành động không phù hợp:
- Một số hoạt động không phù hợp tự động hóa hoặc đóng gói dịch vụ sẽ bị gạch bỏ:
  - Bắt đầu quy trình đặt hàng
  - Nhận thông tin chi tiết về sản phẩm và kiểm tra tồn kho
  - Xác minh thông tin
  - Nếu thông tin không hợp lệ, kết thúc quá trình
  - Nếu thông tin hợp lệ, thực hiện thao tác đặt hàng và cập nhật trạng thái
  - Gửi thông báo xác nhận qua email cho người dùng
  - ~~Người dùng duyệt sản phẩm trên giao diện~~
  - ~~Người dùng xem chi tiết sản phẩm~~
  - ~~Người dùng điều chỉnh số lượng sản phẩm trong giỏ hàng~~

### 4. Xác định các ứng viên Entity Service:
- Bằng cách phân tích các hành động còn lại, phân loại những hành động được coi là bất khả tri. Những hành động không theo bất khả tri được in đậm:
  - **Bắt đầu quy trình đặt hàng**
  - **Người dùng thêm sản phẩm vào giỏ hàng**
  - **Người dùng bấm nút đặt hàng**
  - **Người dùng nhập thông tin giao hàng**
  - Nhận thông tin chi tiết về sản phẩm và kiểm tra tồn kho
  - **Xác minh thông tin**
  - **Nếu thông tin không hợp lệ, kết thúc quá trình**
  - **Nếu thông tin hợp lệ, thực hiện thao tác đặt hàng và cập nhật trạng thái**
  - **Gửi thông báo xác nhận qua email cho người dùng**

- Các hành động bất khả tri được phân loại thành Entity Service sơ bộ và được nhóm lại thành:
  - **Shoe-Service**: Cung cấp thông tin giày và kiểm tra tồn kho (GET /api/shoes/{id}, GET /api/shoes/check).
  - **Cart-Service**: Quản lý giỏ hàng của người dùng (GET /api/cart/{username}/items, POST /api/cart/{username}/items, PUT /api/cart/{username}/items/{itemId}, DELETE /api/cart/{username}/items/{itemId}).
  - **Order-Service**: Quản lý thao tác đặt hàng (POST /api/orders, GET /api/orders/{id}), xác minh thông tin sản phẩm và xử lý đơn hàng.

### 5. Xác định logic cụ thể cho quy trình:
- Các hành động không tuân theo bất khả tri vì chúng được quy định cụ thể cho quy trình đặt hàng:
  - Bắt đầu quy trình đặt hàng
  - **Người dùng thêm sản phẩm vào giỏ hàng**
  - **Người dùng bấm nút đặt hàng**
  - **Người dùng nhập thông tin giao hàng**
  - **Xác minh thông tin**
  - **Nếu thông tin không hợp lệ, kết thúc quá trình**
  - **Nếu thông tin hợp lệ, thực hiện thao tác đặt hàng và cập nhật trạng thái**
  - **Gửi thông báo xác nhận qua email cho người dùng**

- Hành động bắt đầu quy trình đặt hàng và các hành động xử lý đơn hàng được thực hiện trong **Order-Service**.
- Các hành động in đậm còn lại là logic nội bộ được xử lý thông qua tương tác giữa các dịch vụ.

### 6. Xác định các nguồn lực:
- **Danh sách các ngữ cảnh chức năng để xác định tài nguyên**:
  - Agnostic: `/api/shoes/`, `/api/cart/`, `/api/orders/`, `/api/notifications/` (các tài nguyên tái sử dụng được).
- **Ánh xạ giữa thực thể/dịch vụ và tài nguyên**:

| Entity/Service        | Resource                |
|:---------------------:|:-----------------------:|
| Shoe                  | /api/shoes/             |
| Cart                  | /api/cart/              |
| Order                 | /api/orders/            |
| Notification          | /api/notifications/     |
| User                  | /api/users/             |

### 7. Liên kết năng lực dịch vụ với tài nguyên phương thức:
- **Liên kết các dịch vụ với tài nguyên và phương thức HTTP**:
  - **Shoe-Service (Entity)**: 
    - Resource: `/api/shoes/`
    - Methods: 
      - `GET /api/shoes` (lấy tất cả sản phẩm giày)
      - `GET /api/shoes/{id}` (lấy thông tin giày theo ID)
      - `GET /api/shoes/brands/{brandId}` (lấy giày theo thương hiệu)
      - `GET /api/shoes/categories/{categoryId}` (lấy giày theo danh mục)
      - `GET /api/shoes/gender/{gender}` (lấy giày theo giới tính)
      - `GET /api/shoes/price` (lấy giày trong khoảng giá)
      - `GET /api/shoes/{id}/inventory` (lấy thông tin tồn kho của giày)
      - `GET /api/shoes/check` (kiểm tra số lượng giày còn)
      - `PUT /api/shoes/{id}/updateInventory` (cập nhật số lượng tồn kho)
      - `GET /api/shoes/brands` (lấy danh sách thương hiệu)
      - `GET /api/shoes/categories` (lấy danh sách danh mục)
  - **Cart-Service (Entity)**: 
    - Resource: `/api/cart/`
    - Methods: 
      - `GET /api/cart/{username}/items` (lấy danh sách sản phẩm trong giỏ hàng)
      - `POST /api/cart/{username}/items` (thêm sản phẩm vào giỏ hàng)
      - `PUT /api/cart/{username}/items/{itemId}` (cập nhật số lượng sản phẩm)
      - `DELETE /api/cart/{username}/items/{itemId}` (xóa sản phẩm khỏi giỏ hàng)
      - `DELETE /api/cart/{username}/shoes/{shoeId}` (xóa tất cả sản phẩm có cùng shoeId)
  - **Order-Service (Entity)**: 
    - Resource: `/api/orders/`
    - Methods: 
      - `POST /api/orders` (tạo đơn hàng mới)
      - `GET /api/orders/{id}` (lấy thông tin đơn hàng theo ID)
      - `GET /api/orders/user/{username}` (lấy danh sách đơn hàng của người dùng)
      - `PUT /api/orders/{id}/status` (cập nhật trạng thái đơn hàng)
  - **User-Service (Entity)**:
    - Resource: `/api/users/`
    - Methods:
      - `GET /api/users/{username}` (lấy thông tin người dùng)
      - `GET /api/users/{username}/permission` (kiểm tra quyền người dùng)
      - `POST /api/users/{username}/info` (lưu thông tin cá nhân của người dùng)
  - **Notification-Service (Utility)**: 
    - Resource: `/api/notifications/`
    - Method: 
      - `POST /api/notifications/email` (gửi email xác nhận đơn hàng)

### 8. Áp dụng hướng dịch vụ:
- Quy trình đặt hàng sử dụng các nguyên tắc định hướng dịch vụ (SOA) để đảm bảo tính độc lập, tái sử dụng, và khả năng mở rộng của các dịch vụ.
- Mỗi dịch vụ (**Shoe**, **Cart**, **Order**, **User**) được thiết kế để xử lý một phần cụ thể của quy trình, với giao tiếp qua RESTful APIs.
- Các dịch vụ đăng ký với Eureka Server để khám phá dịch vụ và cân bằng tải.
- API Gateway điều hướng các yêu cầu từ người dùng đến các dịch vụ tương ứng.

### 9. Xác định ứng viên thành phần dịch vụ:
- **Order-Service** gọi **Shoe-Service** để kiểm tra tồn kho và cập nhật số lượng sản phẩm.
- **Order-Service** gọi **Cart-Service** để lấy thông tin giỏ hàng.
- **Order-Service** gọi **Notification-Service** để gửi thông báo xác nhận đơn hàng.
- **Order-Service** gọi **User-Service** để xác thực người dùng và lấy thông tin người dùng.
- Các dịch vụ giao tiếp thông qua API Gateway và sử dụng Eureka Server để khám phá dịch vụ.

### 10. Phân tích các yêu cầu xử lý:
- Hành động xác minh thông tin được thực hiện trong **Order-Service** thông qua gọi **Shoe-Service**.
- **Shoe-Service** cung cấp thông tin sản phẩm và kiểm tra tồn kho.
- **Cart-Service** quản lý giỏ hàng của người dùng.
- **Order-Service** tạo và quản lý đơn hàng, điều phối quy trình đặt hàng.
- **User-Service** quản lý thông tin và xác thực người dùng.
- **Notification-Service** xử lý việc gửi email xác nhận.

### 11. Xác định ứng viên dịch vụ tiện ích:
- **Notification-Service**: Hành động gửi email với phương thức `POST /api/notifications/email`.

### 12. Xác định ứng viên vi dịch vụ:
- **Eureka-Server**: Dịch vụ đăng ký và khám phá.
- **API-Gateway**: Dịch vụ định tuyến yêu cầu.

### 13. Kết quả sau khi phân tích:

Hệ thống đặt hàng trực tuyến được thiết kế với các microservices sau:

1. **Shoe-Service**: Quản lý thông tin giày và tồn kho
2. **Cart-Service**: Quản lý giỏ hàng của người dùng
3. **Order-Service**: Quản lý đơn hàng và điều phối quy trình đặt hàng
4. **User-Service**: Quản lý thông tin và xác thực người dùng
5. **Notification-Service**: Gửi thông báo qua email
6. **Eureka-Server**: Đăng ký và khám phá dịch vụ
7. **API-Gateway**: Định tuyến yêu cầu đến các dịch vụ tương ứng

Kiến trúc microservices này cho phép hệ thống có tính mô-đun cao, khả năng mở rộng và khả năng chịu lỗi tốt. Mỗi dịch vụ có thể được phát triển, triển khai và mở rộng độc lập, đồng thời cho phép hệ thống tiếp tục hoạt động ngay cả khi một số dịch vụ gặp sự cố.