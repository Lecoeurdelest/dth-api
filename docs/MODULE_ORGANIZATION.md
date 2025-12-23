# Module Organization Guide

This document describes the standardized structure for organizing modules in this Modular Monolith application.

## 📁 Standard Module Structure

Every module MUST follow this exact structure:

```
{module}/
├── api/                          # REST Controllers (Public Boundary)
│   └── {Module}Controller.java
│
├── application/                   # Business Logic Layer
│   ├── {Module}ApplicationService.java  # Public Interface (for cross-module communication)
│   └── {Module}ServiceImpl.java         # Implementation (internal)
│
├── domain/                       # Domain Entities (PRIVATE)
│   └── {Entity}.java
│
├── repository/                   # JPA Repositories (PRIVATE)
│   └── {Entity}Repository.java
│
├── dto/                          # Data Transfer Objects
│   ├── {Request}Request.java
│   └── {Response}Dto.java
│
├── mapper/                       # MapStruct Mappers
│   └── {Module}Mapper.java
│
├── config/                       # Module Configuration (optional)
│   └── {Module}Config.java
│
└── package-info.java            # Module documentation
```

## 🔒 Module Boundary Rules

### ✅ ALLOWED

1. **Import from Shared Kernel**
   ```java
   import com.example.app.shared.exception.ResourceNotFoundException;
   import com.example.app.shared.response.ApiResponse;
   ```

2. **Use Application Service Interfaces from Other Modules**
   ```java
   // In OrderService
   @Autowired
   private AuthApplicationService authApplicationService;
   ```

3. **Use DTOs from Other Modules**
   ```java
   import com.example.app.auth.dto.UserDto;
   ```

### ❌ FORBIDDEN

1. **Import Entities from Other Modules**
   ```java
   // ❌ WRONG
   import com.example.app.auth.domain.User;
   ```

2. **Import Repositories from Other Modules**
   ```java
   // ❌ WRONG
   import com.example.app.auth.repository.UserRepository;
   ```

3. **Direct Database Queries Across Modules**
   ```java
   // ❌ WRONG
   @Query("SELECT u FROM User u WHERE u.id = :id")
   ```

4. **Import Internal Implementation Classes**
   ```java
   // ❌ WRONG
   import com.example.app.auth.application.AuthService;
   // ✅ CORRECT
   import com.example.app.auth.application.AuthApplicationService;
   ```

## 🔄 Cross-Module Communication Pattern

### Pattern 1: Module A needs data from Module B

```
Module A (Orders)                    Module B (Auth)
-----------                          -----------
OrderService                         AuthApplicationService (interface)
    |                                        |
    | calls via interface                   |
    |-------------------------------------->|
    |                                        |
    | returns UserDto                       |
    |<--------------------------------------|
    |                                        |
    | uses UserDto                          |
```

**Example:**
```java
// In OrderService
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderApplicationService {
    
    private final AuthApplicationService authApplicationService; // ✅ Interface
    
    public OrderDto getOrderWithUser(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(...);
        UserDto user = authApplicationService.getUserById(order.getUserId()); // ✅ Via interface
        return orderMapper.toDto(order, user);
    }
}
```

### Pattern 2: Module A triggers action in Module B

```
Module A (Orders)                    Module B (Loyalty)
-----------                          -----------
OrderService                         LoyaltyApplicationService (interface)
    |                                        |
    | when order completed                   |
    |-------------------------------------->|
    | earnPoints(userId, orderId, points)    |
    |                                        |
```

**Example:**
```java
// In OrderService
@Transactional
public void completeOrder(Long orderId) {
    Order order = orderRepository.findById(orderId).orElseThrow(...);
    order.setStatus(OrderStatus.COMPLETED);
    orderRepository.save(order);
    
    // Trigger loyalty points earning
    int points = calculatePoints(order.getTotalAmount());
    loyaltyApplicationService.earnPoints(
        order.getUserId(), 
        orderId, 
        points, 
        "Order completion"
    );
}
```

## 📦 Module Responsibilities

### Auth Module
- **Public Interface:** `AuthApplicationService`
- **Public Methods:**
  - `getUserById(Long userId) -> UserDto`
  - `userExists(Long userId) -> boolean`
- **Private:** User, RefreshToken entities, UserRepository

### Services Module
- **Public Interface:** `ServiceApplicationService`
- **Public Methods:**
  - `getServiceById(Long id) -> ServiceDto`
  - `getAllServices(...) -> Page<ServiceDto>`
  - `updateServiceRating(...) -> void`
- **Private:** Service entity, ServiceRepository

### Orders Module
- **Public Interface:** `OrderApplicationService`
- **Public Methods:**
  - `getOrderById(Long orderId) -> OrderDto`
  - `isOrderCompleted(Long orderId) -> boolean`
- **Private:** Order, OrderReview entities, OrderRepository

### Loyalty Module
- **Public Interface:** `LoyaltyApplicationService`
- **Public Methods:**
  - `earnPoints(...) -> void`
  - `confirmPointsTransaction(...) -> void`
- **Private:** LoyaltyPoints, PointsTransaction entities

## 🏗️ Shared Kernel Structure

```
shared/
├── config/              # Global Spring configuration
├── security/            # JWT, Spring Security
├── exception/           # Global exception handling
├── response/            # API response wrapper
└── util/                # Utility classes
```

**Rules:**
- ✅ Can be imported by any module
- ❌ Should NOT import from modules
- ❌ Should NOT contain business logic

## 📝 Creating a New Module

### Step 1: Create Module Structure

```bash
mkdir -p src/main/java/com/example/app/{module}/{api,application,domain,repository,dto,mapper,config}
```

### Step 2: Create Domain Entity

```java
// {module}/domain/{Entity}.java
@Entity
@Table(name = "{module}_{tablename}")
public class Entity {
    // ...
}
```

### Step 3: Create Repository

```java
// {module}/repository/{Entity}Repository.java
@Repository
public interface EntityRepository extends JpaRepository<Entity, Long> {
    // Module-private methods
}
```

### Step 4: Create DTOs

```java
// {module}/dto/{Request}Request.java
@Data
public class CreateEntityRequest {
    // Validation annotations
}
```

### Step 5: Create Application Service Interface

```java
// {module}/application/{Module}ApplicationService.java
public interface ModuleApplicationService {
    // Public methods for cross-module communication
    EntityDto getEntityById(Long id);
}
```

### Step 6: Create Implementation

```java
// {module}/application/{Module}ServiceImpl.java
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleApplicationService {
    private final EntityRepository repository;
    // Implementation
}
```

### Step 7: Create Controller

```java
// {module}/api/{Module}Controller.java
@RestController
@RequestMapping("/{module}")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleApplicationService service;
    // Endpoints
}
```

### Step 8: Create package-info.java

```java
/**
 * {Module} Module
 * 
 * <p>Module description and rules.
 */
package com.example.app.{module};
```

## 🔍 Verification Checklist

When creating or modifying a module, verify:

- [ ] Module has `package-info.java` documenting boundaries
- [ ] Application service interface exists for cross-module communication
- [ ] Implementation class implements the interface
- [ ] No entity imports from other modules
- [ ] No repository imports from other modules
- [ ] All cross-module communication uses interfaces
- [ ] DTOs are used for data transfer
- [ ] Module follows standard directory structure

## 📚 Examples

### ✅ Correct Module Communication

```java
// OrderService needs user info
@Service
public class OrderServiceImpl {
    private final AuthApplicationService authService; // ✅ Interface
    
    public OrderDto getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(...);
        UserDto user = authService.getUserById(order.getUserId()); // ✅ Via interface
        return mapper.toDto(order, user);
    }
}
```

### ❌ Incorrect Module Communication

```java
// ❌ WRONG - Direct entity import
import com.example.app.auth.domain.User;

@Service
public class OrderServiceImpl {
    private final UserRepository userRepository; // ❌ Repository from other module
    
    public OrderDto getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(...);
        User user = userRepository.findById(order.getUserId()).orElseThrow(...); // ❌ Direct access
        return mapper.toDto(order, user);
    }
}
```

## 🎯 Benefits of This Structure

1. **Clear Boundaries:** Easy to see what's public vs private
2. **Testability:** Interfaces can be easily mocked
3. **Flexibility:** Can extract modules to microservices later
4. **Maintainability:** Changes in one module don't break others
5. **Scalability:** Teams can work on modules independently

---

**Remember:** The application service interface is the **ONLY** way modules should communicate with each other.

