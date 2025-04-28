## 🛠 초기 세팅
### 1. 레포지토리 클론
```bash
git clone https://github.com/prgrms-aibe-devcourse/AIBE1-Project2-Team02-backend.git
cd your-repository
```

### 2. application-dev.yml 세팅
```text
spring:
  datasource:
    url: db-url
    username: db-username
    password: db-password
server:
  port: 8081
logging:
  level:
    aibe1.proj2.mentos.feature.initTest.model.mapper: DEBUG
springdoc:
  swagger-ui:
    enabled: true
  api-docs:
    enabled: true
```