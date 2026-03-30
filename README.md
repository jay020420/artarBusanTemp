# ArtAR Busan MVP

ArtAR Busan은 작품 3D 재현이 아니라, 작품 설명을 AR 패널로 실제 공간 위에 배치하는 Android 전용 MVP입니다.

## 목표 플로우
1. 앱 실행
2. 행사 선택
3. AR 카메라 화면 진입
4. 마커 인식
5. 작품 데이터 조회(API 실패 시 mock fallback)
6. AR 설명 패널 표시(작품명/작가/설명/더보기/TTS/체크인)
7. 체크인 스탬프 로컬 저장

## 기술 스택
- Android: Kotlin, ARCore, Sceneform UX, MVVM, Retrofit, kotlinx.serialization, DataStore, Android TTS
- Backend: FastAPI, Pydantic, In-memory seed (PostgreSQL 확장 가능한 repository 구조)

## 폴더 구조
- `android-app/`: Android 앱
- `android-app/app/src/main/java/com/artar/busan/ui`: 화면/뷰모델
- `android-app/app/src/main/java/com/artar/busan/ar`: AR 마커/패널 로직
- `android-app/app/src/main/java/com/artar/busan/data`: API/Mock 데이터소스, Repository
- `android-app/app/src/main/java/com/artar/busan/model`: Event/Venue/Artwork/VisitLog
- `android-app/app/src/main/java/com/artar/busan/tts`: TTS 매니저
- `android-app/app/src/main/java/com/artar/busan/checkin`: 로컬 스탬프 저장
- `backend/app`: FastAPI 서버(main/schemas/repository/seed)

## Android 실행 방법
1. Android Studio에서 `android-app` 열기
2. Gradle Sync
3. API 서버를 로컬에서 실행 (아래 Backend 실행 방법)
4. 에뮬레이터/기기에서 실행

### Android 주요 구현 포인트
- `EventSelectionFragment`: 행사 목록 선택
- `ArExperienceFragment`: ARCore 이미지 인식 -> AR 패널 렌더링
- `ArtArRepositoryImpl`: API 실패 시 Mock fallback
- `TtsManager`: 언어별 음성 출력, 미지원 언어 graceful fallback
- `StampStore`: DataStore 기반 체크인 중복 방지

## Backend 실행 방법
1. Python 가상환경 생성/활성화
2. 의존성 설치

```bash
cd backend
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

3. 확인
- `GET http://localhost:8000/health`
- `GET http://localhost:8000/events`
- `GET http://localhost:8000/events/event-busan-2026/artworks`
- `GET http://localhost:8000/artworks/art-001`

## API 명세 요약
- `GET /events`: 행사 목록
- `GET /events/{eventId}/artworks`: 행사별 작품 목록
- `GET /artworks/{artworkId}`: 작품 상세

## 샘플 데이터
- Event: `event-busan-2026`
- Artworks: `art-001`, `art-002`
- Marker key: `marker_001`, `marker_002`

## 아키텍처 결정 이유
- MVVM + Repository: UI/데이터 관심사 분리
- API/Mock 분리: 네트워크 실패 fallback 보장
- 마커 기반 AR + 텍스트 카드: MVP 범위에 맞는 경량 구현
- DataStore: 체크인 중복 방지 및 로컬 상태 지속

## 향후 개선 포인트
1. 마커 이미지를 실제 행사 리소스로 교체하고 서버에서 동적 동기화
2. 이벤트별 테마 색상을 AR 패널/상세 UI에 일관 적용
3. 체크인 이벤트를 Firebase Analytics 등으로 전송
4. SQLite -> PostgreSQL 연동 repository 구현으로 서버 확장
5. 오프라인 캐시 정책(만료/동기화) 추가

## 알려진 제약사항
1. 현재 마커 DB는 코드에서 생성한 샘플 비트맵 기반(실서비스용 마커셋 아님)
2. AR 패널은 최대 3개만 유지(성능/지터 방어용 단순 정책)
3. FastAPI 패키지가 설치되지 않은 환경에서는 서버 실행 검증 불가
4. Android 빌드는 이 환경에서 Gradle/SDK 부재로 실제 컴파일 검증 미실행

## 주요 파일
- `android-app/app/src/main/java/com/artar/busan/ui/ArExperienceFragment.kt`
- `android-app/app/src/main/java/com/artar/busan/data/ArtArRepositoryImpl.kt`
- `android-app/app/src/main/java/com/artar/busan/checkin/StampStore.kt`
- `android-app/app/src/main/java/com/artar/busan/tts/TtsManager.kt`
- `backend/app/main.py`
- `backend/app/seed.py`
