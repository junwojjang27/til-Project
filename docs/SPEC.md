# TIL 메모장 요구사항

## 목적
하루 동안 배운 것을 짧게 기록하고 목록으로 다시 보는 개인용 웹 앱.

## 기능
1. 기록 작성: 제목(필수, 100자 이내), 내용(필수, 2000자 이내), 태그(선택, 50자 이내, 예: AI)
2. 목록 조회: 최신 작성순으로 제목, 태그, 작성일 표시
3. 상세 조회: 목록에서 항목을 누르면 내용이 펼쳐짐
4. 삭제: 항목별 삭제 버튼, 삭제 전 확인창

## API
| 메서드 | 경로 | 설명 |
| --- | --- | --- |
| GET | /api/tils | 목록 조회 |
| GET | /api/tils/{id} | 상세 조회 |
| POST | /api/tils | 작성 (JSON 본문) |
| DELETE | /api/tils/{id} | 삭제 |

- 검증 실패는 400, 없는 id는 404
- 에러 응답 본문: {"message": "사람이 읽을 수 있는 설명"}

## 테이블
TIL
- ID: BIGINT, 자동 증가, PK
- TITLE: VARCHAR(100), NOT NULL
- CONTENT: VARCHAR(2000), NOT NULL
- TAG: VARCHAR(50), NULL 허용
- CREATED_AT: TIMESTAMP, NOT NULL, 기본값 현재 시각

## DB 구성
- 실행(local 프로파일): MySQL 8.x, localhost:3306, DB명 til
- 테스트(기본 프로파일): H2 인메모리, MySQL 호환 모드
- 접속 정보: application-local.yml (gitignore 대상, 커밋 금지)
- 테이블 DDL: src/main/resources/schema.sql (두 환경 모두 공용)

## 화면
index.html 한 장. 위쪽에 작성 폼, 아래쪽에 목록.
검증 실패 시 서버가 준 message를 폼 아래에 보여준다.

## 이번 범위에서 제외
로그인, 수정, 페이징, 검색, 배포

## 완료 기준
- ./mvnw test 통과
- 브라우저에서 작성, 목록, 상세, 삭제가 모두 동작
- 기능 단위로 나뉜 커밋이 GitHub에 올라가 있음
