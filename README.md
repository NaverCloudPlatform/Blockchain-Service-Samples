# Blockchain Service Sample Application & Chaincode's guide

본 샘플은 네이버클라우드 기술블로그 [이렇게 사용하세요!] [Blockchain Service: 상품 유통 이력 관리 시스템 구축하기](https://blog.naver.com/n_cloudplatform/222946929076)에서 설명한 내용에서 사용하는 소스코드 입니다.

## Sample Distributed Application
### 개요
  - 상품 유통 이력 관리 시스템을 구현하는 Application 입니다
  - 네이버클라우드 Blockchain Service에 설치된 체인코드를 호출하여 상품 이력을 관리하는 기능을 수행합니다.
  - Requirement
     - Node 12 이상
     - npm 6 이상
     - Frontend : vue.js
     - Backend : node.js, express

## Sample Chaincode
   - Hyperledger Fabirc에 설치하여 트랜잭션을 발생시키는 체인코드입니다
   - 네이버클라우드 Blockchain Service에 install/instantiate를 진행하여 상품 유통 이력 과정의 트랜잭션을 발생시키는 역할을 수행합니다.
   - requirement
      - JAVA 11 이상
      - org.hyperledger.fabric-chaincode-java:fabric-chaincode-shim:2.2.0

## Application UI
![image](https://user-images.githubusercontent.com/45256557/207275148-f3fff016-45a1-4779-a880-6e8b424693ad.png)
![image](https://user-images.githubusercontent.com/45256557/207276117-8c62b5f7-6635-4735-985a-f13c3fafb4a6.png)
