# 요구사항 구현
* 대용량 트래픽을 고려한 검색어 목록 응답에 대한 caching 적용
* 카카오 API or 네이버 API등의 외부 API 장애 시 장애 전파를 막기 위한 circuit breaker 적용
* 대용량 트래픽 대응을 위해 외부 검색 API 연동 시 서버 자원의 효율적인 사용을 위한 비동기, non blocking 호출 적용
* 동시성 이슈를 해결하기 위한 키워드 별 검색 횟수 저장 배치 및 스케쥴링 처리 적용

# 환경
* JavaSE-17
* Gradle 7.5.1
* Spring Boot 2.7.5
* H2 database 

# 오픈소스
* resilience4j - 외부 API 호출 장애 시 장애 전파를 막기 위한 circuite breaker 적용
* jsoup - 응답값의 html tag 제거
* caffeine - 검색어 목록 local cache 적용

# curl 호출방법

* 장소 검색 API: curl -X GET "http://localhost:8080/v1/search?query=%EC%9D%80%ED%96%89" 
* 인기 검색어 API: curl -X GET "http://localhost:8080/v1/popularkeywords"


# API 명세

### 블로그 검색

#### 기본 정보
GET /v1/search HTTP/1.1

#### Request

| *name* | *type* | *description* | *required* |
|-------|-------|-------------|----------|
| query | String | 검색을 원하는 검색어 | O |


#### Response
JSON 형식으로 응답

| *name (1st depth)* | *name (2nd depth)* | *type* | *description* |
|--------------------|--------------------|--------|---------------|
| request | | JSONObject | 검색 요청 정보 |
| request | query | String | 검색을 원하는 검색어 |
| documents | | JSONArray | 문서 목록 |
| documents | name | String | 장소명 |
| documents | url | String | URL |
| documents | category | String | 장소 분류 |
| documents | phone | String | 전화번호 |
| documents | address | String | 지번 주소 |
| documents | roadAddress | String | 도로명 주소 |


### 인기 키워드 조회

#### 기본 정보
GET /v1/popularkeywords HTTP/1.1

#### Request
*N/A*

#### Response
JSONArray 형식으로 응답. 인기순으로 내림차순 응답 (최대 10개)

| *name* | *type* | *description* |
|--------|--------|---------------|
| keyword | String | 검색어 |
| count | Integer | 검색 빈도 |
