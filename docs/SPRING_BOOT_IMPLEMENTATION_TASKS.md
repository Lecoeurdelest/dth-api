# Spring Boot 3 Implementation Task Breakdown

**Based on:** Frontend Requirements (`FRON_END.md`)  
**Architecture:** Modular Monolith (Spring Boot 3, Java 17+)  
**Database:** PostgreSQL with Flyway Migrations  
**Conventions:** Lombok, MapStruct, Jakarta Validation, JPA

---

## Module 1: Auth Module (Authentication & Authorization)

### Database & Domain Layer:

- [x] **Task 1:** Create migration `V1__Create_auth_users_table.sql` in `resources/db/migration`.
    - [x] Create table `auth_users` with fields: `id`, `email`, `username`, `phone`, `password`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `first_name`, `last_name`, `avatar_url`, `google_id`, `facebook_id`, `created_at`, `updated_at`.
    - [x] Add unique constraints on `email` and `username`.
    - [x] Add indexes on `email`, `username`.

- [x] **Task 2:** Create `User.java` entity in `auth.domain`.
    - [x] Use `@Entity`, `@Table(name = "auth_users")`.
    - [x] Use Lombok `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
    - [x] Add `@CreatedDate` and `@LastModifiedDate` with `@EntityListeners(AuditingEntityListener.class)`.
    - [x] Add Jakarta Validation annotations (`@NotNull`, `@Size`, `@Email`).

- [x] **Task 3:** Create migration `V2__Create_auth_refresh_tokens_table.sql`.
    - [x] Create table `auth_refresh_tokens` with fields: `id`, `user_id`, `token`, `expires_at`, `created_at`.
    - [x] Add foreign key to `auth_users.id` (or reference via ID if cross-module).

- [x] **Task 4:** Create `RefreshToken.java` entity in `auth.domain`.
    - [x] Use Lombok annotations.
    - [x] Add relationship to `User` entity.

### Application Core:

- [x] **Task 5:** Create DTOs in `auth.dto`.
    - [x] Create `LoginRequest.java` with fields: `username` (or `email` or `phone`), `password`, `loginType` (enum: USERNAME, EMAIL, PHONE).
        - [x] Use Lombok `@Data`, `@NoArgsConstructor`.
        - [x] Add Jakarta Validation (`@NotBlank`, `@Size`).
    - [x] Create `RegisterRequest.java` with fields: `email`, `username`, `password`, `confirmPassword`, `phone`, `firstName`, `lastName`.
        - [x] Add validation for password match, email format, phone format.
    - [x] Create `AuthResponse.java` with fields: `accessToken`, `refreshToken`, `tokenType`, `expiresIn`, `user` (UserDto).
    - [x] Create `UserDto.java` with fields: `id`, `email`, `username`, `phone`, `firstName`, `lastName`, `avatarUrl`.
    - [x] Create `RefreshTokenRequest.java` with field: `refreshToken` (`@NotBlank`).

- [x] **Task 6:** Create `AuthMapper.java` interface in `auth.mapper`.
    - [x] Use MapStruct `@Mapper(componentModel = "spring")`.
    - [x] Add method `UserDto toDto(User user)`.
    - [x] Add method `User toEntity(RegisterRequest request)` (password excluded, set separately).

- [x] **Task 7:** Create `UserRepository.java` in `auth.repository`.
    - [x] Extend `JpaRepository<User, Long>`.
    - [x] Add method `Optional<User> findByEmail(String email)`.
    - [x] Add method `Optional<User> findByUsername(String username)`.
    - [x] Add method `Optional<User> findByPhone(String phone)`.
    - [x] Add method `Boolean existsByEmail(String email)`.
    - [x] Add method `Boolean existsByUsername(String username)`.

- [x] **Task 8:** Create `RefreshTokenRepository.java` in `auth.repository`.
    - [x] Extend `JpaRepository<RefreshToken, Long>`.
    - [x] Add method `Optional<RefreshToken> findByToken(String token)`.
    - [x] Add method `void deleteByUserId(Long userId)`.
    - [x] Add method `void deleteByExpiresAtBefore(LocalDateTime now)`.

- [x] **Task 9:** Implement `JwtTokenProvider.java` in `auth.config` (or `shared`).
    - [x] Use `io.jsonwebtoken:jjwt` library.
    - [x] Implement `generateAccessToken(User user)`.
    - [x] Implement `generateRefreshToken(User user)`.
    - [x] Implement `validateToken(String token)`.
    - [x] Implement `getUserIdFromToken(String token)`.
    - [ ] Implement `getUsernameFromToken(String token)` (Note: Using `getUserIdFromToken` instead, which is more appropriate for current implementation).
    - [x] Use `@Value` for secret key and expiration times.

- [x] **Task 10:** Implement `UserDetailsServiceImpl.java` in `auth.application`.
    - [x] Implement `UserDetailsService` interface.
    - [x] Inject `UserRepository` via `@RequiredArgsConstructor`.
    - [x] Implement `loadUserByUsername(String username)`.
    - [x] Return `UserDetails` with authorities.

- [x] **Task 11:** Implement `AuthService.java` in `auth.application`.
    - [x] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [x] Inject dependencies: `UserRepository`, `RefreshTokenRepository`, `AuthMapper`, `JwtTokenProvider`, `PasswordEncoder`.
    - [x] Implement `login(LoginRequest request)`:
        - [x] Find user by username/email/phone based on `loginType`.
        - [x] Validate password using `PasswordEncoder`.
        - [x] Generate access and refresh tokens.
        - [x] Save refresh token.
        - [x] Return `AuthResponse`.
    - [x] Implement `register(RegisterRequest request)`:
        - [x] Check if email/username exists.
        - [x] Encode password.
        - [x] Create user entity via mapper.
        - [x] Save user.
        - [x] Generate tokens.
        - [x] Return `AuthResponse`.
    - [x] Implement `refreshToken(RefreshTokenRequest request)`:
        - [x] Validate refresh token.
        - [x] Generate new access token.
        - [x] Return new `AuthResponse`.
    - [x] Implement `logout(String refreshToken)`:
        - [x] Delete refresh token.
    - [x] Annotate write methods with `@Transactional`.

### API Layer:

- [x] **Task 12:** Implement `AuthController.java` in `auth.api`.
    - [x] Use `@RestController`, `@RequestMapping("/api/auth")`.
    - [x] Inject `AuthService` via `@RequiredArgsConstructor`.
    - [x] Add `@PostMapping("/login")` endpoint:
        - [x] Accept `@RequestBody @Valid LoginRequest`.
        - [x] Call `authService.login()`.
        - [x] Return `ResponseEntity<ApiResponse<AuthResponse>>`.
    - [x] Add `@PostMapping("/register")` endpoint:
        - [x] Accept `@RequestBody @Valid RegisterRequest`.
        - [x] Call `authService.register()`.
        - [x] Return `ResponseEntity<ApiResponse<AuthResponse>>`.
    - [x] Add `@PostMapping("/refresh")` endpoint:
        - [x] Accept `@RequestBody @Valid RefreshTokenRequest`.
        - [x] Call `authService.refreshToken()`.
        - [x] Return `ResponseEntity<ApiResponse<AuthResponse>>`.
    - [x] Add `@PostMapping("/logout")` endpoint:
        - [x] Accept `@RequestBody RefreshTokenRequest`.
        - [x] Call `authService.logout()`.
        - [x] Return `ResponseEntity<ApiResponse<Void>>`.

### Security Configuration:

- [x] **Task 13:** Create `SecurityConfig.java` in `shared.config` (or `auth.config`).
    - [x] Extend `SecurityFilterChain` configuration.
    - [x] Configure `JwtAuthenticationFilter` to intercept requests.
    - [x] Configure public endpoints: `/api/auth/**`, `/api/services/**`, `/api/news/**`, `/api/contact/**`.
    - [x] Configure protected endpoints: `/api/profile/**`, `/api/orders/**`, `/api/loyalty/**`, `/api/tasks/**`.
    - [x] Configure `PasswordEncoder` bean (BCrypt).
    - [x] Configure `AuthenticationManager` bean.

- [x] **Task 14:** Create `JwtAuthenticationFilter.java` in `shared.security`.
    - [x] Extend `OncePerRequestFilter`.
    - [x] Extract token from `Authorization` header.
    - [x] Validate token via `JwtTokenProvider`.
    - [x] Load `UserDetails` via `UserDetailsService`.
    - [x] Set `SecurityContext` with authentication.

### Quality Assurance:

- [ ] **Task 15:** Write Unit Tests for `AuthService`.
    - [ ] Use `@ExtendWith(MockitoExtension.class)`.
    - [ ] Test `login()` success scenario.
    - [ ] Test `login()` with invalid credentials.
    - [ ] Test `register()` success scenario.
    - [ ] Test `register()` with duplicate email/username.
    - [ ] Test `refreshToken()` success scenario.
    - [ ] Test `refreshToken()` with invalid token.

- [ ] **Task 16:** Write Integration Tests for `AuthController`.
    - [ ] Use `@WebMvcTest(AuthController.class)`.
    - [ ] Test login endpoint with valid credentials.
    - [ ] Test login endpoint with invalid credentials.
    - [ ] Test register endpoint.
    - [ ] Test refresh token endpoint.

---

## Module 2: Services Module

### Database & Domain Layer:

- [ ] **Task 17:** Create migration `V3__Create_services_services_table.sql`.
    - [ ] Create table `services_services` with fields: `id`, `name`, `description`, `base_price`, `image_url`, `category`, `active`, `created_at`, `updated_at`.
    - [ ] Add index on `category`.
    - [ ] Add index on `active`.

- [ ] **Task 18:** Create `Service.java` entity in `services.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add auditing fields.
    - [ ] Add validation annotations.

- [ ] **Task 19:** Create migration `V4__Create_services_categories_table.sql`.
    - [ ] Create table `services_categories` with fields: `id`, `name`, `icon`, `description`, `display_order`, `active`, `created_at`, `updated_at`.

- [ ] **Task 20:** Create `ServiceCategory.java` entity in `services.domain`.
    - [ ] Use Lombok annotations.

- [ ] **Task 21:** Create migration `V5__Create_services_pricing_items_table.sql`.
    - [ ] Create table `services_pricing_items` with fields: `id`, `service_id`, `category_name`, `item_name`, `price`, `display_order`, `created_at`, `updated_at`.
    - [ ] Add foreign key to `services_services.id`.

- [ ] **Task 22:** Create `PricingItem.java` entity in `services.domain`.
    - [ ] Use `@ManyToOne` relationship to `Service`.
    - [ ] Use Lombok annotations.

- [ ] **Task 23:** Create migration `V6__Create_services_sub_services_table.sql`.
    - [ ] Create table `services_sub_services` with fields: `id`, `service_id`, `name`, `display_order`, `created_at`.
    - [ ] Add foreign key to `services_services.id`.

- [ ] **Task 24:** Create `SubService.java` entity in `services.domain`.
    - [ ] Use `@ManyToOne` relationship to `Service`.
    - [ ] Use Lombok annotations.

### Application Core:

- [ ] **Task 25:** Create DTOs in `services.dto`.
    - [ ] Create `ServiceDto.java` with fields: `id`, `name`, `description`, `basePrice`, `imageUrl`, `category`, `rating`, `reviewCount`, `serviceCount`.
    - [ ] Create `ServiceDetailDto.java` with fields: `id`, `title`, `description`, `headerImage`, `subServices` (List<String>), `pricingCategories` (List<PricingCategoryDto>), `commitments` (List<String>), `videoUrl`, `images` (List<String>), `reviews` (List<ReviewDto>).
    - [ ] Create `PricingCategoryDto.java` with fields: `category`, `items` (List<PricingItemDto>).
    - [ ] Create `PricingItemDto.java` with fields: `item`, `price`.
    - [ ] Create `ServiceCategoryDto.java` with fields: `id`, `name`, `icon`, `description`.
    - [ ] Create `ServiceListRequest.java` with fields: `category` (optional), `search` (optional), `page`, `size`, `sortBy`, `sortDir`.
    - [ ] Use Lombok `@Data`, `@Builder` for all DTOs.

- [ ] **Task 26:** Create `ServiceMapper.java` interface in `services.mapper`.
    - [ ] Use MapStruct `@Mapper(componentModel = "spring")`.
    - [ ] Add method `ServiceDto toDto(Service service)`.
    - [ ] Add method `ServiceDetailDto toDetailDto(Service service)`.
    - [ ] Add method `List<ServiceDto> toDtoList(List<Service> services)`.
    - [ ] Map pricing items and sub-services.

- [ ] **Task 27:** Create `ServiceRepository.java` in `services.repository`.
    - [ ] Extend `JpaRepository<Service, Long>`.
    - [ ] Add method `Page<Service> findByActiveTrue(Pageable pageable)`.
    - [ ] Add method `Page<Service> findByCategoryAndActiveTrue(String category, Pageable pageable)`.
    - [ ] Add method `Page<Service> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable)`.
    - [ ] Add method `Optional<Service> findByIdAndActiveTrue(Long id)`.

- [ ] **Task 28:** Create `ServiceCategoryRepository.java` in `services.repository`.
    - [ ] Extend `JpaRepository<ServiceCategory, Long>`.
    - [ ] Add method `List<ServiceCategory> findByActiveTrueOrderByDisplayOrderAsc()`.

- [ ] **Task 29:** Create `PricingItemRepository.java` in `services.repository`.
    - [ ] Extend `JpaRepository<PricingItem, Long>`.
    - [ ] Add method `List<PricingItem> findByServiceIdOrderByDisplayOrderAsc(Long serviceId)`.

- [ ] **Task 30:** Create `SubServiceRepository.java` in `services.repository`.
    - [ ] Extend `JpaRepository<SubService, Long>`.
    - [ ] Add method `List<SubService> findByServiceIdOrderByDisplayOrderAsc(Long serviceId)`.

- [ ] **Task 31:** Implement `ServiceService.java` in `services.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repositories and mapper.
    - [ ] Implement `getAllServices(ServiceListRequest request)`:
        - [ ] Build `Pageable` from request.
        - [ ] Query based on filters (category, search).
        - [ ] Map to DTOs.
        - [ ] Calculate rating and review count (from reviews module).
        - [ ] Return `Page<ServiceDto>`.
    - [ ] Implement `getServiceById(Long id)`:
        - [ ] Find service by ID.
        - [ ] Load pricing items and sub-services.
        - [ ] Load reviews (from reviews module).
        - [ ] Map to `ServiceDetailDto`.
        - [ ] Throw `ResourceNotFoundException` if not found.
    - [ ] Implement `getServiceCategories()`:
        - [ ] Find all active categories.
        - [ ] Map to DTOs.
        - [ ] Return `List<ServiceCategoryDto>`.
    - [ ] Annotate read methods with `@Transactional(readOnly = true)`.

### API Layer:

- [ ] **Task 32:** Implement `ServiceController.java` in `services.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/services")`.
    - [ ] Inject `ServiceService` via `@RequiredArgsConstructor`.
    - [ ] Add `@GetMapping` endpoint:
        - [ ] Accept query params: `category`, `search`, `page`, `size`, `sortBy`, `sortDir`.
        - [ ] Call `serviceService.getAllServices()`.
        - [ ] Return `ResponseEntity<ApiResponse<Page<ServiceDto>>>`.
    - [ ] Add `@GetMapping("/{id}")` endpoint:
        - [ ] Extract `id` from path variable.
        - [ ] Call `serviceService.getServiceById()`.
        - [ ] Return `ResponseEntity<ApiResponse<ServiceDetailDto>>`.
    - [ ] Add `@GetMapping("/categories")` endpoint:
        - [ ] Call `serviceService.getServiceCategories()`.
        - [ ] Return `ResponseEntity<ApiResponse<List<ServiceCategoryDto>>>`.

### Quality Assurance:

- [ ] **Task 33:** Write Unit Tests for `ServiceService`.
    - [ ] Test `getAllServices()` with filters.
    - [ ] Test `getServiceById()` success scenario.
    - [ ] Test `getServiceById()` with non-existent ID.
    - [ ] Test `getServiceCategories()`.

---

## Module 3: Orders Module

### Database & Domain Layer:

- [ ] **Task 34:** Create migration `V7__Create_orders_orders_table.sql`.
    - [ ] Create table `orders_orders` with fields: `id`, `user_id`, `service_id`, `order_code` (unique), `status`, `total_amount`, `address`, `customer_name`, `customer_phone`, `issue_description`, `payment_method`, `notes`, `created_at`, `updated_at`.
    - [ ] Add index on `user_id`.
    - [ ] Add index on `status`.
    - [ ] Add index on `order_code`.
    - [ ] Add enum for `status`: PENDING, PROCESSING, REPAIRING, COMPLETED, CANCELLED.

- [ ] **Task 35:** Create `Order.java` entity in `orders.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add `@Enumerated(EnumType.STRING)` for status.
    - [ ] Add auditing fields.

- [ ] **Task 36:** Create migration `V8__Create_orders_order_items_table.sql`.
    - [ ] Create table `orders_order_items` with fields: `id`, `order_id`, `item_name`, `quantity`, `unit_price`, `total_price`, `created_at`.
    - [ ] Add foreign key to `orders_orders.id`.

- [ ] **Task 37:** Create `OrderItem.java` entity in `orders.domain`.
    - [ ] Use `@ManyToOne` relationship to `Order`.
    - [ ] Use Lombok annotations.

- [ ] **Task 38:** Create migration `V9__Create_orders_order_images_table.sql`.
    - [ ] Create table `orders_order_images` with fields: `id`, `order_id`, `image_url`, `display_order`, `created_at`.
    - [ ] Add foreign key to `orders_orders.id`.

- [ ] **Task 39:** Create `OrderImage.java` entity in `orders.domain`.
    - [ ] Use `@ManyToOne` relationship to `Order`.
    - [ ] Use Lombok annotations.

- [ ] **Task 40:** Create migration `V10__Create_orders_order_timeline_table.sql`.
    - [ ] Create table `orders_order_timeline` with fields: `id`, `order_id`, `status`, `description`, `completed`, `completed_at`, `created_at`.
    - [ ] Add foreign key to `orders_orders.id`.

- [ ] **Task 41:** Create `OrderTimeline.java` entity in `orders.domain`.
    - [ ] Use `@ManyToOne` relationship to `Order`.
    - [ ] Use Lombok annotations.

- [ ] **Task 42:** Create migration `V11__Create_orders_technician_assignments_table.sql`.
    - [ ] Create table `orders_technician_assignments` with fields: `id`, `order_id`, `technician_id` (reference to workers module), `assigned_at`, `created_at`.
    - [ ] Add foreign key to `orders_orders.id`.

- [ ] **Task 43:** Create `TechnicianAssignment.java` entity in `orders.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Store `technicianId` as `Long` (no FK, cross-module reference).

- [ ] **Task 44:** Create migration `V12__Create_orders_invoices_table.sql`.
    - [ ] Create table `orders_invoices` with fields: `id`, `order_id`, `invoice_number` (unique), `issue_date`, `status`, `warranty_note`, `created_at`, `updated_at`.
    - [ ] Add foreign key to `orders_orders.id`.

- [ ] **Task 45:** Create `Invoice.java` entity in `orders.domain`.
    - [ ] Use `@OneToOne` relationship to `Order`.
    - [ ] Use Lombok annotations.

### Application Core:

- [ ] **Task 46:** Create DTOs in `orders.dto`.
    - [ ] Create `CreateOrderRequest.java` with fields: `serviceId`, `address`, `customerName`, `customerPhone`, `issueDescription`, `issueImages` (List<String>), `paymentMethod`, `notes`.
        - [ ] Add Jakarta Validation annotations.
    - [ ] Create `OrderDto.java` with fields: `id`, `orderCode`, `serviceType`, `serviceIcon`, `orderDate`, `status`, `address`, `totalCost`, `customerName`, `customerPhone`, `issueDescription`, `issueImages`, `technician`, `timeline`, `costDetails`, `paymentMethod`, `notes`, `canReview`, `reviewSubmitted`, `invoice`.
    - [ ] Create `OrderItemDto.java` with fields: `item`, `quantity`, `unitPrice`, `total`.
    - [ ] Create `TimelineStepDto.java` with fields: `status`, `time`, `completed`, `description`.
    - [ ] Create `TechnicianInfoDto.java` with fields: `id`, `name`, `avatar`, `experience`, `rating`, `phone`.
    - [ ] Create `InvoiceDto.java` with fields: `invoiceNumber`, `issueDate`, `status`, `warrantyNote`.
    - [ ] Create `OrderListRequest.java` with fields: `status` (optional), `page`, `size`, `sortBy`, `sortDir`.
    - [ ] Create `UpdateOrderStatusRequest.java` with field: `status` (`@NotNull`, `@Valid` enum).
    - [ ] Create `CancelOrderRequest.java` with field: `reason` (`@NotBlank`).

- [ ] **Task 47:** Create `OrderMapper.java` interface in `orders.mapper`.
    - [ ] Use MapStruct `@Mapper(componentModel = "spring")`.
    - [ ] Add method `OrderDto toDto(Order order)`.
    - [ ] Add method `List<OrderDto> toDtoList(List<Order> orders)`.
    - [ ] Map order items, images, timeline, technician info, invoice.

- [ ] **Task 48:** Create `OrderRepository.java` in `orders.repository`.
    - [ ] Extend `JpaRepository<Order, Long>`.
    - [ ] Add method `Page<Order> findByUserId(Long userId, Pageable pageable)`.
    - [ ] Add method `Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable)`.
    - [ ] Add method `Optional<Order> findByOrderCode(String orderCode)`.
    - [ ] Add method `Optional<Order> findByIdAndUserId(Long id, Long userId)`.

- [ ] **Task 49:** Create `OrderItemRepository.java` in `orders.repository`.
    - [ ] Extend `JpaRepository<OrderItem, Long>`.
    - [ ] Add method `List<OrderItem> findByOrderId(Long orderId)`.

- [ ] **Task 50:** Create `OrderImageRepository.java` in `orders.repository`.
    - [ ] Extend `JpaRepository<OrderImage, Long>`.
    - [ ] Add method `List<OrderImage> findByOrderIdOrderByDisplayOrderAsc(Long orderId)`.

- [ ] **Task 51:** Create `OrderTimelineRepository.java` in `orders.repository`.
    - [ ] Extend `JpaRepository<OrderTimeline, Long>`.
    - [ ] Add method `List<OrderTimeline> findByOrderIdOrderByCreatedAtAsc(Long orderId)`.

- [ ] **Task 52:** Create `InvoiceRepository.java` in `orders.repository`.
    - [ ] Extend `JpaRepository<Invoice, Long>`.
    - [ ] Add method `Optional<Invoice> findByOrderId(Long orderId)`.

- [ ] **Task 53:** Implement `OrderService.java` in `orders.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repositories, mapper, and `ServiceApplicationService` (from services module).
    - [ ] Implement `createOrder(Long userId, CreateOrderRequest request)`:
        - [ ] Generate unique order code.
        - [ ] Fetch service details via `ServiceApplicationService`.
        - [ ] Calculate total amount from service pricing.
        - [ ] Create order entity.
        - [ ] Create order items from service pricing.
        - [ ] Save order images.
        - [ ] Create initial timeline entry (PENDING).
        - [ ] Save order.
        - [ ] Return `OrderDto`.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `getUserOrders(Long userId, OrderListRequest request)`:
        - [ ] Build `Pageable` from request.
        - [ ] Query orders by user ID and status filter.
        - [ ] Load related data (items, images, timeline, technician, invoice).
        - [ ] Map to DTOs.
        - [ ] Return `Page<OrderDto>`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.
    - [ ] Implement `getOrderById(Long orderId, Long userId)`:
        - [ ] Find order by ID and user ID.
        - [ ] Load all related data.
        - [ ] Map to DTO.
        - [ ] Throw `ResourceNotFoundException` if not found.
        - [ ] Annotate with `@Transactional(readOnly = true)`.
    - [ ] Implement `updateOrderStatus(Long orderId, UpdateOrderStatusRequest request)`:
        - [ ] Find order by ID.
        - [ ] Update status.
        - [ ] Create timeline entry.
        - [ ] Save order.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `cancelOrder(Long orderId, Long userId, CancelOrderRequest request)`:
        - [ ] Find order by ID and user ID.
        - [ ] Validate order can be cancelled (status check).
        - [ ] Update status to CANCELLED.
        - [ ] Create timeline entry.
        - [ ] Save order.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `assignTechnician(Long orderId, Long technicianId)`:
        - [ ] Find order by ID.
        - [ ] Create technician assignment.
        - [ ] Update order status to PROCESSING.
        - [ ] Create timeline entry.
        - [ ] Save order.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `generateInvoice(Long orderId)`:
        - [ ] Find order by ID.
        - [ ] Check if order is COMPLETED.
        - [ ] Generate unique invoice number.
        - [ ] Create invoice entity.
        - [ ] Save invoice.
        - [ ] Return `InvoiceDto`.
        - [ ] Annotate with `@Transactional`.

### API Layer:

- [ ] **Task 54:** Implement `OrderController.java` in `orders.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/orders")`.
    - [ ] Use `@PreAuthorize("isAuthenticated()")` on class level.
    - [ ] Inject `OrderService` via `@RequiredArgsConstructor`.
    - [ ] Add `@PostMapping` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Accept `@RequestBody @Valid CreateOrderRequest`.
        - [ ] Call `orderService.createOrder()`.
        - [ ] Return `ResponseEntity<ApiResponse<OrderDto>>`.
    - [ ] Add `@GetMapping` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Accept query params: `status`, `page`, `size`, `sortBy`, `sortDir`.
        - [ ] Call `orderService.getUserOrders()`.
        - [ ] Return `ResponseEntity<ApiResponse<Page<OrderDto>>>`.
    - [ ] Add `@GetMapping("/{id}")` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Extract `id` from path variable.
        - [ ] Call `orderService.getOrderById()`.
        - [ ] Return `ResponseEntity<ApiResponse<OrderDto>>`.
    - [ ] Add `@PutMapping("/{id}/status")` endpoint (admin only):
        - [ ] Extract `id` from path variable.
        - [ ] Accept `@RequestBody @Valid UpdateOrderStatusRequest`.
        - [ ] Call `orderService.updateOrderStatus()`.
        - [ ] Return `ResponseEntity<ApiResponse<OrderDto>>`.
    - [ ] Add `@PostMapping("/{id}/cancel")` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Extract `id` from path variable.
        - [ ] Accept `@RequestBody @Valid CancelOrderRequest`.
        - [ ] Call `orderService.cancelOrder()`.
        - [ ] Return `ResponseEntity<ApiResponse<OrderDto>>`.
    - [ ] Add `@PostMapping("/{id}/invoice")` endpoint:
        - [ ] Extract `id` from path variable.
        - [ ] Call `orderService.generateInvoice()`.
        - [ ] Return `ResponseEntity<ApiResponse<InvoiceDto>>`.

### Quality Assurance:

- [ ] **Task 55:** Write Unit Tests for `OrderService`.
    - [ ] Test `createOrder()` success scenario.
    - [ ] Test `getUserOrders()` with filters.
    - [ ] Test `getOrderById()` success scenario.
    - [ ] Test `cancelOrder()` success scenario.
    - [ ] Test `cancelOrder()` with invalid status.

---

## Module 4: Reviews Module

### Database & Domain Layer:

- [ ] **Task 56:** Create migration `V13__Create_orders_reviews_table.sql`.
    - [ ] Create table `orders_reviews` with fields: `id`, `order_id`, `user_id`, `service_id`, `rating`, `service_rating`, `technician_rating`, `comment`, `created_at`, `updated_at`.
    - [ ] Add unique constraint on `order_id` (one review per order).
    - [ ] Add index on `service_id`.
    - [ ] Add index on `user_id`.

- [ ] **Task 57:** Create `OrderReview.java` entity in `orders.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add `@OneToOne` relationship to `Order`.
    - [ ] Add validation annotations (`@Min(1)`, `@Max(5)` for ratings).

- [ ] **Task 58:** Create migration `V14__Create_orders_review_images_table.sql`.
    - [ ] Create table `orders_review_images` with fields: `id`, `review_id`, `image_url`, `display_order`, `created_at`.
    - [ ] Add foreign key to `orders_reviews.id`.

- [ ] **Task 59:** Create `ReviewImage.java` entity in `orders.domain`.
    - [ ] Use `@ManyToOne` relationship to `OrderReview`.
    - [ ] Use Lombok annotations.

### Application Core:

- [ ] **Task 60:** Create DTOs in `orders.dto`.
    - [ ] Create `CreateReviewRequest.java` with fields: `orderId`, `rating`, `serviceRating`, `technicianRating`, `comment`, `images` (List<String>).
        - [ ] Add Jakarta Validation (`@NotNull`, `@Min(1)`, `@Max(5)`, `@Size(min = 10)` for comment).
    - [ ] Create `ReviewDto.java` with fields: `id`, `userName`, `userAvatar`, `rating`, `serviceRating`, `technicianRating`, `date`, `comment`, `images`.
    - [ ] Update `OrderDto` to include `canReview` and `reviewSubmitted` flags.

- [ ] **Task 61:** Create `ReviewMapper.java` interface in `orders.mapper`.
    - [ ] Add method `ReviewDto toDto(OrderReview review)`.
    - [ ] Map user info from auth module (via application service).

- [ ] **Task 62:** Create `OrderReviewRepository.java` in `orders.repository`.
    - [ ] Extend `JpaRepository<OrderReview, Long>`.
    - [ ] Add method `Optional<OrderReview> findByOrderId(Long orderId)`.
    - [ ] Add method `List<OrderReview> findByServiceIdOrderByCreatedAtDesc(Long serviceId, Pageable pageable)`.
    - [ ] Add method `Double calculateAverageRatingByServiceId(Long serviceId)`.
    - [ ] Add method `Long countByServiceId(Long serviceId)`.

- [ ] **Task 63:** Implement `ReviewService.java` in `orders.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repositories and mapper.
    - [ ] Implement `createReview(Long userId, CreateReviewRequest request)`:
        - [ ] Find order by ID and user ID.
        - [ ] Validate order is COMPLETED.
        - [ ] Check if review already exists.
        - [ ] Create review entity.
        - [ ] Save review images.
        - [ ] Update order (set `reviewSubmitted = true`).
        - [ ] Save review.
        - [ ] Return `ReviewDto`.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `getReviewsByServiceId(Long serviceId, Pageable pageable)`:
        - [ ] Find reviews by service ID.
        - [ ] Load review images.
        - [ ] Map to DTOs.
        - [ ] Return `Page<ReviewDto>`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.
    - [ ] Implement `getReviewByOrderId(Long orderId)`:
        - [ ] Find review by order ID.
        - [ ] Map to DTO.
        - [ ] Return `Optional<ReviewDto>`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.

### API Layer:

- [ ] **Task 64:** Implement `ReviewController.java` in `orders.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/orders/reviews")`.
    - [ ] Use `@PreAuthorize("isAuthenticated()")` on class level.
    - [ ] Inject `ReviewService` via `@RequiredArgsConstructor`.
    - [ ] Add `@PostMapping` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Accept `@RequestBody @Valid CreateReviewRequest`.
        - [ ] Call `reviewService.createReview()`.
        - [ ] Return `ResponseEntity<ApiResponse<ReviewDto>>`.
    - [ ] Add `@GetMapping("/service/{serviceId}")` endpoint (public):
        - [ ] Extract `serviceId` from path variable.
        - [ ] Accept query params: `page`, `size`.
        - [ ] Call `reviewService.getReviewsByServiceId()`.
        - [ ] Return `ResponseEntity<ApiResponse<Page<ReviewDto>>>`.
    - [ ] Add `@GetMapping("/order/{orderId}")` endpoint:
        - [ ] Extract `orderId` from path variable.
        - [ ] Call `reviewService.getReviewByOrderId()`.
        - [ ] Return `ResponseEntity<ApiResponse<ReviewDto>>`.

### Quality Assurance:

- [ ] **Task 65:** Write Unit Tests for `ReviewService`.
    - [ ] Test `createReview()` success scenario.
    - [ ] Test `createReview()` with duplicate review.
    - [ ] Test `getReviewsByServiceId()`.

---

## Module 5: Profile Module

### Database & Domain Layer:

- [ ] **Task 66:** Create migration `V15__Create_profile_user_profiles_table.sql`.
    - [ ] Create table `profile_user_profiles` with fields: `id`, `user_id` (unique), `first_name`, `last_name`, `phone`, `email`, `address`, `avatar_url`, `birthdate`, `created_at`, `updated_at`.
    - [ ] Add foreign key to `auth_users.id` (or reference via ID).

- [ ] **Task 67:** Create `UserProfile.java` entity in `profile.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add auditing fields.

### Application Core:

- [ ] **Task 68:** Create DTOs in `profile.dto`.
    - [ ] Create `ProfileInfoDto.java` with fields: `name`, `birthdate`, `phone`, `email`, `address`, `avatar`.
    - [ ] Create `UpdateProfileRequest.java` with fields: `firstName`, `lastName`, `phone`, `email`, `address`, `birthdate`, `avatarUrl`.
        - [ ] Add Jakarta Validation annotations.

- [ ] **Task 69:** Create `ProfileMapper.java` interface in `profile.mapper`.
    - [ ] Add method `ProfileInfoDto toDto(UserProfile profile)`.
    - [ ] Add method `void updateEntityFromRequest(UpdateProfileRequest request, @MappingTarget UserProfile entity)`.

- [ ] **Task 70:** Create `UserProfileRepository.java` in `profile.repository`.
    - [ ] Extend `JpaRepository<UserProfile, Long>`.
    - [ ] Add method `Optional<UserProfile> findByUserId(Long userId)`.

- [ ] **Task 71:** Implement `ProfileService.java` in `profile.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repository and mapper.
    - [ ] Implement `getProfile(Long userId)`:
        - [ ] Find profile by user ID.
        - [ ] If not found, create default profile.
        - [ ] Map to DTO.
        - [ ] Return `ProfileInfoDto`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.
    - [ ] Implement `updateProfile(Long userId, UpdateProfileRequest request)`:
        - [ ] Find or create profile.
        - [ ] Update fields via mapper.
        - [ ] Save profile.
        - [ ] Return updated `ProfileInfoDto`.
        - [ ] Annotate with `@Transactional`.

### API Layer:

- [ ] **Task 72:** Implement `ProfileController.java` in `profile.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/profile")`.
    - [ ] Use `@PreAuthorize("isAuthenticated()")` on class level.
    - [ ] Inject `ProfileService` via `@RequiredArgsConstructor`.
    - [ ] Add `@GetMapping("/me")` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Call `profileService.getProfile()`.
        - [ ] Return `ResponseEntity<ApiResponse<ProfileInfoDto>>`.
    - [ ] Add `@PutMapping("/me")` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Accept `@RequestBody @Valid UpdateProfileRequest`.
        - [ ] Call `profileService.updateProfile()`.
        - [ ] Return `ResponseEntity<ApiResponse<ProfileInfoDto>>`.

### Quality Assurance:

- [ ] **Task 73:** Write Unit Tests for `ProfileService`.
    - [ ] Test `getProfile()` with existing profile.
    - [ ] Test `getProfile()` with non-existent profile (creates default).
    - [ ] Test `updateProfile()` success scenario.

---

## Module 6: Loyalty Module

### Database & Domain Layer:

- [ ] **Task 74:** Create migration `V16__Create_loyalty_points_table.sql`.
    - [ ] Create table `loyalty_points` with fields: `id`, `user_id` (unique), `total_points`, `current_tier`, `created_at`, `updated_at`.
    - [ ] Add index on `user_id`.

- [ ] **Task 75:** Create `LoyaltyPoints.java` entity in `loyalty.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add auditing fields.

- [ ] **Task 76:** Create migration `V17__Create_loyalty_points_transactions_table.sql`.
    - [ ] Create table `loyalty_points_transactions` with fields: `id`, `user_id`, `order_id` (reference to orders), `points`, `transaction_type` (enum: EARNED, REDEEMED, EXPIRED), `status` (enum: PENDING, CONFIRMED), `description`, `created_at`.
    - [ ] Add index on `user_id`.
    - [ ] Add index on `order_id`.

- [ ] **Task 77:** Create `PointsTransaction.java` entity in `loyalty.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add `@Enumerated(EnumType.STRING)` for enums.

- [ ] **Task 78:** Create migration `V18__Create_loyalty_promotion_tiers_table.sql`.
    - [ ] Create table `loyalty_promotion_tiers` with fields: `id`, `tier_name`, `points_required`, `discount_percentage`, `description`, `icon`, `display_order`, `active`, `created_at`, `updated_at`.

- [ ] **Task 79:** Create `PromotionTier.java` entity in `loyalty.domain`.
    - [ ] Use Lombok annotations.

### Application Core:

- [ ] **Task 80:** Create DTOs in `loyalty.dto`.
    - [ ] Create `LoyaltyDataDto.java` with fields: `totalPoints`, `currentTier`, `pointHistory` (List<PointsHistoryDto>), `promotionTiers` (List<PromotionTierDto>).
    - [ ] Create `PointsHistoryDto.java` with fields: `id`, `orderCode`, `serviceType`, `orderDate`, `points`, `status`.
    - [ ] Create `PromotionTierDto.java` with fields: `points`, `discount`, `description`, `icon`.
    - [ ] Create `EarnPointsRequest.java` with fields: `userId`, `orderId`, `points`, `description`.

- [ ] **Task 81:** Create `LoyaltyMapper.java` interface in `loyalty.mapper`.
    - [ ] Add method `LoyaltyDataDto toDto(LoyaltyPoints points)`.
    - [ ] Add method `PointsHistoryDto toHistoryDto(PointsTransaction transaction)`.
    - [ ] Add method `PromotionTierDto toTierDto(PromotionTier tier)`.

- [ ] **Task 82:** Create `LoyaltyPointsRepository.java` in `loyalty.repository`.
    - [ ] Extend `JpaRepository<LoyaltyPoints, Long>`.
    - [ ] Add method `Optional<LoyaltyPoints> findByUserId(Long userId)`.

- [ ] **Task 83:** Create `PointsTransactionRepository.java` in `loyalty.repository`.
    - [ ] Extend `JpaRepository<PointsTransaction, Long>`.
    - [ ] Add method `List<PointsTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable)`.

- [ ] **Task 84:** Create `PromotionTierRepository.java` in `loyalty.repository`.
    - [ ] Extend `JpaRepository<PromotionTier, Long>`.
    - [ ] Add method `List<PromotionTier> findByActiveTrueOrderByPointsRequiredAsc()`.

- [ ] **Task 85:** Implement `LoyaltyService.java` in `loyalty.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repositories, mapper, and `OrderApplicationService` (from orders module).
    - [ ] Implement `getLoyaltyData(Long userId)`:
        - [ ] Find or create loyalty points for user.
        - [ ] Load point history.
        - [ ] Load promotion tiers.
        - [ ] Calculate current tier.
        - [ ] Map to DTO.
        - [ ] Return `LoyaltyDataDto`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.
    - [ ] Implement `earnPoints(Long userId, Long orderId, Integer points)`:
        - [ ] Find or create loyalty points.
        - [ ] Create points transaction (PENDING).
        - [ ] Update total points.
        - [ ] Calculate and update tier.
        - [ ] Save transaction and points.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `confirmPointsTransaction(Long transactionId)`:
        - [ ] Find transaction by ID.
        - [ ] Update status to CONFIRMED.
        - [ ] Save transaction.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `getPromotionTiers()`:
        - [ ] Find all active tiers.
        - [ ] Map to DTOs.
        - [ ] Return `List<PromotionTierDto>`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.

### API Layer:

- [ ] **Task 86:** Implement `LoyaltyController.java` in `loyalty.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/loyalty")`.
    - [ ] Use `@PreAuthorize("isAuthenticated()")` on class level.
    - [ ] Inject `LoyaltyService` via `@RequiredArgsConstructor`.
    - [ ] Add `@GetMapping("/points")` endpoint:
        - [ ] Extract `userId` from `SecurityContext`.
        - [ ] Call `loyaltyService.getLoyaltyData()`.
        - [ ] Return `ResponseEntity<ApiResponse<LoyaltyDataDto>>`.
    - [ ] Add `@GetMapping("/tiers")` endpoint (public):
        - [ ] Call `loyaltyService.getPromotionTiers()`.
        - [ ] Return `ResponseEntity<ApiResponse<List<PromotionTierDto>>>`.

### Quality Assurance:

- [ ] **Task 87:** Write Unit Tests for `LoyaltyService`.
    - [ ] Test `getLoyaltyData()` with new user.
    - [ ] Test `earnPoints()` success scenario.
    - [ ] Test tier calculation logic.

---

## Module 7: Workers/Technicians Module

### Database & Domain Layer:

- [ ] **Task 88:** Create migration `V19__Create_workers_workers_table.sql`.
    - [ ] Create table `workers_workers` with fields: `id`, `user_id` (reference to auth_users), `name`, `avatar_url`, `phone`, `experience_years`, `specializations` (JSON or separate table), `rating`, `total_reviews`, `active`, `created_at`, `updated_at`.
    - [ ] Add index on `active`.
    - [ ] Add index on `specializations`.

- [ ] **Task 89:** Create `Worker.java` entity in `workers.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add `@ElementCollection` for specializations (or separate entity).
    - [ ] Add auditing fields.

- [ ] **Task 90:** Create migration `V20__Create_workers_worker_availability_table.sql`.
    - [ ] Create table `workers_worker_availability` with fields: `id`, `worker_id`, `day_of_week`, `start_time`, `end_time`, `available`, `created_at`, `updated_at`.
    - [ ] Add foreign key to `workers_workers.id`.

- [ ] **Task 91:** Create `WorkerAvailability.java` entity in `workers.domain`.
    - [ ] Use `@ManyToOne` relationship to `Worker`.
    - [ ] Use Lombok annotations.

### Application Core:

- [ ] **Task 92:** Create DTOs in `workers.dto`.
    - [ ] Create `WorkerDto.java` with fields: `id`, `name`, `avatar`, `phone`, `experience`, `rating`, `specializations` (List<String>).
    - [ ] Create `WorkerListRequest.java` with fields: `specialization` (optional), `available` (optional), `page`, `size`.

- [ ] **Task 93:** Create `WorkerMapper.java` interface in `workers.mapper`.
    - [ ] Add method `WorkerDto toDto(Worker worker)`.

- [ ] **Task 94:** Create `WorkerRepository.java` in `workers.repository`.
    - [ ] Extend `JpaRepository<Worker, Long>`.
    - [ ] Add method `Page<Worker> findByActiveTrue(Pageable pageable)`.
    - [ ] Add method `Page<Worker> findByActiveTrueAndSpecializationsContaining(String specialization, Pageable pageable)`.

- [ ] **Task 95:** Implement `WorkerService.java` in `workers.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repository and mapper.
    - [ ] Implement `getAvailableWorkers(WorkerListRequest request)`:
        - [ ] Query workers by filters.
        - [ ] Filter by availability if needed.
        - [ ] Map to DTOs.
        - [ ] Return `Page<WorkerDto>`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.
    - [ ] Implement `getWorkerById(Long id)`:
        - [ ] Find worker by ID.
        - [ ] Map to DTO.
        - [ ] Return `WorkerDto`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.

### API Layer:

- [ ] **Task 96:** Implement `WorkerController.java` in `workers.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/workers")`.
    - [ ] Inject `WorkerService` via `@RequiredArgsConstructor`.
    - [ ] Add `@GetMapping` endpoint:
        - [ ] Accept query params: `specialization`, `available`, `page`, `size`.
        - [ ] Call `workerService.getAvailableWorkers()`.
        - [ ] Return `ResponseEntity<ApiResponse<Page<WorkerDto>>>`.
    - [ ] Add `@GetMapping("/{id}")` endpoint:
        - [ ] Extract `id` from path variable.
        - [ ] Call `workerService.getWorkerById()`.
        - [ ] Return `ResponseEntity<ApiResponse<WorkerDto>>`.

### Quality Assurance:

- [ ] **Task 97:** Write Unit Tests for `WorkerService`.
    - [ ] Test `getAvailableWorkers()` with filters.
    - [ ] Test `getWorkerById()` success scenario.

---

## Module 8: News Module

### Database & Domain Layer:

- [ ] **Task 98:** Create migration `V21__Create_news_articles_table.sql`.
    - [ ] Create table `news_articles` with fields: `id`, `title`, `content`, `summary`, `image_url`, `author`, `published`, `published_at`, `views`, `created_at`, `updated_at`.
    - [ ] Add index on `published`.
    - [ ] Add index on `published_at`.

- [ ] **Task 99:** Create `Article.java` entity in `news.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add auditing fields.

### Application Core:

- [ ] **Task 100:** Create DTOs in `news.dto`.
    - [ ] Create `ArticleDto.java` with fields: `id`, `title`, `content`, `summary`, `imageUrl`, `author`, `publishedAt`, `views`.
    - [ ] Create `ArticleListRequest.java` with fields: `page`, `size`, `sortBy`, `sortDir`.

- [ ] **Task 101:** Create `NewsMapper.java` interface in `news.mapper`.
    - [ ] Add method `ArticleDto toDto(Article article)`.

- [ ] **Task 102:** Create `ArticleRepository.java` in `news.repository`.
    - [ ] Extend `JpaRepository<Article, Long>`.
    - [ ] Add method `Page<Article> findByPublishedTrueOrderByPublishedAtDesc(Pageable pageable)`.

- [ ] **Task 103:** Implement `NewsService.java` in `news.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repository and mapper.
    - [ ] Implement `getArticles(ArticleListRequest request)`:
        - [ ] Query published articles.
        - [ ] Map to DTOs.
        - [ ] Return `Page<ArticleDto>`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.
    - [ ] Implement `getArticleById(Long id)`:
        - [ ] Find article by ID.
        - [ ] Increment views.
        - [ ] Map to DTO.
        - [ ] Return `ArticleDto`.
        - [ ] Annotate with `@Transactional`.

### API Layer:

- [ ] **Task 104:** Implement `NewsController.java` in `news.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/news")`.
    - [ ] Inject `NewsService` via `@RequiredArgsConstructor`.
    - [ ] Add `@GetMapping` endpoint:
        - [ ] Accept query params: `page`, `size`, `sortBy`, `sortDir`.
        - [ ] Call `newsService.getArticles()`.
        - [ ] Return `ResponseEntity<ApiResponse<Page<ArticleDto>>>`.
    - [ ] Add `@GetMapping("/{id}")` endpoint:
        - [ ] Extract `id` from path variable.
        - [ ] Call `newsService.getArticleById()`.
        - [ ] Return `ResponseEntity<ApiResponse<ArticleDto>>`.

### Quality Assurance:

- [ ] **Task 105:** Write Unit Tests for `NewsService`.
    - [ ] Test `getArticles()` pagination.
    - [ ] Test `getArticleById()` increments views.

---

## Module 9: Contact Module

### Database & Domain Layer:

- [ ] **Task 106:** Create migration `V22__Create_contact_messages_table.sql`.
    - [ ] Create table `contact_messages` with fields: `id`, `name`, `email`, `phone`, `subject`, `message`, `status` (enum: NEW, READ, REPLIED), `created_at`, `updated_at`.
    - [ ] Add index on `status`.

- [ ] **Task 107:** Create `ContactMessage.java` entity in `contact.domain`.
    - [ ] Use Lombok annotations.
    - [ ] Add auditing fields.
    - [ ] Add validation annotations.

### Application Core:

- [ ] **Task 108:** Create DTOs in `contact.dto`.
    - [ ] Create `ContactRequest.java` with fields: `name`, `email`, `phone`, `subject`, `message`.
        - [ ] Add Jakarta Validation (`@NotBlank`, `@Email`, `@Size`).
    - [ ] Create `ContactMessageDto.java` with fields: `id`, `name`, `email`, `phone`, `subject`, `message`, `status`, `createdAt`.

- [ ] **Task 109:** Create `ContactMapper.java` interface in `contact.mapper`.
    - [ ] Add method `ContactMessage toEntity(ContactRequest request)`.
    - [ ] Add method `ContactMessageDto toDto(ContactMessage message)`.

- [ ] **Task 110:** Create `ContactMessageRepository.java` in `contact.repository`.
    - [ ] Extend `JpaRepository<ContactMessage, Long>`.

- [ ] **Task 111:** Implement `ContactService.java` in `contact.application`.
    - [ ] Annotate with `@Service` and `@RequiredArgsConstructor`.
    - [ ] Inject repository and mapper.
    - [ ] Implement `submitContactMessage(ContactRequest request)`:
        - [ ] Map request to entity.
        - [ ] Set status to NEW.
        - [ ] Save message.
        - [ ] Return `ContactMessageDto`.
        - [ ] Annotate with `@Transactional`.
    - [ ] Implement `getContactMessages(Pageable pageable)` (admin only):
        - [ ] Query all messages.
        - [ ] Map to DTOs.
        - [ ] Return `Page<ContactMessageDto>`.
        - [ ] Annotate with `@Transactional(readOnly = true)`.

### API Layer:

- [ ] **Task 112:** Implement `ContactController.java` in `contact.api`.
    - [ ] Use `@RestController`, `@RequestMapping("/api/contact")`.
    - [ ] Inject `ContactService` via `@RequiredArgsConstructor`.
    - [ ] Add `@PostMapping` endpoint:
        - [ ] Accept `@RequestBody @Valid ContactRequest`.
        - [ ] Call `contactService.submitContactMessage()`.
        - [ ] Return `ResponseEntity<ApiResponse<ContactMessageDto>>`.
    - [ ] Add `@GetMapping` endpoint (admin only):
        - [ ] Accept query params: `page`, `size`.
        - [ ] Call `contactService.getContactMessages()`.
        - [ ] Return `ResponseEntity<ApiResponse<Page<ContactMessageDto>>>`.

### Quality Assurance:

- [ ] **Task 113:** Write Unit Tests for `ContactService`.
    - [ ] Test `submitContactMessage()` success scenario.

---

## Shared Infrastructure

### Global Exception Handling:

- [ ] **Task 114:** Create `GlobalExceptionHandler.java` in `shared.exception`.
    - [ ] Use `@RestControllerAdvice`.
    - [ ] Handle `ResourceNotFoundException` → 404.
    - [ ] Handle `ValidationException` → 400.
    - [ ] Handle `AuthenticationException` → 401.
    - [ ] Handle `AccessDeniedException` → 403.
    - [ ] Handle `MethodArgumentNotValidException` → 400.
    - [ ] Handle generic `Exception` → 500.
    - [ ] Return unified `ApiResponse` format.

### API Response Wrapper:

- [ ] **Task 115:** Create `ApiResponse.java` in `shared.dto`.
    - [ ] Use Lombok `@Data`, `@Builder`.
    - [ ] Fields: `success` (Boolean), `data` (T), `error` (String), `message` (String).
    - [ ] Add static factory methods: `success()`, `error()`.

### Application Service Interfaces (Cross-Module Communication):

- [ ] **Task 116:** Create `ServiceApplicationService.java` interface in `services.application`.
    - [ ] Define method `ServiceDto getServiceById(Long id)`.
    - [ ] Implement in `ServiceService` class.

- [ ] **Task 117:** Create `OrderApplicationService.java` interface in `orders.application`.
    - [ ] Define method `OrderDto getOrderById(Long id)`.
    - [ ] Implement in `OrderService` class.

- [ ] **Task 118:** Create `AuthApplicationService.java` interface in `auth.application`.
    - [ ] Define method `UserDto getUserById(Long id)`.
    - [ ] Implement in `AuthService` class.

### Configuration:

- [ ] **Task 119:** Create `ApplicationConfig.java` in `shared.config`.
    - [ ] Configure `ObjectMapper` bean (Jackson).
    - [ ] Configure `RestTemplate` or `WebClient` bean (if needed).
    - [ ] Configure CORS (if needed).

- [ ] **Task 120:** Create `JpaAuditingConfig.java` in `shared.config`.
    - [ ] Enable JPA auditing with `@EnableJpaAuditing`.

### Swagger/OpenAPI Documentation:

- [ ] **Task 121:** Configure Swagger/OpenAPI in `shared.config`.
    - [ ] Add dependency `springdoc-openapi-starter-webmvc-ui`.
    - [ ] Create `OpenApiConfig.java`.
    - [ ] Configure API info, security schemes (JWT).
    - [ ] Add `@Operation`, `@ApiResponse` annotations to controllers.

---

## Integration Tasks

### Order Completion → Loyalty Points:

- [ ] **Task 122:** Integrate loyalty points earning on order completion.
    - [ ] In `OrderService.updateOrderStatus()`, when status changes to COMPLETED:
        - [ ] Call `LoyaltyApplicationService.earnPoints()`.
        - [ ] Calculate points based on order total (e.g., 1 point per 10,000 VND).
    - [ ] Create `LoyaltyApplicationService` interface in `loyalty.application`.
    - [ ] Implement in `LoyaltyService`.

### Order Review → Service Rating Update:

- [ ] **Task 123:** Integrate review submission to update service rating.
    - [ ] In `ReviewService.createReview()`, after saving review:
        - [ ] Call `ServiceApplicationService.updateServiceRating()`.
        - [ ] Recalculate average rating for service.
    - [ ] Add `updateServiceRating()` method to `ServiceService`.

### Worker Assignment → Order Status Update:

- [ ] **Task 124:** Integrate worker assignment in order creation.
    - [ ] In `OrderService.createOrder()`, if worker is selected:
        - [ ] Create technician assignment.
        - [ ] Update order status to PROCESSING.
    - [ ] Call `WorkerApplicationService.getWorkerById()` to validate worker exists.

---

## Testing & Documentation

### Integration Tests:

- [ ] **Task 125:** Write Integration Tests for Order Flow.
    - [ ] Test: Create Order → Assign Worker → Update Status → Complete → Generate Invoice.
    - [ ] Use `@SpringBootTest` with test database.

- [ ] **Task 126:** Write Integration Tests for Review Flow.
    - [ ] Test: Complete Order → Submit Review → Verify Service Rating Updated.

### API Documentation:

- [ ] **Task 127:** Verify all endpoints are documented in Swagger.
    - [ ] Check all controllers have proper annotations.
    - [ ] Verify request/response examples.

### Performance Testing:

- [ ] **Task 128:** Add pagination to all list endpoints.
    - [ ] Verify default page size is reasonable (e.g., 20).
    - [ ] Add maximum page size limit.

---

## Summary

**Total Tasks:** 128  
**Estimated Timeline:** 16-20 weeks (assuming 1 developer, 0.5-1 day per task)

**Module Breakdown:**
- Auth Module: 16 tasks
- Services Module: 17 tasks
- Orders Module: 22 tasks
- Reviews Module: 10 tasks
- Profile Module: 8 tasks
- Loyalty Module: 14 tasks
- Workers Module: 10 tasks
- News Module: 8 tasks
- Contact Module: 8 tasks
- Shared Infrastructure: 8 tasks
- Integration: 3 tasks
- Testing & Documentation: 4 tasks

**Priority Order:**
1. Auth Module (Foundation)
2. Services Module (Core Business)
3. Orders Module (Core Business)
4. Profile Module (User Management)
5. Reviews Module (Order Completion Flow)
6. Loyalty Module (Business Logic)
7. Workers Module (Order Assignment)
8. News Module (Content)
9. Contact Module (Support)
10. Shared Infrastructure (Throughout)
11. Integration Tasks (After Core Modules)
12. Testing & Documentation (Final Phase)

