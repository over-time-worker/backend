<div align="center">

</div> 

# 📝 프로젝트 소개
### 프로젝트 이름
스프링 부트 기반 배달 및 포장 음식 주문 관리 플랫폼을 개발했습니다. <br />
광화문 근처에서 운영될 음식점들의 배달 및 포장 주문 관리, 결제, 그리고 주문 내역 관리 기능을 제공하는 플랫폼입니다.

<br />

### 📄 프로젝트 설계
- 초기 단계이기에 모놀리식 아키텍처로 개발하지만, 마이크로 서비스 아키텍처로 변경 용이한 구조를 생각하여 개발
- 도메인 주도 설계를 고려하여 ERD 설계
- 기능 개발 시 확장 가능성을 고려
<br />

### 🚩 프로젝트 목적
- AI API 연동 : AI API를 연동하여 가게 사장님이 상품 설명을 쉽게 작성할 수 있도록 지원
- 권한에 따른 접근 관리 : 고객, 가게 주인, 관리자 별로 데이터 및 페이지 접근 권한 제한
- 데이터 보존 및 삭제 처리 : soft delete 적용, 데이터 감사 로그 저장

<br />

# ⚙️ 서비스 구성 <br />
### 🙍‍♀️ 유저 <br />
- **고객:** 자신의 주문 내역만 조회 가능
- **가게 주인:** 자신의 가게 주문 내역, 가게 정보, 주문 처리 및 메뉴 수정 가능
- **관리자:** 모든 가게 및 주문에 대한 전체 권한 보유
- **회원 검색:** 회원 ID로 회원 검색 지원
  
### 🏪 가게 <br />
- **카테고리:** 음식점 카테고리로 가게 분류
- **지역:** 음식점의 위치로 가게 분류
- **가게 검색:** 가게 이름으로 가게 검색 지원

### 🍕 메뉴 및 옵션 <br />
- **메뉴 및 옵션 노출 상태:** 판매중, 숨김 처리, 매진 모두 지원
- **메뉴 검색:** 메뉴 이름으로 메뉴 검색 지원

### 🎞️ 이미지 <br />
- **메뉴 및 옵션 이미지 저장:** AWS S3를 연동하여 가게 사장님이 등록하는 메뉴 및 옵션 이미지를 저장
- **이미지 순서:** 이미지 등록시 순서를 고려하여 저장 및 노출
- **이미지 미리보기:** 이미지 등록시 미리보기를 제공하여 가게 사장님이 확인할 수 있도록 지원

### 🤖 가게 및 메뉴 설명 추천 AI <br />
- **가게 및 메뉴 설명 자동 생성:** AI API를 연동하여 가게 사장님이 가게 및 메뉴 설명을 쉽게 작성할 수 있도록 지원
- **AI 요청 기록:** AI API 요청 질문과 대답은 모두 데이터베이스에 저장


<br />

# ⚙️ 실행 방법 <br />
- **서버:** Spring Boot 3.x
- **데이터베이스:** PostgreSQL
- **빌드 툴:**  Gradle
- **추가 해야 할 파일:** application-prod, messages.properties

 <br />

  **java jar 파일 실행 명령어** <br />
  `nohup java -Xms256M -Xmx256M -jar orderdelivery-1.0.0.jar --spring.profiles.active=prod > order.log 2>&1 &`



<br />

## 📜 ERD
<a href="" target="_blank">👉🏻클릭 !  [ERD] </a>

<br />

## 👨‍👩‍👧‍👦 팀 Notion (테이블 명세서 / API 명세서 / ERD 명세서 / 인프라 설계)
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

### Infra
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/AWSEC2.png?raw=true" width="80">



### Tools
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Figma.png?raw=true" width="80">
</div>

<br />

## 🛠️ 프로젝트 아키텍쳐
