### 도커 컴포즈 실행 명령어
```bash
# 이미지 빌드 및 실행
docker-compose build  # 애플리케이션 빌드
docker-compose up -d  # 백그라운드 실행

# 로그 확인
docker-compose logs -f application

# 컨테이너 중지 및 삭제
docker-compose down -v
```
