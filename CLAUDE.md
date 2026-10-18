# TIL 메모장

하루 동안 배운 것을 기록하는 개인용 웹 앱이다.
요구사항은 docs/SPEC.md가 기준이고, 명세에 없는 기능은 만들기 전에 먼저 묻는다.

## 기술 스택
- Java 17, Spring Boot 4.0.x, Maven Wrapper(mvnw)
- MyBatis(XML 매퍼)
- DB: 실행은 MySQL 8.x(local 프로파일), 테스트는 H2 인메모리 MySQL 호환 모드(기본 프로파일)
- 접속 정보는 application-local.yml에 두고 커밋하지 않는다
- 화면: src/main/resources/static/index.html 한 장, 순수 JavaScript(fetch). 프론트엔드 프레임워크는 쓰지 않는다
- 테스트: JUnit 5, MockMvc

## 명령어
- 테스트: ./mvnw test
- 실행: ./mvnw spring-boot:run 후 http://localhost:8080

## 구조 규칙
- 패키지: com.example.til 아래 controller, service, mapper, domain, dto
- 호출 방향은 Controller → Service → Mapper 한 방향만. Controller에서 Mapper를 직접 부르지 않는다
- SQL은 src/main/resources/mapper/*.xml에만 쓴다. 어노테이션 SQL은 쓰지 않는다
- MyBatis 파라미터는 #{}만 쓴다. ${}는 쓰지 않는다
- DB 컬럼은 대문자 스네이크(CREATED_AT), 자바 필드는 카멜(createdAt)로 하고 map-underscore-to-camel-case로 연결한다

## 코딩 규칙
- 입력 검증은 Bean Validation(@Valid)으로 한다
- 에러 응답은 {"message": "..."} 형식으로 통일하고 @RestControllerAdvice에서 처리한다
- 화면에서 사용자 입력을 출력할 때는 textContent를 쓴다. innerHTML에 사용자 입력을 넣지 않는다
- 주석과 커밋 메시지는 한국어로 쓴다
- 새 의존성을 추가하기 전에 이유를 설명하고 승인을 받는다

## 작업 방식
- 기능 하나 단위로 진행한다: 계획 → 구현 → 테스트 작성 → ./mvnw test 통과
- 테스트가 실패하면 테스트를 고치기 전에 구현이 명세와 맞는지 먼저 확인한다
- 커밋은 사용자가 /commit을 실행할 때만 한다. 스스로 커밋하지 않는다
- 명세나 CLAUDE.md와 다르게 진행해야 할 상황이면 멈추고 먼저 묻는다
