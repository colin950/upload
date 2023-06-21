## 사용 라이브러리
- JDK 17
- Spring Framework 3.0.2
- Spring WebMVC
- H2
    - local에서만 테스트 용도로 사용하기 위해 해당 인메모리DB 사용
        - auto-ddl : create-drop
- Spring Security
    - /v1/calculate에 대해 ignore 처리 그 밖의 경우에는 에러 발생 (인증 정보가 없기 때문)

- HTTP 클라이언트: spring webclient
    - restTemplate 개발 중단으로 해당 라이브러리 사용
    - 동기, 비동기 방식 둘다 가능 (.block())

## Project 구조
- src/main/.../base > 해당 서비스의 controller, service, dto 구성
- src/main/.../audit > 공통으로 사용하는 dateEntity 구현
- src/main/.../exception > custom exception 사용
- src/main/.../push > 알림 기능을 위해 외부 서비스 호출 모듈, 외부 client는 따로 분리 하여 사용
- src/main/.../repository > querydsl, jpa 모듈 
- src/main/.../component > scheduler component, 부가 기능들(user정보 셋팅등 ...)
- ...

## DB
- user_info (유저 정보 테이블)
- calculate_info (정산 요청 관련 요청자에 대한 테이블)
- calculate_detail (calculate_info의 id 값을 바탕으로 송금자에 대한 정보 테이블)
- transfer_hist (송금건 관련 이력 테이블)
  - 해당 테이블로 동시성 이슈도 해결
- wallet (지갑)

## API
### 테스트 방법
- 따로 profile을 설정하지 않고 서버 실행 (기본 profile 테스트용)
- 기본적으로 X-USER-ID에 대해 Filter에서 UserInfo에 default값 1로 셋팅하여 사용중
  - 다른 유저로 테스트를 진행할 경우 헤더에 X-USER-ID 값 지정 필요
- 실제 동시성 처리를 위해 unique 키만 entity에 작성
--------------
### 정산 요청 API :
- proxy패턴으로 인해 적용이 되지 않은 transaction을 적용하기 위해 ApplicationContext 사용
- curl --location --request POST 'localhost:8201/v1/calculate/request' \
  --header 'Content-Type: application/json' \
  --data-raw '{"title": "title2","message": "message2","amount": "100","calculate_type": "01",
  "data": [{"user_id": "3","amount": "50"},{"user_id": "4","amount": "50"}]}'
--------------
### 정산 송금 API :
- 동시성 이슈 해결을 위해여 TransferHist(전송 이력 테이블) 테이블 이용
  - 해당 테이블의 정산건에 대해 unique를 잡아 두고 같은 값으로 요청시에는 방지
    - 사전에 validation 먼저 체크
- 정산 요청 건에 대해 마지막건이 처리 될경우 원장에 완료 처리
- curl --location --request POST 'localhost:8201/v1/calculate/transfer' \
  --header 'Content-Type: application/json' \
  --data-raw '{"calculate_detail_id": 1 }'
--------------
### 정산 요청 확인 API :
- 테이블 간의 연관을 위해 querydsl을 사용하여 dto로 컨버팅해서 응답
  - curl --location --request GET 'localhost:8201/v1/calculate/request/1'
--------------
### 정산 요청 전체 리스트 확인 API:
  - curl --location --request GET 'localhost:8201/v1/calculate/request/list'
--------------
### 본인의 정산 요청 받은 전체 리스트 확인 API
  - curl --location --request GET 'localhost:8201/v1/calculate/receiver/list' \
    --header 'X-USER-ID: 3'
--------------
### BatchService 
  - multi node로 구성 대비하여 redis 분산 락 처리 가능
  - 미정산 리마인드 알림을 위해 fixedDelay 20분 설정 및 config값을 사용하여 배치 서비스 중단 가능


