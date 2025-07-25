```mermaid
classDiagram
    class Authentication {
        -Long userId
        -Role role
        +authorize(id)
        +isUser() bool
        +isAnonyMous() bool
    }
    Authentication --o Role

    class Role {
        <<enumeration>>
        USER
        ANONYMOUS
    }
```


```mermaid
classDiagram
    class User {
        -Long id
        -String username
        -Gender gender
        -LocalDate birthDate
        -String email
    }
    class Gender {
        <<enumeration>>
        F
        M
    }
    class UserPoint {
        -Long id
        -Long userId
        -BigDecimal point
        +charge(BigDecimal target) UserPointHistory
        +substract(BigDecimal target) UserPointHistory
    }
    UserPoint ..> UserPointHistory
    
    class UserPointHistory {
        -Long id
        -Long userId
        -BigDecimal targetValue
        -BigDecimal resultValue
        -BigDecimal previousValue
        -UserPointMethod method
    }
    UserPointHistory --o UserPointMethod
    
    class UserPointMethod {
        <<enumeration>>
        CHARGE
        SUBSTRACT
    }
    User --o Gender
```
```mermaid
classDiagram
    class Brand {
        -Long id
        -String name
    }
```


```mermaid
classDiagram
    class Product {
        -Long id
        -BrandInfo brand
        -String name
        -BigDecimal price
        -ProductStatus status
        -LocalDateTime createdAt
        -Set~ProductLike~ likes
        +addLike(Authentication authentication) LikeHistory
        +removeLike(Authentication authentication) LikeHistory
    }
    class BrandInfo {
        -Long id
        -String name
    }
    Product --o BrandInfo
    Product ..> LikeHistory
    Product ..> Authentication

    
    class LikeHistory {
        -Long id
        -Long userId
        -LikeMethod method
        -LikeTarget target
        -LocalDateTime createdAt
    }
    LikeHistory --* LikeTarget
    class LikeTarget {
        -Long targetId
        -LikeTargetType targetType
    }
    LikeTarget --o LikeTargetType
    LikeHistory --o LikeMethod
    class LikeTargetType {
        <<enumeration>>
        Product
        Brand
    }
    
    class LikeMethod {
        <<enumeration>>
        ADD
        REMOVE
    }
    
    Product --* ProductLike
    Product --o ProductStatus
    class ProductLike {
        -Long id
        -Long productId
        -Long userId
    }

    class ProductStatus {
        <<enumeration>>
        SOLD_OUT
        NORMAL
    }
```
```mermaid
classDiagram
    class Order {
        -Long id
        -Long userId
        -LocalDateTime createdAt
        -List~OrderLine~ lines
        +getTotalPrice() BigDecimal
        +pay(Authentication authentication, UserPoint point) OrderPayment
    }
    Order ..> Authentication
    Order ..> UserPoint
    Order ..> OrderPayment

    Order --* OrderLine
    class OrderLine {
        -Long orderId
        -LineItem lineItem
        +getPrice() BigDecimal
    }

    OrderLine --o LineItem


    class LineItem {
        -Long productId
        -BrandInfo brand
        -String name
        -BigDecimal price
        -LocalDateTime createdAt
        +getPrice() BigDecimal
    }

```
    
```mermaid
classDiagram
    class Payment {
        -Long id
        -Long userId
        -BigDecimal price
    }

    Payment o-- OrderPayment
    OrderPayment --o PaymentMethod
    class OrderPayment {
        -Long id
        -Long orderId
        -Payment payment
        -PaymentMethod method
    }
    class PaymentMethod {
        <<enumeration>>
        POINT
    }
```