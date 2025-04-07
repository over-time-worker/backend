<div align="center">
  <h1>Owl-Express</h1>
</div>

# 📝 프로젝트 소개
### OwlExpress
MSA(Microservices Architecture) 기반의 국내 물류 관리 및 배송 시스템입니다. <br/>
B2B(Business to Business) 환경에서 물류와 배송을 효율적으로 관리하기 위한 솔루션을 제공합니다.<br/>
각 지역에 허브센터를 두고 있으며, 각 허브 센터는 여러 업체의 물건을 보관합니다. 업체의 상품은 필요에 따라 허브로 전달되며, 배송 요청이 들어오면 목적지 허브로 물품을 이동시켜 최종 배송지에 전달합니다.
<br />

### 📄 프로젝트 설계
- MSA 기반 아키텍처로 독립적인 마이크로서비스 개발 및 배포가 가능한 구조
- 도메인 주도 설계를 고려한 Layered Architecture 적용
- 확장성과 유지보수성을 고려한 서비스 간 통신 구조
<br />

### 🚩 프로젝트 목적
- MSA 기반 시스템 설계 및 구현을 통한 실무 역량 강화
- 서비스 간 API 연동 및 데이터 무결성 유지
- Gemini API를 활용한 AI 기능 통합
- 권한 기반 접근 제어 시스템 구현
- 논리적 삭제(soft delete) 처리를 통한 데이터 관리
<br />

# ⚙️ 서비스 구성 <br />
### 🏗️ 허브 관리 <br />
- **허브 위치**: 전국 17개 지역(서울, 경기, 부산 등)에 허브센터 운영
- **허브간 이동정보**: 허브 간 경로 매핑으로 효율적인 물류 이동 지원
- **허브 검색**: 지역별 허브 검색 기능

### 👨‍💼 배송 담당자 관리 <br />
- **허브 배송 담당자**: 허브 간 물품 이동 담당 (전체 시스템에 10명)
- **업체 배송 담당자**: 허브에서 수령 업체까지 배송 담당 (허브당 10명)
- **담당자 검색**: 담당자 정보 검색 기능

### 🏢 업체 관리 <br />
- **업체 소속**: 모든 업체는 특정 허브에 소속
- **업체 타입**: 생산업체와 수령업체로 구분
- **업체 검색**: 업체명, 지역으로 검색 기능

### 📦 상품 관리 <br />
- **상품 소속**: 모든 상품은 특정 업체와 허브에 소속
- **상품 검색**: 상품명, 카테고리로 검색 기능
- **재고 관리**: 허브별 상품 재고 관리

### 📋 주문 관리 <br />
- **주문 생성/취소**: 주문 생성 시 재고 감소, 취소 시 재고 복원
- **재고 확인**: 허브에 재고 없을 경우 주문 실패 처리
- **주문 내역**: 주문 상태 추적 및 조회

### 🚚 배송 관리 <br />
- **배송 데이터**: 주문 생성 시 배송 및 경로 기록 자동 생성
- **배송 추적**: 현재 배송 상태 실시간 조회
- **배송 담당자 배정**: 배송 유형에 따른 담당자 자동 배정

### 👤 사용자 관리 <br />
- **권한 관리**: 마스터 관리자, 허브 관리자, 배송 담당자, 업체 담당자 역할 구분
- **계정 관리**: 사용자 정보 관리 및 비활성화 처리
<br />

# ⚙️ 실행 방법 <br />
- **서버**: Spring Boot 3.x
- **데이터베이스**: PostgreSQL
- **빌드 툴**: Gradle
- **API 게이트웨이**: Spring Cloud Gateway
- **서비스 디스커버리**: Spring Cloud Eureka
- **추가 설정 파일**: application-prod.yml, application-eureka.yml
<br />

**서비스 실행 명령어** <br />
```
# 서비스 디스커버리 서버 시작
java -jar eureka-server-1.0.0.jar --spring.profiles.active=prod

# API 게이트웨이 시작
java -jar api-gateway-1.0.0.jar --spring.profiles.active=prod

# 각 마이크로서비스 시작
java -Xms256M -Xmx256M -jar hub-service-1.0.0.jar --spring.profiles.active=prod > hub.log 2>&1 &
java -Xms256M -Xmx256M -jar delivery-service-1.0.0.jar --spring.profiles.active=prod > delivery.log 2>&1 &
java -Xms256M -Xmx256M -jar order-service-1.0.0.jar --spring.profiles.active=prod > order.log 2>&1 &
java -Xms256M -Xmx256M -jar user-service-1.0.0.jar --spring.profiles.active=prod > user.log 2>&1 &
```
<br />

## 📜 ERD
![Owl-Express](https://github.com/user-attachments/assets/701b141d-9cfc-4686-aa23-da4992687845)
)
<br />

## 👨‍👩‍👧‍👦 팀 Notion (API 명세서 / 인프라 설계 / 서비스 간 통신 구조)
<a href="" target="_blank">👉🏻클릭 !  [팀노션] </a>
<br />

## ⚙ 기술 스택
### Back-end
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringBoot.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringSecurity.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringDataJPA.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/JWT.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Qeurydsl.png?raw=true" width="80">
<img src="https://github.com/user-attachments/assets/08e68472-fbdc-4a86-a2af-b28bb37ff132?raw=true" width="80" height="85">
</div>

### Spring Cloud
<div>
<img src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" width="80">
<img src="https://user-images.githubusercontent.com/25181517/186711335-a3729606-5a78-4496-9a36-06efcc74f800.png" width="80">
</div>

### Infra
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/AWSEC2.png?raw=true" width="80">
</div>

### Tools
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Figma.png?raw=true" width="80">
</div>
<br />

## 🛠️ 프로젝트 아키텍처
![MSA 아키텍처](https://github.com/user-attachments/assets/placeholder-architecture.png)
