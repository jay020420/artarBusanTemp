# Codex 실행 프롬프트 — ArtAR Busan

아래 지침을 따르며 **ArtAR Busan Android 앱 MVP**를 개발하라.

## 0. 역할
너는 시니어 Android/Kotlin + ARCore + FastAPI 아키텍트이자 구현자다.  
목표는 **전시 작품 자체를 3D로 띄우는 앱이 아니라, 작품 설명을 AR 패널로 실제 공간 위에 띄우는 Android 전용 MVP**를 구축하는 것이다.

---

## 1. 제품 핵심 정의
- 제품명: ArtAR Busan
- 목표: 전시 공간에서 작품 마커를 인식하면, 해당 작품의 설명/작가명/언어별 안내/TTS 버튼이 포함된 **AR 설명 패널**을 띄운다.
- 플랫폼: **Android Only**
- 핵심 가치:
  - 작품 설명의 직관적 전달
  - 다국어 지원
  - 접근성(TTS)
  - 행사형 템플릿 구조로 재사용 가능

---

## 2. 반드시 지켜야 할 범위
### 포함
- Kotlin 기반 Android 앱
- ARCore 이미지(마커) 인식
- 작품 설명 AR 패널 표시
- 행사/작품 데이터 API 연동
- 다국어 처리
- TTS
- 로컬 스탬프/체크인 저장
- 확장 가능한 템플릿 구조

### 제외
- 3D 스캐닝
- LiDAR 활용
- 실시간 공간 스캔/SLAM 고도화
- iOS 지원
- 복잡한 3D 모델 렌더링
- 관리자 CMS 구현 완료본
- 과도한 인증/결제/푸시 시스템

**범위를 벗어나는 기능은 임의로 추가하지 마라.**

---

## 3. 기술 스택 고정
### Android App
- Kotlin
- Android Studio 기준 Gradle Kotlin DSL
- ARCore
- Camera/AR 렌더링은 Android 네이티브 친화적으로 구현
- Jetpack ViewModel
- Retrofit 또는 Ktor client 중 1개 선택
- kotlinx.serialization 또는 Moshi/Gson 중 1개 선택
- Android TTS
- DataStore 또는 SharedPreferences 중 1개 선택

### Backend
- FastAPI
- PostgreSQL 연동 가능한 구조
- MVP 단계에서는 SQLite 또는 메모리 대체 가능하되, PostgreSQL로 확장 가능하게 설계

### 문서/산출물
- README.md
- 실행 방법
- 폴더 구조 설명
- API 명세 요약
- 향후 확장 포인트

---

## 4. 구현 목표
다음 사용자 플로우가 실제로 성립해야 한다.

1. 앱 실행
2. 행사 선택 화면 표시
3. 행사 선택 후 AR 카메라 화면 진입
4. 특정 마커 이미지 인식
5. 해당 작품 정보를 API 또는 로컬 mock 데이터에서 조회
6. 작품명 / 작가명 / 설명 / 언어 선택 / TTS 버튼이 포함된 AR 패널 표시
7. 사용자가 체크인 버튼 또는 스탬프 획득 가능
8. 스탬프는 로컬에 저장
9. 네트워크 실패 시 mock 데이터 fallback 가능

---

## 5. 원하는 아키텍처
다음 구조를 우선 고려하라.

- app/
  - ui/
  - ar/
  - data/
  - domain/
  - model/
  - tts/
  - checkin/
  - util/

### 권장 패턴
- MVVM
- Repository pattern
- API/Mock data source 분리
- AR 화면 로직과 일반 UI 로직 분리
- 행사 템플릿 재사용을 위한 Event / Venue / Artwork 중심 데이터 구조

---

## 6. 데이터 모델 최소 요구사항
아래 모델을 우선 구현하라.

### Event
- id
- name
- slug
- primaryColor
- secondaryColor
- logoUrl

### Venue
- id
- eventId
- name
- latitude
- longitude
- descriptionI18n

### Artwork
- id
- venueId
- markerImageKey
- titleI18n
- artist
- descriptionI18n
- thumbnailUrl
- audioGuideAvailable

### VisitLog / Stamp
- artworkId or venueId
- visitedAt
- language
- storedLocally

다국어 필드는 Map<String, String> 또는 Locale key 기반 구조로 구현하라.

---

## 7. UI 요구사항
### 7.1 행사 선택 화면
- 행사 목록 표시
- 행사 카드 클릭 시 선택
- 행사별 대표 색상 반영

### 7.2 AR 화면
- 카메라 프리뷰
- 현재 선택 행사 표시
- 마커 인식 상태 표시
- 인식 성공 시 AR 설명 패널 표시

### 7.3 AR 설명 패널 포함 항목
- 작품명
- 작가명
- 짧은 설명
- 더보기 버튼
- TTS 재생 버튼
- 체크인/스탬프 버튼

### 7.4 상세 화면(선택)
- 긴 설명
- 언어 전환
- 행사/장소 정보

UI는 화려함보다 **명확성, 가독성, 유지보수성**을 우선한다.

---

## 8. AR 구현 조건
- 마커 기반 인식 중심으로 설계
- Plane detection이 꼭 필요하지 않다면 최소화
- 작품 설명 패널은 “실제 작품 근처에 떠 있는 정보 패널”처럼 보이면 충분
- 복잡한 3D 오브젝트 대신 **text/image card overlay** 중심
- 성능 저하를 막기 위해 AR 오브젝트 수를 제한
- 인식 실패/중복 인식/떨림(jitter)에 대한 기본 방어 로직 포함

---

## 9. 네트워크/API 요구사항
최소 API 예시는 아래와 같다.

- GET /events
- GET /events/{eventId}/artworks
- GET /artworks/{artworkId}

FastAPI 예제 서버를 함께 작성하라.  
최소한 다음을 제공하라.
- mock seed data
- CORS 설정
- pydantic schema
- 실행 명령어

앱은 API 실패 시 로컬 mock repository로 fallback 할 수 있어야 한다.

---

## 10. 체크인/스탬프 요구사항
- 체크인 시 작품 또는 장소 단위로 중복 방지
- 로컬 저장
- 저장 성공 시 간단한 피드백 표시
- 이후 Firebase Analytics 등을 붙이기 쉽게 이벤트 포인트를 남겨라

---

## 11. TTS 요구사항
- 현재 선택 언어 기준으로 작품 설명 읽기
- TTS 사용 불가 시 graceful fallback
- 화면 종료 시 TTS 정리

---

## 12. Codex 작업 방식
다음 순서대로 진행하라.

### Step 1
프로젝트 전체 구조와 구현 계획을 먼저 제시하라.

### Step 2
필수 파일들을 생성하라.

### Step 3
Android 앱의 실행 가능한 최소 뼈대를 작성하라.

### Step 4
FastAPI mock 서버를 작성하라.

### Step 5
Android 앱과 API를 연결하라.

### Step 6
마커 인식 → AR 패널 표시 흐름을 구현하라.

### Step 7
TTS / 체크인 / 다국어를 연결하라.

### Step 8
README와 실행 방법을 정리하라.

각 단계마다:
- 무엇을 만들었는지
- 왜 그렇게 설계했는지
- 다음 단계가 무엇인지
짧고 명확하게 설명하라.

---

## 13. 산출물 요구사항
최종적으로 아래가 있어야 한다.

1. Android 프로젝트 코드
2. FastAPI 서버 코드
3. README.md
4. 샘플 데이터
5. 주요 파일 설명
6. 향후 개선 포인트
7. 알려진 제약사항

---

## 14. 코딩 스타일 규칙
- 빌드 가능한 코드 우선
- 임시 코드라도 TODO 주석으로 명확히 남길 것
- 파일/클래스 이름 직관적으로 작성
- 하드코딩 최소화
- 주석은 필요한 곳에만
- 과도한 추상화 금지
- MVP를 망치는 과설계 금지

---

## 15. 실패 방지 규칙
다음 행동은 하지 마라.
- 필요 이상으로 기술 스택을 늘리기
- Compose/XML 혼합을 복잡하게 만들기
- 3D 렌더링 엔진을 새로 도입하기
- 범위 밖 기능을 멋대로 추가하기
- 설명 없이 대규모 파일 생성하기
- mock 데이터 없이 API만 정의하기

---

## 16. 우선순위
### P0
- 행사 선택
- 마커 인식
- AR 설명 패널
- 작품 데이터 연결
- TTS
- 체크인 로컬 저장

### P1
- 언어 전환
- 상세 설명 화면
- 에러 처리 개선
- mock/API fallback 개선

### P2
- 분석 이벤트 포인트
- 테마 색상 적용 고도화
- 스탬프 UX 개선

---

## 17. 최종 목표 문장
“사용자가 Android 앱에서 행사 선택 후 작품 마커를 비추면, 작품 설명이 AR 패널로 나타나고, 다국어 안내와 TTS 및 체크인 기능까지 동작하는 MVP를 구축한다.”

이제 위 요구사항을 기준으로 개발을 시작하라.  
먼저 **전체 구현 계획과 폴더 구조**부터 제시한 후, 순차적으로 파일 생성을 시작하라.
