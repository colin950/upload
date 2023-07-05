## 사용 라이브러리
- JDK 17
- Spring Framework 3.0.2
- Spring WebMVC
- H2
    - local에서만 테스트 용도로 사용하기 위해 해당 인메모리DB 사용
        - auto-ddl : create-drop
- Spring Security
    - /v1/file 대해 ignore 처리 그 밖의 경우에는 에러 발생 (인증 정보가 없기 때문)

- HTTP 클라이언트: spring webclient
    - restTemplate 개발 중단으로 해당 라이브러리 사용
    - 동기, 비동기 방식 둘다 가능 (.block())

## Project 구조
- src/main/.../base > 해당 서비스의 controller, service, dto 구성
- src/main/.../audit > 공통으로 사용하는 dateEntity 구현
- src/main/.../exception > custom exception 사용
- src/main/.../repository > querydsl, jpa 모듈 
- src/main/.../utils > ffmpeg upload util 관리 및 부가적 util 구성
- src/main/.../component > scheduler component, 부가 기능들(user정보 셋팅등 ...)
- ...

## DB
- file_info (동영상 원본 관련 정보 테이블)
- file_convert_info (동영상 변환 관련 정보 테이블)

## API
### 테스트 방법
#### 따로 profile을 설정하지 않고 서버 실행 (기본 profile 테스트용)
- martipart 100mb 제한
- 테스트용 mp4 (resources/video3.mp4)
1. docker-compose up -f docker-compose.yml up
   - mac사용 및 리눅스 사용시에 local redis로도 사용 가능
2. UploadApplication 구동
--------------
### 동영상 업로드 API :
- proxy패턴으로 인해 적용이 되지 않은 transaction을 적용하기 위해 ApplicationContext 사용 (동영상 변환 완료 이후 사용)
1. 동영상 업로드 및 썸네일 생성(첫 장면)
2. 원본 데이터 및 썸네일 DB 저장
3. 동영상 변환 비동기 처리
4. 변환 완료시 변환 데이터 DB 저장
- curl --location --request POST 'localhost:8201/v1/file/upload' \
  --form 'file=@"/Users/jinho_colin/git-repos/video3.mp4"' \
  --form 'title="title"'
--------------
### 영상 상세 조회 API :
- 동영상 상세 조회 API
- querydsl 사용하여 조회에 사용
- curl --location --request GET 'localhost:8201/v1/file/video/1'
--------------
### 동영상 변환 진행률 조회 API :
- 변환 진행률 조회
- 변환 비동기 처리 중에 redis에 저장 (ttl은 따로 지정하지 않았지만 수정 가능)
  - curl --location --request GET 'localhost:8201/v1/file/video/1/progress'
--------------

