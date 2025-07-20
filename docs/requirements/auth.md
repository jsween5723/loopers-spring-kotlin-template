# 인증 인가 요구사항

## 인증
인증은 HTTP 헤더의 `X-USER-ID` 항목(aka 인증헤더)으로 진행합니다.    
인증 헤더는 사용자 식별자를 저장합니다.  

## 인가
각 사용자는 USER, ANONYMOUS 역할을 가집니다.  
인증헤더가 있을 경우 USER, 없을 경우 ANONYMOUS 역할을 부여받습니다.    

### 인증

#### AuthorizationFilter
인증은 OncePerRequest 필터를 구현하는 AuthorizationFilter에서 수행합니다.  
- 인증 헤더가 없을 경우 userId = -1L, roles = {ANONYMOUS} Authentication 인스턴스를 스레드로컬에 저장합니다. 
- 인증 헤더 식별에 성공할 경우 userId = {X-USER-ID}, roles = {USER} Authentication 인스턴스를 스레드로컬에 저장합니다.
- 인증 헤더 식별 (타입 불일치 등)에 실패할 경우 401 UNAUTHORIZED 상태코드를 반환합니다.  

### 인가

#### ArgumentResolver
- 인가는 DispatcherServlet에서 ArgumentResolver를 통해 수행합니다.  
- 컨트롤러 메소드 인자에 Authentication 파라미터가 있다면 스레드로컬의 Authentication이 ANONYMOUS라면 403 FORBIDDEN 예외를 던집니다.  
- 해당 파라미터에 @CanAnonymous 어노테이션이 달려있다면 스레드로컬의 Authentication이 ANONYMOUS 여도 통과합니다.
