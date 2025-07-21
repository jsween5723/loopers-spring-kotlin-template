# 주문 요구사항
주문은 상품, 수량 목록을 가진다.

## 주문 생성
사용자는 상품과 수량을 지정해 주문을 생성할 수 있다.

### 파라미터

#### body

{products: [{productId: number, quantity: number}]}

#### 파라미터 별 조건
1. products
    1. 비어있을 수 없다.
2. quantity
    1. 1보다 작을 수 없다.
    2. 현재 stockNumber 보다 클 수 없다.
3. productId
    1. 존재하는 상품이어야한다.

### 요구사항
- [ ] products가 비어있을 경우 400 BAD REQUEST를 반환한다.
- [ ] quantity가 1보다 작을 경우 400 BAD REQUEST를 반환한다.
    - [ ] 도메인 레이어 검증 실패 시 IllegalArgumentException을 반환한다.
- [ ] quantity가 현재 상품의 stockNumber보다 작을 경우 409 CONFLICT를 반환한다.
    - [ ] 도메인 레이어 검증 실패 시 IllegalStateException을 반환한다.
- [ ] productId에 해당하는 상품이 없을 경우 404 NOT FOUND를 반환한다.
    - [ ] 도메인 레이어 검증 실패 시 IllegalStateException을 반환한다.
- [ ] 성공 시 200 OK와 주문 id를 반환한다





## 내 주문 목록 조회
내가 생성한 주문 목록을 조회합니다.  
주문은 주문 id 상품과 수량 목록, 총 가격, 상태가 포함된다.  

### 파라미터

#### 헤더
1. X-USER-ID

#### 쿼리스트링
1. status: PENDING, COMPLETED
2. sort: created_at(default) / totalPrice
3. page: 1(default)
4. size: 20(default)

#### 파라미터 별 조건
쿼리스트링은 비어있을 수 있다.  
1. X-USER-ID: 존재하는 사용자여야한다.
2. page: 1이상이어야 한다.
3. size: 1이상이어야 한다.


### 요구사항
- [ ] X-USER-ID가 비어있을 경우 403 FORBIDDEN을 반환한다.
    - [ ] ANONYMOUS일 경우 AccessControlException을 던진다.
- [ ] X-USER-ID가 존재하지 않는 사용자일 경우 404 NOT FOUND를 반환한다.
    - [ ] 도메인 레이어 검증 실패시 EntityNotFoundException을 던진다.
- [ ] page가 1보다 작을 경우 400 BAD REQUEST를 던진다.
    - 