# 예외처리
예외가 던져질 경우 일괄적으로 처리된다  
예외로 필터에서는 직접 명시한다
```mermaid
sequenceDiagram
    autonumber
    participant P as 클라이언트
    participant A as RestControllerAdvice
    participant C as SomeThing
    alt IllegalArgumentException
        C -->> A: throw IllegalArgumentException
        A -->> P: 400 BAD REQUEST
    end
    alt IllegalStateException
        C -->> A: throw IllegalStateException
        A -->> P: 409 CONFLICT
    end
    alt EntityNotFoundException 
        C-->> A: throw EntityNotFoundException
        A-->> P: 404 NOT FOUND
    end
    alt AccessControlException
        C -->> A: throw AccessControlException
        A -->> P: 403 FORBIDDEN
    end
```

# 인증, 인가
인증은 모든 시퀀스에 포함된다.

## 인증 과정
```mermaid
sequenceDiagram
    autonumber
    participant P as 클라이언트
    participant AF as AuthenticationFilter
    participant US as UserService
    participant AR as ThreadLocal
    P ->>+ AF : X-USER-ID
    alt 헤더 없음
        AF->>+AR: Authentication(role=ANONYMOUS, id=-1L)
    else
        alt 식별 실패
            AF -->> P : 401 UNAUTHORIZED
        end
        AF ->>+ US : 사용자 존재 유무 검증
        alt 사용자 없음
            US-->>-AF : throw EntityNotFoundException
            AF-->>P : 401 UNAUTHORIZED
        end
        AF ->> AR: Authentication(role=USER, id)
    end
    deactivate AF
    
```

## 인가 과정
모든 사용자라고 명시돼있다면 생략한다.  
****로그인한 사용자****라고 명시돼 있다면 authorize(USER)를 활용한다.  
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant TL as Controller
    participant a as Authorizer
    C ->>+ TL: 요청
    TL ->>+ a: authorize(USER, ANONYMOUS)(Authentication)
    alt Authentication에 해당 Role이 포함되지 않을 경우
        a -->>- TL: throw AccessControlException
    end
    TL -->>- C: 2xx status
```
---
# 브랜드

## 브랜드 목록 조회
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant BC as BrandController
    participant BQS as BrandService
    C ->>+ BC: GET /api/v1/brands?name=
    BC ->>+ BQS: search(BrandQuery)
    BQS -->>- BC: 브랜드 목록
    BC -->>- C: 브랜드 목록
```

## 브랜드 상세 조회
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant BC as BrandController
    participant BF as BrandFacade
    participant BQS as BrandService
    participant PS as ProductService
    C ->>+ BC: GET /api/v1/brands/:id
    BC ->>+ BF: 브랜드 조회
    BF ->>+ BQS: 브랜드 조회
    alt 브랜드가 존재하지 않을 때 
        BQS -->> BF: throw EntityNotFoundException
    end
    BQS -->>- BF: 브랜드 정보
    BF ->>+ PS: brandId 상품 목록 조회
    PS -->>- BF: 상품 목록
    BF -->>- BC: 브랜드 + 상품 목록
    BC -->>- C: 브랜드+ 상품 목록
```

---
# 상품
### 모든 사용자는 전체 상품 목록을 볼 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant PC as ProductController
    participant PQS as ProductService
    C ->>+ PC: GET /api/v1/products?brandId=&name=&sort=
    PC ->>+ PQS: search(Authentication, ProductQuery)
    alt **로그인한 사용자**인 경우
        PQS ->> PQS: isLiked 반영
    end
    PQS -->>- PC: 상품 정보 목록
    PC -->>- C: 상품 정보 목록
```

### 모든 사용자는 상품 식별자를 통해 상품 상세 정보를 볼 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant PC as ProductController
    participant PQS as ProductService
    C ->>+ PC: GET /api/v1/products/:id
    PC ->>+ PQS: findBy(id)
    alt 존재하지 않는 상품인 경우 
        PQS -->> PC: throw EntityNotFoundException 
    end
    PQS -->>- PC: 상품 상세 정보
    PC -->>- C: 상품 상세 정보
```

### **로그인한 사용자**는 자신이 좋아요한 상품 목록을 조회할 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant PC as ProductController
    participant PQS as ProductService
    C ->>+ PC: GET /api/v1/products/likes/me?brandId=&name=&sort=
    PC ->>+ PQS: search(Authentication, ProductLikeQuery)
    PQS ->> PQS: isLiked 반영
    PQS -->>- PC: 상품 정보 목록
    PC -->>- C: 상품 정보 목록
```

### **로그인한 사용자**는 상품 식별자를 통해 상품에 좋아요를 추가할 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant PC as ProductController
    participant PS as ProductService
    participant PR as ProductRepository
    C ->>+ PC: POST /api/v1/products/:id/likes
    PC ->>+ PS: addLike(Authentication, productId)
    PS ->>+ PR: 상품 가져오기
    alt 상품이 존재하지 않을 경우 
        PS -->> PC: throw EntityNotFoundException
    end
    PR -->>- PS: 상품정보
    alt **로그인한 사용자**의 좋아요가 존재하지 않을 경우
        PS ->> PR: 좋아요 insert
    end
    PS -->>- PC: void
    PC -->>- C: 204 NO CONTENT
```

### **로그인한 사용자**는 상품 식별자를 통해 상품에 좋아요를 취소할 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant PC as ProductController
    participant PS as ProductService
    participant PR as ProductRepository
    C ->>+ PC: DELETE /api/v1/products/:id/likes
    PC ->>+ PS: removeLike(Authentication, productId)
    alt 상품이 존재하지 않을 경우 
        PS -->> PC: throw EntityNotFoundException
    end
    alt 좋아요가 존재할 경우
        PS ->> PR: 좋아요 delete
    end
    PS -->>- PC: void
    PC -->>- C: 204 NO CONTENT
```

---
# 주문
### **로그인한 사용자**는 SKU 식별자와 수량목록을 입력해 주문을 생성할 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant OC as OrderController
    participant OF as OrderFacade
    participant PRCS as ProductService
    participant OCS as OrderdService
    participant PCS as PaymentService
    C ->>+ OC: POST /api/v1/orders
    OC ->>+ OF: 주문 생성
    OF ->>+ PRCS: LineItem 목록 조회
    alt 존재하지 않는 상품이 있을 경우
        PRCS -->> OF: throw EntityNotFoundException
    end
    alt 재고 부족, 판매 불가 상태인 SKU가 있을 경우 
        PRCS -->> OF: throw IllegalStateException
    end
    PRCS -->>- OF: LineItem 목록
    OF ->>+ OCS: 주문 생성 (결제 대기중)
    OCS -->>- OF: 주문 정보
    OF ->>+ PCS: 결제 생성 (대기중)
    PCS -->>- OF: 결제 정보
    OF -->>- OC: 주문 및 결제 정보
    OC -->>- C: 주문 및 결제 정보
```

### **로그인한 사용자**는 자신의 주문 목록을 조회할 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant OC as OrderController
    participant OF as OrderFacade
    participant OQS as OrderService
    participant PQS as PaymentService
    
    C ->>+ OC: GET /api/v1/orders/me?status=&sort=&page=&limit=
    OC ->>+ OF: search(OrderQuery)
    OF ->>+ OQS: 주문 정보 목록 조회
    OQS -->>- OF: 주문 정보 목록
    OF ->>+ PQS: 결제 정보 목록 조회
    PQS -->>- OF: 결제 정보 목록
    OF -->>- OC: 주문 및 결제 정보 목록
    OC -->>- C: 주문 및 결제 정보 목록

```

### **로그인한 사용자**는 주문 생성 후 포인트 결제를 진행할 수 있다.
```mermaid
sequenceDiagram
    autonumber
    participant C as 클라이언트
    participant PC as PaymentController
    participant PF as PaymentFacade
    participant PQS as PaymentService
    participant OCS as OrderService
    participant UPCS as UserPointService
    participant PRCS as ProductService
    participant ES as ExternalService
    C ->>+ PC: PATCH /api/v1/payments/:id  {method: "POINT"}
    PC ->>+ PF: 지불하기
    PF ->>+ PQS: 결제정보 조회
    alt 결제 정보 없음 
        PQS -->> PF: throw EntityNotFoundException
    end
    PQS -->>- PF: 결제정보
    PF ->>+ OCS: 주문 금액 검증
    alt 주문 정보 없음 
        OCS -->> PF: throw EntityNotFoundException
    end
    alt 금액 미일치 
        OCS -->> PF: throw IllegalStateException
    end
    OCS -->>- PF: 주문 정보
    PF ->>+ UPCS: 포인트 차감
    alt 포인트 부족 
        UPCS -->> PF: throw IllegalStateException
    end
    UPCS -->>- PF: 포인트 증감 정보
    PF ->>+ PRCS: 재고 차감
    alt 재고 부족 
        PRCS -->>- PF: throw IllegalStateException
    end
    PF ->>+ PQS: 결제정보 상태 변경 (완료)
    PQS -->>- PF: 결제정보
    PF ->>+ OCS: 주문 상태 변경 (결제 완료)
    OCS -->>- PF: 주문정보
    PF -) ES: 정보 전송
    PF -->>- PC: 결제 정보
    PC -->>- C: 결제 정보
```