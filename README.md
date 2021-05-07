# 1. @Entity
- @Entity: có thuộc tính name đặt tên cho entity trong câu lệnh truy vấn
# 2. @Table
- @Table: chỉ rõ ánh xạ xuống bảng nào trong cơ sở dữ liệu. @Table có 3 thuộc tính
--name: tên bảng
--catalog: Khác biệt giữa catalog và schema
--schema: tên nhóm các bảng. Một CSDL có thể chia thành nhiều schema để nhóm các bảng cùng chức năng lại ví dụ: Accounting, HR, Engneering, Sales
# 3. @Data
- @Data là một annotation của lombok kết hợp của các annotation @ToString , @EqualsAndHashCode , @Getter / @Setter và @RequiredArgsConstructor lại với nhau
- Các annotation này sẽ tạo ra các method tương ứng

# 4. @NoArgsConstructor
- Annotation đánh dấu tạo nhanh một constructor không có tham số
# 5. @AllArgsConstructor
- Annotation đánh dấu tạo ra một constructor có đầy đủ tham số

# 6. @Id
- @Id dùng để đánh dấu một trường rất đặc biệt: primary key (khoá chính) nó có giá trị duy nhất (unique) trong cả cột dữ liệu. Primary key có mấy đặc điểm sau:

--Tính duy nhất (uniqueness)
--Không được phép rỗng (not null)
--Cần được đánh chỉ mục (index) để tăng tốc độ tìm kiếm
--Primary phải có kiểu là số nguyên hoặc chuỗi ký tự.

# 7.@GeneratedValue
Để đảm bảo tính duy nhất của @Id, primary key, có 2 cách chính:
1. Ứng dụng phải tự sinh giá trị duy nhất rồi gán vào đối tượng --> Entity --> Table
2. Sử dụng @GeneratedValue để chỉ định CSDL tự sinh giá trị duy nhất.

Có 4 cách:
- GenerationType.AUTO: để cho CSDL tự chọn cách nào phù hợp tuỳ thuộc vào loại cơ sở dữ liệu và kiểu của primary key.
- GenerationType.IDENTITY: cột tự động tăng. MySQL, Postgresql, MS-SQL, Oracle 12C hỗ trợ.
- GenerationType.SEQUENCE: sinh ra các giá trị liên tiếp, có thể cách nhau.
- GenerationType.TABLE: ít sử dụng vì tốc độ chậm, yêu cầu tạm thời khoá tạo bản ghi mới vào bản trong lúc sinh primary key
-- Với GenerationType.AUTO nếu là primary kiểu numeric thì AUTO sẽ chọn IDENTITY hoặc SEQUENCE. Nếu CSDL là Oracle 11C không hỗ trợ IDENTITY thì nó chuyển sang SEQUENCE. Còn nếu là kiểu UUID thì nó sẽ trả về chuỗi 32 chữ số Hexadecimal kiểu như thế này '123e4567-e89b-12d3-a456-426614174000'

# 8. @Column
@Column là annotation bổ xung tính chất cho cột tương ứng với trường trong class. Nó có tham số như sau:

- String name: đặt lên tên cột khác với tên trường
- boolean unique() default false;: đặt yêu cầu duy nhất nếu là true. Mặc định là - false
- boolean nullable() default true;: cho phép cột có giá trị null không. Mặc định là true
- boolean updatable() default true;: cho phép cột có được sửa đổi dữ liệu không. Mặc định là true
- String columnDefinition() default "";: tuỳ biến luôn cả cách tạo ra cột bằng cách viết trực tiếp câu lệnh SQL DDL (Data Defintion Language)
- int length() default 255;: định số ký tự nếu là String
# 9. @NaturalId
Ngoài @Id JPA còn có @NaturalId như là một khoá uniqe khác hỗ trợ cho việc tìm kiếm. Ví dụ trong nội bộ ứng dụng, tìm kiếm product bằng id dạng numeric cho tốc độ nhanh nhất. Tuy nhiên khi hiện thị ra giao diện web, mỗi product có một unique slug (một đường dẫn gợi nhớ duy nhất). Yêu cầu làm sao chỉ dùng unique slug mà vẫn tìm ra sản phẩm mà không cần id. Lúc này ta dùng @NaturalId để làm một unique key bổ trợ cho primary key.

Khác biệt giữa @Id primary key và @NaturalId là gì?

-- Primary Key và NaturalId luôn là Unique và Not Null
-- Primary Key nên giữ nguyên, ổn định, tuyết đối không thay đổi. Nó được dùng để join các bảng khác
-- NaturalID có thể thay đổi ví dụ như slug, email, số di động. Nó chỉ dùng để tìm kiếm đối tượng chứ không nên dùng để join các bảng khác.
# 10. @OneToMany
Chỉ định một liên kết nhiều giá trị với một-nhiều-nhiều.
Nếu tập hợp được xác định bằng cách sử dụng generic để chỉ định loại phần tử, thì loại thực thể mục tiêu được liên kết không cần được chỉ định; nếu không thì lớp thực thể đích phải được chỉ định. Nếu mối quan hệ là hai chiều, mappedBy phần tử phải được sử dụng để chỉ định trường mối quan hệ hoặc thuộc tính của thực thể là chủ sở hữu của mối quan hệ.
# 11. cascade = CascadeType.ALL
Cascade là cơ chế cho phép lan truyền quá trình chuyển đổi từ một entity đến các related entity(các entity có mối quan hệ với một entity khác).
- CascadeType.ALL nó là nó sẽ thực hiện tất cả các hành động ( PERSIST, REMOVE, REFRESH, MERGE, DETACH) đến các thực thể liên quan.
# 11. orphanRemoval = true
Thực thể "con" bị xóa khi nó không còn được tham chiếu (cha mẹ của nó có thể không bị xóa).
# 12. fetch = FetchType.LAZY
FetchType.LAZY - Tìm nạp khi bạn cần
Các FetchType.LAZY nói với Hibernate để chỉ lấy các đối tượng có liên quan từ cơ sở dữ liệu khi bạn sử dụng các mối quan hệ
# 13. fetch = FetchType.EAGER
- FetchType.EAGER - Tìm nạp nó để bạn có nó khi cần
- Các FetchType.EAGER nói với Hibernate để có được tất cả các yếu tố của một mối quan hệ khi lựa chọn đơn vị gốc
# 14. @JoinColumn
- @JoinColumn  đánh dấu một cột làm cột nối cho một liên kết thực thể hoặc một tập hợp phần tử.

# 15. @PrePersist
@PrePersist được sử dụng để định cấu hình lệnh gọi lại cho các sự kiện tồn tại trước (chèn trước) của thực thể. Nói cách khác, nó được sử dụng để chú thích các phương thức của mô hình để chỉ ra rằng phương thức nên được gọi trước khi thực thể được chèn (tồn tại) vào cơ sở dữ liệu.

# 16. @PreUpdate
@PreUpdate được sử dụng để định cấu hình lệnh gọi lại trước cập nhật cho mô hình thực thể, tức là, nó được sử dụng để chú thích các phương thức trong mô hình để chỉ ra một hoạt động cần được kích hoạt trước khi một thực thể được cập nhật trong cơ sở dữ liệu.

# 17. @ManyToOne
 Chỉ định một liên kết có giá trị đơn với một lớp thực thể khác có nhiều-một. Thông thường không cần thiết phải chỉ định thực thể đích một cách rõ ràng vì nó thường có thể được suy ra từ loại đối tượng đang được tham chiếu. Nếu mối quan hệ là hai chiều, thì OneToMany bên thực thể không sở hữu phải sử dụng mappedBy phần tử để chỉ định trường quan hệ hoặc thuộc tính của thực thể là chủ sở hữu của mối quan hệ.
 
 # 18. @JoinTable
Được sử dụng trong ánh xạ các liên kết.
Một bảng kết hợp thường được sử dụng trong ánh xạ của nhiều kết hợp với nhiều người và một chiều từ một đến nhiều. Nó cũng có thể được sử dụng để lập bản đồ các liên kết hai chiều nhiều-một / một-nhiều, các mối quan hệ nhiều-một-một hướng và các liên kết một-một (cả hai chiều và một chiều)
- name : Tên của bảng tham gia.Mặc định là tên được nối của hai bảng thực thể chính được liên kết, được phân tách bằng dấu gạch dưới.
- joinColumns: Các cột khóa ngoại của bảng tham chiếu đến bảng chính của thực thể sở hữu liên kết.Sử dụng các giá trị mặc định giống như cho JoinColumn.
- inverseJoinColumns: Các cột khóa ngoại của bảng tham chiếu đến bảng chính của thực thể không sở hữu liên kết.Sử dụng các giá trị mặc định giống như cho JoinColumn.
# 19. @FullTextField
@FullTextField ánh xạ một thuộc tính tới một trường chỉ mục toàn văn bản có cùng tên và kiểu. Các trường toàn văn bản được chia nhỏ thành các mã thông báo và chuẩn hóa (viết thường,…). Ở đây chúng tôi dựa trên cấu hình phân tích mặc định, nhưng hầu hết các ứng dụng cần phải tùy chỉnh nó; điều này sẽ được giải quyết sâu hơn.
