# TIL 메모장

## 프로젝트 소개

하루 동안 배운 것을 짧게 기록하고 목록으로 다시 보는 개인용 웹 앱입니다.

**만든 목적:** Claude Code를 실제 프로젝트에 활용하는 연습. CLAUDE.md로 프로젝트 규칙을 설정하고, 커스텀 스킬·에이전트를 만들어 반복 작업을 자동화하는 흐름을 익힙니다.

---

## 기술 스택

| 구분 | 내용 |
|------|------|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 4.0.8 |
| 빌드 | Maven Wrapper (mvnw) |
| ORM | MyBatis 4.0.1 (XML 매퍼) |
| DB (실행) | MySQL 8.x |
| DB (테스트) | H2 인메모리, MySQL 호환 모드 |
| 화면 | 순수 HTML + JavaScript (fetch), 프레임워크 없음 |
| 테스트 | JUnit 5, MockMvc |

---

## 실행 방법

### 1. MySQL에 데이터베이스 생성

```sql
CREATE DATABASE til CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. application-local.yml 생성

`src/main/resources/application-local.yml.example`을 복사해 `application-local.yml`을 만들고 비밀번호를 채웁니다.

```bash
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
```

파일 내용 (비밀번호 수정 필요):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/til?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: (여기에 비밀번호 입력)
  h2:
    console:
      enabled: false
  sql:
    init:
      mode: always
      platform: mysql
```

### 3. 앱 실행 (local 프로파일)

**PowerShell:**
```powershell
$env:SPRING_PROFILES_ACTIVE="local"; ./mvnw spring-boot:run
```

**Git Bash:**
```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

브라우저에서 http://localhost:8080 접속

### 4. 테스트 실행

```bash
./mvnw test
```

테스트는 H2 인메모리 DB를 사용하므로 MySQL 없이도 실행됩니다.

---

## 프로파일 구조

| 프로파일 | DB | 용도 |
|----------|----|------|
| 기본 (없음) | H2 인메모리 | `./mvnw test` — MySQL 없이도 빠르게 실행 |
| local | MySQL 8.x | `spring-boot:run` — 실제 데이터 유지 |

H2를 `MODE=MySQL`로 설정해 MySQL 문법을 그대로 씁니다. 테스트가 MySQL 특화 쿼리에 의존하지 않으므로 두 환경이 동일한 `schema.sql`을 공유합니다.

---

## API 목록

| 메서드 | 경로 | 설명 |
|--------|------|------|
| `GET` | `/api/tils` | 목록 조회 (최신순) |
| `GET` | `/api/tils/{id}` | 상세 조회 |
| `POST` | `/api/tils` | 작성 |
| `DELETE` | `/api/tils/{id}` | 삭제 |

### POST /api/tils 요청 본문

```json
{
  "title": "오늘 배운 것 (필수, 100자 이내)",
  "content": "자세한 내용 (필수, 2000자 이내)",
  "tag": "AI (선택, 50자 이내)"
}
```

### 에러 응답 형식

```json
{
  "message": "사람이 읽을 수 있는 설명"
}
```

- `400` — 입력 검증 실패 (필수값 누락, 길이 초과)
- `404` — 존재하지 않는 id

---

## 폴더 구조

```
temp_project/
├── src/
│   ├── main/
│   │   ├── java/com/example/til/
│   │   │   ├── TilApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── TilController.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── service/
│   │   │   │   └── TilService.java
│   │   │   ├── mapper/
│   │   │   │   └── TilMapper.java
│   │   │   ├── domain/
│   │   │   │   └── Til.java
│   │   │   ├── dto/
│   │   │   │   ├── TilCreateRequest.java
│   │   │   │   ├── TilDetail.java
│   │   │   │   └── TilListItem.java
│   │   │   └── exception/
│   │   │       └── TilNotFoundException.java
│   │   └── resources/
│   │       ├── application.yml          # 기본 프로파일 (H2)
│   │       ├── application-local.yml    # 로컬 프로파일 (MySQL, gitignore 대상)
│   │       ├── schema.sql               # 테이블 DDL (두 환경 공용)
│   │       ├── mapper/
│   │       │   └── TilMapper.xml        # SQL
│   │       └── static/
│   │           └── index.html           # 단일 페이지 화면
│   └── test/
│       └── java/com/example/til/
│           ├── TilApplicationTests.java
│           ├── controller/
│           │   └── TilControllerTest.java
│           └── service/
│               └── TilServiceTest.java
├── docs/
│   └── SPEC.md                          # 기능 명세 (요구사항의 기준)
├── .claude/
│   ├── settings.json                    # Claude Code 권한 설정
│   ├── skills/commit/SKILL.md           # /commit 커스텀 스킬
│   └── agents/reviewer.md               # reviewer 서브에이전트
├── CLAUDE.md                            # 프로젝트 규칙 (Claude Code 지시서)
└── pom.xml
```

---

## Claude Code 설정

### `CLAUDE.md`

Claude Code가 이 프로젝트에서 따르는 규칙 모음. 기술 스택, 패키지 구조, 호출 방향, SQL 작성 규칙, 코딩 컨벤션, Spring Boot 4.x 주의사항(Jackson 3.x 패키지 변경, MockMvc 어노테이션 위치 변경)을 명시합니다. 명세에 없는 기능을 구현하기 전에 먼저 묻도록 지시합니다.

### `docs/SPEC.md`

기능 명세의 기준 문서. 4개 API, 테이블 구조, 화면 요건, 이번 범위에서 제외한 기능(로그인·수정·페이징·검색·배포)을 정의합니다. 구현·테스트 작성 시 이 문서를 기준으로 판단합니다.

### `.claude/settings.json`

Claude Code의 도구 실행 권한을 제어합니다.
- **허용:** `./mvnw *`, `git status`, `git diff`, `git log` — 테스트·읽기 전용 명령은 자동 승인
- **차단:** `git push` — 원격 반영은 항상 사람이 직접

### `.claude/skills/commit`

`/commit` 으로 호출하는 커스텀 스킬. 실행 순서: 테스트 통과 확인 → diff 검토(비밀번호·빌드 산출물 포함 여부) → 관련 파일만 스테이징 → `feat/fix/test/docs/refactor/chore` 타입 형식으로 커밋. 커밋은 이 스킬로만 하고 Claude가 스스로 커밋하지 않도록 CLAUDE.md에서 제한합니다.

### `.claude/agents/reviewer`

커밋 전 코드를 검토하는 서브에이전트. `sonnet` 모델로 실행됩니다. CLAUDE.md 규칙과 SPEC.md 요구사항을 기준으로 변경 파일을 읽고, 명세 불일치·입력 검증 누락·잘못된 HTTP 상태 코드·호출 방향 위반·SQL 인젝션(`${}` 사용)·XSS(`innerHTML`에 사용자 입력) 위험을 심각도별로 보고합니다. 파일을 직접 수정하지 않습니다.
