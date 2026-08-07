# Daniel's Community
> 사용자들이 게시글과 댓글을 작성하고 좋아요를 통해 소통할 수 있는
> SpringBoot 기반 REST API 커뮤니티 서비스입니다.

<br>

## 기술스택
- Language: Java 17
- Framework: Spring Boot 4.0.6 
- Security: Spring Security & JWT
- ORM: Spring Data JPA & QueryDSL
- Database: MySQL
- Container: Docker
- Orchestration: Kubernetes & Helm
- CI/CD: GitHub Actions
- Container Registry: Docker Hub
- Infrastructure: AWS EC2 & AWS SSM

<br>

## 폴더구조
```text
src/main/java/com/daniel/community
├── Application.java
├── auth
│   ├── controller
│   ├── dto
│   └── service
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
├── post
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
├── comment
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
└── global
    ├── config
    ├── exception
    ├── response
    └── security
```

<br>

## 아키텍처
<img src="docs/images/Architecture.png" width="1100">


<br>

## API 목록
[Daniel's Community REST API](https://docs.google.com/spreadsheets/d/16pu0hmGkYhMrjpo1Jyw5POYPdBZY3jD2GQdoi-egTG0/edit?gid=1878554884#gid=1878554884)

| 구분 | Method | Endpoint | 설명 | 인증 |
|---|---|---|---|:---:|
| 회원 | `POST` | `/users/signup` | 회원가입 | X |
|  | `GET` | `/users/me` | 내 정보 조회 | O |
|  | `PATCH` | `/users/me` | 내 정보 수정 | O |
|  | `DELETE` | `/users/me` | 회원 탈퇴 | O |
|  | `PATCH` | `/users/me/password` | 비밀번호 변경 | O |
|  | `GET` | `/users/emails/{email}` | 이메일 중복 확인 | X |
|  | `GET` | `/users/nicknames/{nickname}` | 닉네임 중복 확인 | X |
| 인증 | `POST` | `/users/login` | 로그인 및 JWT 발급 | X |
|  | `DELETE` | `/users/logout` | 로그아웃 | O |
| 프로필 이미지 | `POST` | `/users/profile-images` | 프로필 이미지 업로드 | X |
|  | `DELETE` | `/users/profile-images/{profileImageName}` | 프로필 이미지 삭제 | O |
|  | `GET` | `/images/profiles/{fileName}` | 프로필 이미지 조회 | X |
| 게시글 | `POST` | `/posts` | 게시글 작성 | O |
|  | `GET` | `/posts` | 게시글 목록 조회 | X |
|  | `GET` | `/posts?cursor={postId}` | 커서 기반 게시글 목록 조회 | X |
|  | `GET` | `/posts/search?keyword={keyword}` | 게시글 검색 | X |
|  | `GET` | `/posts/{postId}` | 게시글 상세 조회 | X |
|  | `PATCH` | `/posts/{postId}` | 게시글 수정 | O |
|  | `DELETE` | `/posts/{postId}` | 게시글 삭제 | O |
| 게시글 이미지 | `POST` | `/posts/images` | 게시글 이미지 업로드 | O |
|  | `GET` | `/images/posts/{fileName}` | 게시글 이미지 조회 | X |
| 좋아요 | `POST` | `/posts/{postId}/likes` | 게시글 좋아요 | O |
|  | `DELETE` | `/posts/{postId}/likes` | 게시글 좋아요 취소 | O |
| 댓글 | `GET` | `/posts/{postId}/comments` | 댓글 목록 조회 | X |
|  | `POST` | `/posts/{postId}/comments` | 댓글 작성 | O |
|  | `PATCH` | `/posts/{postId}/comments/{commentId}` | 댓글 수정 | O |
|  | `DELETE` | `/posts/{postId}/comments/{commentId}` | 댓글 삭제 | O |

<br>

## 환경변수
```bash
export DB_URL="jdbc:mysql://localhost:3306/community"
export DB_USERNAME="community"
export DB_PASSWORD="password"
```

<br>

## 설치 및 실행
```bash
git clone https://github.com/Kakao-Tech-Bootcamp/4-daniel-community-BE.git
cd 4-daniel-community-BE
./gradlew bootRun
```

서버는 `http://localhost:3000`에서 실행됩니다.

<br>

### Docker
```bash
docker build -t daniel-community-be .

docker run -d \
  --name daniel-community-be \
  -p 3000:3000 \
  -e DB_URL="jdbc:mysql://host.docker.internal:3306/community" \
  -e DB_USERNAME="daniel" \
  -e DB_PASSWORD="password" \
  -v "$(pwd)/uploads:/app/uploads" \
  daniel-community-be
```

<br>

### Kubernetes
```bash
kubectl create secret generic daniel-community-secret \
  --from-literal=DB_URL="jdbc:mysql://mysql-host:3306/community" \
  --from-literal=DB_USERNAME="daniel" \
  --from-literal=DB_PASSWORD="password"
```
DB 접속 정보를 Secret으로 생성합니다.
<br>

```bash
helm upgrade --install daniel-community \
  ./deploy/helm/daniel-community \
  --set image.repository="<DOCKERHUB_USERNAME>/daniel-community-be" \
  --set image.tag="latest"
```
Helm으로 애플리케이션을 배포합니다.
<br>

```bash
kubectl get pods
kubectl rollout status deployment/daniel-community
```
배포 상태를 확인합니다.

