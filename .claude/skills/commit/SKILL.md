---
name: commit
description: 현재 변경사항을 이 프로젝트의 커밋 규칙에 맞춰 테스트 후 커밋한다
disable-model-invocation: true
allowed-tools:
  - Bash(git status *)
  - Bash(git diff *)
  - Bash(git add *)
  - Bash(git commit *)
  - Bash(git log *)
  - Bash(./mvnw test)
---

## 현재 변경 파일
!`git status --short`

## 절차
1. ./mvnw test를 실행한다. pom.xml이 아직 없으면 건너뛴다. 실패하면 커밋하지 말고 실패 원인만 보고하고 멈춘다.
2. git diff로 변경 내용을 확인한다. 비밀번호, API 키, target/ 같은 빌드 산출물이 섞여 있으면 멈추고 알린다.
3. 이번 작업과 관련된 파일만 이름을 지정해서 git add 한다.
4. 아래 형식으로 커밋 메시지를 쓰고 커밋한다. $ARGUMENTS가 있으면 요약에 반영한다.
5. git log --oneline -1 결과를 보여준다.

## 커밋 메시지 형식
첫 줄: <타입>: <무엇을 했는지 50자 이내 요약>
빈 줄 다음: 왜 바꿨는지 1~3줄 (선택)

타입
- feat: 기능 추가
- fix: 버그 수정
- test: 테스트 추가, 수정
- docs: 문서
- refactor: 동작 변화 없는 구조 개선
- chore: 설정, 빌드, 의존성

예시: feat: TIL 작성 API 추가
