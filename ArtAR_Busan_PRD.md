# 📘 ArtAR Busan PRD (AI-Coding Optimized)

## 1. 📌 Product Overview

### 1.1 Product Name
ArtAR Busan

### 1.2 Vision
실제 전시 공간 위에 작품 설명을 AR로 배치하여  
관람 경험을 확장하는 행사형 AR 템플릿 플랫폼

### 1.3 Core Concept
- 작품 자체를 3D로 재현 ❌
- 작품 "설명"을 AR로 공간에 배치 ⭕

---

## 2. 🎯 Problem Definition

### 2.1 Current Problems
- 전시 정보가 SNS/웹에 분산
- 외국인 대상 언어 장벽
- AR은 단순 QR 웹뷰 수준
- 행사 종료 후 재사용 불가

### 2.2 Solution
- AR 기반 "공간 설명 인터페이스"
- 다국어 + TTS
- CMS 기반 템플릿 플랫폼

---

## 3. 👤 Target Users

### P1 일반 관람객
- 빠르게 작품 이해

### P2 외국인 관광객
- 다국어 안내 필요

### P3 행사 운영자
- 개발 없이 콘텐츠 등록

---

## 4. 🧠 Core UX Flow

1. 앱 실행
2. 행사 선택
3. 카메라 실행
4. 마커 인식
5. AR 설명 패널 표시
6. 사용자 인터랙션

---

## 5. 🧩 Core Features

### F1 AR 설명 패널
- 작품명
- 작가
- 설명
- 이미지

### F2 다국어 지원
- ko / en / jp / cn

### F3 TTS
- 음성 안내

### F4 스탬프 시스템
- 체크인 기반

---

## 6. 🎨 AR UI Design

### 6.1 AR Overlay 구조

[ 실제 작품 ]
      ↓
[ AR 패널 (Anchor) ]
 ├ 작품명
 ├ 설명
 ├ 버튼 (더보기)

### 6.2 인터랙션
- Tap → 상세 보기
- 버튼 → TTS 실행

---

## 7. 🏗 System Architecture

### 7.1 Client (Android)
- Kotlin
- ARCore
- CameraX

### 7.2 Backend
- FastAPI
- PostgreSQL

### 7.3 CMS
- React

---

## 8. 📦 Data Model

### Artwork
- id
- title_i18n
- description_i18n
- marker_image_url
- media_url

### Venue
- id
- lat
- lng

---

## 9. 🔄 API Spec

### GET /events
→ 행사 목록 반환

### GET /artworks
→ 작품 리스트 반환

---

## 10. ⚙️ AI-Coding Task Breakdown

## Phase 1: 프로젝트 초기 설정
- Android 프로젝트 생성
- ARCore 설정

## Phase 2: 카메라 + 마커 인식
- CameraX 연동
- 이미지 트래킹 구현

## Phase 3: AR Anchor 생성
- detected image → anchor 생성
- plane detection OFF (마커 기반)

## Phase 4: AR UI Overlay
- ViewRenderable 생성
- 텍스트 표시

## Phase 5: API 연동
- FastAPI 서버 연결
- JSON 파싱

## Phase 6: 다국어 처리
- locale 기반 데이터 로딩

## Phase 7: TTS
- Android TTS 연동

## Phase 8: 스탬프 시스템
- 로컬 저장 (SharedPreferences)

---

## 11. ✅ Acceptance Criteria

- 마커 인식 시 AR 패널이 떠야 한다
- 언어 변경 시 설명이 바뀌어야 한다
- TTS 버튼 클릭 시 음성 출력
- 체크인 시 스탬프 저장

---

## 12. ❌ Out of Scope

- 3D 스캐닝
- 실시간 SLAM 기반 공간 맵핑
- 고급 3D 렌더링

---

## 13. 🚀 Future Expansion

- 공간 기반 AR (GPS Anchor)
- SNS 공유
- 데이터 분석 대시보드

---

## 14. 🔥 Key Differentiation

- "작품을 보여주는 AR"이 아니라
- "설명을 공간에 배치하는 AR"

---

## 15. 📌 Developer Notes

- ARCore Image Tracking 사용
- 성능 위해 텍스트 중심 렌더링
- 3D 모델 최소화
