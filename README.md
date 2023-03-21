## 사용 라이브러리
- JDK 17
- Spring Framework 3.0.2
- Spring WebMVC
- H2
    - local에서만 테스트 용도로 사용하기 위해 해당 인메모리DB 사용
        - auto-ddl : create-drop
- Spring Security
    - /v1/search에 대해 ignore 처리 그 밖의 경우에는 에러 발생

- HTTP 클라이언트: spring webclient
    - restTemplate 개발 중단으로 해당 라이브러리 사용
    - 동기, 비동기 방식 둘다 가능 (.block())

## Project 구조
- src/main/.../base > 해당 서비스의 controller, service, dto 구성
- src/main/.../kakao or naver > 외부 client는 따로 분리 하여 사용
- src/main/.../audit > 공통으로 사용하는 dateEntity 구현
- src/main/.../exception > custom exception 사용
- ...

## API
### 테스트 방법
- 따로 profile을 설정하지 않고 서버 실행 (기본 profile 테스트용)
- 장소 검색 :
  curl --location --request GET 'localhost:8201/v1/search/places/{장소 키워드}' \
  --data-raw ''
- 실행 api :
  curl --location --request GET 'localhost:8201/v1/search/places/most'
### 장소 검색
- 외부 서비스에 대해 장애 및 조회가 되지 않을 경우 빈 리스트로 처리
- 동시성 이슈 해결을 위해 select를 하지 않고 insert후 unique index error catch 후 update 진행
    - 낙관적 락을 사용 할 수 있지만 느릴것 같아 해당 방법 체택
    - SearchService.updateKeywordCnt()
- response ex) :
  [
  {
  "place_name": "별미곱창 본점"
  },
  {
  "place_name": "세광양대창 교대본점"
  },
  {
  "place_name": "해성막창집 본점"
  },
  {
  "place_name": "곱 마포점"
  },
  {
  "place_name": "백화양곱창 6호"
  },
  {
  "place_name": "60년전통신촌황소곱창 강남직영점"
  },
  {
  "place_name": "구공탄막창 형님본점"
  },
  {
  "place_name": "은하곱창"
  },
  {
  "place_name": "당산옛날곱창"
  },
  {
  "place_name": "구공탄막창 2호점"
  }
  ]

#### 검색 키워드 목록
- PageRequest 사용 : default : (page 1, size : 10, sort : desc, field : cnt)
- transaction(readOnly=true) : 스냅샷 저장, 변경 감지(dirty check)의 작업을 수행하지 않아 성능에 좋으므로 사용
- response ex) :
  {
  "content": [
  {
  "keyword": "곱창",
  "cnt": 2
  },
  {
  "keyword": "은행",
  "cnt": 1
  }
  ],
  "total_elements": 2
  }
