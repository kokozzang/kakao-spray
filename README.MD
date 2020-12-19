
  
# 카카오 페이 뿌리기  
카카오 페이 뿌리기 API  
  
## 사용 기술
- java 11
- spring boot 2.4.1  
- spring data jpa  
- junit5  
- mysql  
  
  
## 실행  
docker로 mysql 실행후 애플리케이션 실행이 필요합니다.  
  
1. mysql docker 실행   
```  
$ ./mysql_docker.sh  
```  
2. 스프링 부트 애플리케이션 실행  
  
## 문제 해결 전략  
  
  
### API  
뿌리기 생성, 뿌리기 받기, 뿌리기 상태 조회 3개의 API가 있습니다.    
모든 API는 상황에 맞는 HTTP STATUS 코드를 포함하여 응답합니다.  
요청 실패시 적절한 HTTP STATUS 코드와 원인 파악에 도움이 되는 메시지를 응답합니다.  
  
#### 공통 request 헤더  
|헤더|필수|설명|  
|---|----|--|  
|X-USER-ID|y|요청자 아이디|  
|X-ROOM-ID|y|대화방 아이디|  
  
#### POST /spray 뿌리기 생성  
  
Request body  
|필드|필수|설명|  
|---|----|--|  
|budget|y|뿌릴 금액|  
|numberOfPeople|y|인원수|  
  
Response  
|응답|필수|설명|  
|---|----|--|  
|token|y|토큰|  
  
  
#### POST /spray/{token}/prize-money  
뿌리기 받기  
Path variable  
|필드|필수|설명|  
|---|----|--|  
|token|y|토큰|  
  
Response  
|응답|필수|설명|  
|---|----|--|  
|prizeMoney|y|당첨금|  
  
  
#### GET /spray/{token}  
뿌리기 상태 조회  
|필드|필수|설명|  
|---|----|--|  
|token|y|토큰|  
  
Response  
|응답|필수|설명|  
|---|----|--|  
|createdAt|y|뿌린 시각|  
|budget|y|뿌린 금액|  
|paidPrizeMoney|y|받기 완료된 금액|  
|paymentDetails|y|받기 완료된 정보|  
|paymentDetails.prizeMoney||받은 금액|  
|paymentDetails.userId||받은 사용자 아이디|  
  
  
### 테이블  
- spray: 뿌리기
- spray_item: 개별 뿌리기 아이템

뿌리기가 저장되는 spray 테이블, 뿌리기 금액이 분배된 개별 아이템이 저장되는 spray_item 테이블 두 개를 생성하였습니다.   
  
뿌리기 받기 요청시 spray_item의 version 필드를 사용하여 동시성을 처리합니다.  
  
  
### 테스트  
controller 와 service layer 에 대한 테스트를 작성하였습니다.  
- controller  
   - service 를 mocking하여, 요청 값에 따른 validation과 예외 발생시 응답에 대해 검증했습니다.  
- service  
   - respository를 mocking하여 service의 비즈니스 로직에 대해 검증했습니다.