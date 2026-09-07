# 필요한 순간에 읽는 공식자료 지도

확인일: 2026-09-07. 아래 공식 문서 14개의 본문을 열어 제목과 관련 내용을 확인했어. 각 문서 전체를 읽거나 예제를 현재 프로젝트에서 실행 검증한 것은 아니야.

이 자료는 처음부터 완독할 목록이 아니야. 지금 막힌 문제에 해당하는 항목 하나를 골라 **예상 → 짧은 실험 → 결과 설명**으로 연결하면 돼. 공식 문서·IDE·문법 검색을 이용하는 것은 자력 학습과 양립해. 완성 구현을 외워서 재현하는 시험으로 사용하지 않아.

## 버전을 먼저 구분하기

- `library/build.gradle`에서 확인한 설정은 Java 21, Spring Boot 4.1.1이야. 아래 Spring Boot 문서도 확인 시점에 4.1.1로 표시됐어.
- Spring Framework 문서는 확인 시점에 7.0.9로 표시됐어. 이것이 프로젝트에서 실제로 해결된 의존성 버전과 같다고 단정하지 않아. 다른 프로젝트에 적용할 때는 그 프로젝트의 의존성 버전과 import를 먼저 확인해.
- Jakarta Persistence 자료는 3.2 명세 API를 기준으로 골랐어. JPA의 규칙과 Hibernate 구현의 실행 시점·SQL 결과는 구분해서 봐.
- 프로젝트에는 MySQL 드라이버와 H2 의존성이 함께 있지만, 이것만으로 실행 중인 DB 종류나 MySQL 서버 버전을 알 수는 없어. 아래 MySQL 8.4 자료는 해당 버전을 선택해 실험할 때의 기준이고, H2에서 같은 잠금 동작을 보장한다는 뜻은 아니야.
- 버전 없는 Spring 문서 주소는 이후 다른 버전으로 바뀔 수 있어. 링크가 열리는지와 본문 상단의 버전을 함께 확인하면 돼.

## 지금 생성·연관관계·저장을 다룰 때

### 01. 생성자가 완성해야 하는 상태

[Dev.java — Providing Constructors for your Classes](https://dev.java/learn/classes-objects/defining-constructors/)

- **언제·어디를 읽을까:** 생성자에서 어떤 값을 받아야 하는지, 기본 생성자가 언제 생기는지 헷갈릴 때 읽어. 생성자 선언, 오버로딩, 컴파일러가 제공하는 기본 생성자 부분이면 충분해.
- **직접 확인:** 필요한 값을 받는 생성자를 추가했을 때 인자 없는 생성이 가능한지 확인하고, Java의 기본 생성자 규칙과 JPA가 요구하는 생성자를 별개로 설명해봐.
- **버전:** Java 21에서도 적용되는 기본 문법이야. Java 전체 문법 학습을 다시 시작하라는 뜻은 아니야.

### 02. 날짜 계산과 불변 객체

[Java 21 API — LocalDate](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/LocalDate.html)

- **언제·어디를 읽을까:** 대출일·반납예정일을 만들 때 `of`, `plusDays`, `now` 설명을 읽어. `LocalDate`는 시각과 시간대를 저장하지 않고, `plusDays`는 원본을 바꾸지 않고 새 날짜를 반환해.
- **직접 확인:** 월말이나 연말의 날짜에 14일을 더하고, 원래 날짜와 결과 날짜를 각각 확인해봐. 날짜 단위 정책과 정확한 반납 시각이 필요한 정책도 구별해보면 좋아.
- **버전:** 프로젝트와 같은 Java 21 API야.

### 03. 테스트에서 ‘오늘’을 고정하기

[Java 21 API — Clock](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Clock.html)

- **언제·어디를 읽을까:** 날짜가 바뀌면 테스트 결과도 바뀌는 문제를 만났을 때 `fixed`, 시간대, `LocalDate.now(clock)`과의 연결을 읽어. 현재처럼 계산에 사용할 날짜를 인자로 받는 방식도 가능하므로 지금 즉시 Clock을 도입할 필요는 없어.
- **직접 확인:** 고정된 시각과 시간대에서 얻은 날짜가 반복 실행해도 같은지 확인해봐. ‘오늘’을 얻는 책임과 ‘14일 뒤’를 계산하는 책임을 구별하는 게 목적이야.
- **버전:** Java 21 API야. `LocalDate`에 시간대가 저장되는 것은 아니지만 ‘오늘’을 얻을 때 사용하는 시간대는 날짜에 영향을 줘.

### 04. mappedBy가 가리키는 대상

[Jakarta Persistence 3.2 — OneToMany](https://jakarta.ee/specifications/persistence/3.2/apidocs/jakarta.persistence/jakarta/persistence/onetomany)

- **언제·어디를 읽을까:** 목록 타입, 관계의 반대편, `mappedBy` 문자열이 섞일 때 설명 첫 부분과 `mappedBy` 항목을 읽어. 이 값은 상대 엔티티에서 그 관계를 소유하는 **필드 또는 프로퍼티의 이름**이야. 현재 코드는 필드에 매핑하므로 필드 이름을 써.
- **직접 확인:** 먼저 `Member.java`의 `List<Loan>`에서 상대가 Loan임을 확인해. 그다음 **파일을 바꿔 `Loan.java`를 보고**, 회원을 참조하는 필드 이름 `member`와 `@JoinColumn`을 찾아봐. 나중에 관계 저장을 실험할 때는 목록만 바꾼 경우와 외래키를 관리하는 참조를 설정한 경우의 DB 결과를 비교하면 좋아.
- **주의할 경계:** `mappedBy`를 적은 쪽은 관계의 반대편이야. `@OneToMany`가 있다는 사실만으로 모든 경우에 상대가 자동으로 주인이 된다고 일반화하면 안 돼. `@JoinColumn`의 `name`은 DB 컬럼 이름이므로 같은 종류의 이름이 아니야.

### 05. 객체의 변경과 DB의 변경 사이

[Jakarta Persistence 3.2 — EntityManager](https://jakarta.ee/specifications/persistence/3.2/apidocs/jakarta.persistence/jakarta/persistence/entitymanager)

- **언제·어디를 읽을까:** 저장했다고 생각했는데 SQL이나 재조회 결과가 예상과 다를 때 `Persistence Context`, `flush`, `clear`를 읽어. `flush`는 변경을 DB와 동기화하고, `clear`는 관리하던 엔티티를 영속성 컨텍스트에서 분리해.
- **직접 확인:** 매핑 저장 검증에서는 변경 후 flush하고 clear한 다음 다시 조회해, 메모리의 같은 객체만 보고 통과한 것은 아닌지 확인해봐. SQL 로그도 함께 읽으면 좋아.
- **주의할 경계:** flush는 커밋이 아니야. clear 전에 flush되지 않은 변경은 사라질 수 있고, flush가 끝났어도 트랜잭션은 이후 롤백될 수 있어. 이 순서를 모든 서비스 코드에 넣으라는 뜻은 아니야.

### 06. 실패했을 때 무엇이 롤백되는가

[Spring Framework — Rolling Back a Declarative Transaction](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/rolling-back.html)

- **언제·어디를 읽을까:** 기록 생성과 상태 변경을 함께 성공·실패시키는 서비스를 만들 때 기본 롤백 규칙과 `rollbackFor` 설명을 읽어. 기본 설정은 `RuntimeException`과 `Error`에 대해 롤백하고, checked exception은 별도 규칙이 없으면 기본 롤백 대상이 아니야.
- **직접 확인:** Spring의 트랜잭션 경계를 거쳐 호출한 작업에서 중간 실패를 만들고, 작업 종료 후 다른 트랜잭션에서 저장 결과를 확인해봐. 예외를 내부에서 잡아 정상 반환하는 경우를 실패 예외가 밖으로 전달되는 경우와 같다고 보지 않아야 해.
- **버전:** 확인한 문서는 Framework 7.0.9야. 명시한 롤백 설정이 있으면 기본 규칙보다 그 설정을 먼저 확인해. ‘트랜잭션만 붙이면 동시 요청도 모두 안전하다’는 결론은 이 자료가 보장하지 않아.

## 동작을 증명할 테스트를 고를 때

### 07. 무엇을 실행하는 테스트인가

[Spring Boot — Testing Spring Boot Applications](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html)

- **언제·어디를 읽을까:** 테스트를 추가하기 전에 `Auto-configured Spring MVC Tests`, `Auto-configured Data JPA Tests`, `Testing With a Running Server` 중 목적에 해당하는 부분만 읽어. `@DataJpaTest`는 JPA 중심으로 구성하며 일반 서비스 전체를 자동으로 검사하는 테스트가 아니야.
- **직접 확인:** 날짜 계산은 순수 Java 테스트, 매핑·쿼리는 DB를 쓰는 테스트, HTTP 바인딩·응답은 MVC 테스트처럼 검증 대상을 먼저 적어봐. 저장소를 가짜로 바꾼 테스트로 실제 SQL·제약조건·DB 롤백이 검증됐다고 주장하지 않으면 돼.
- **버전:** 확인한 문서는 Boot 4.1.1이야. 현재 프로젝트에서는 `DataJpaTest`가 `org.springframework.boot.data.jpa.test.autoconfigure`, `WebMvcTest`가 `org.springframework.boot.webmvc.test.autoconfigure` 아래에 있어. 과거 Boot 3 예제의 import를 그대로 복사하지 말고 IDE와 실제 의존성에서 확인해.

### 08. 테스트 자체의 롤백에 속지 않기

[Spring Framework — TestContext Transaction Management](https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/tx.html)

- **언제·어디를 읽을까:** DB 테스트는 통과하지만 애플리케이션 동작을 믿기 어려울 때 `Test-managed Transactions`, `Transaction Rollback and Commit Behavior`, `Avoid false positives when testing ORM code`를 읽어. 테스트 프레임워크가 만든 트랜잭션과 실제 서비스의 트랜잭션 경계를 구분해.
- **직접 확인:** 외래키·null 제약 검증은 flush까지 진행하고, 서비스 원자성은 실패한 서비스 호출의 트랜잭션이 끝난 뒤 새 트랜잭션에서 상태를 읽어 확인하는 식으로 검증 범위를 분명히 해봐. 테스트 종료 시 자동 롤백된다는 사실만으로 서비스 롤백을 증명할 수는 없어.
- **버전·범위:** Framework 7.0.9 문서를 확인했어. 실제 HTTP 서버를 띄운 테스트에서는 서버와 테스트 클라이언트가 별도 스레드·트랜잭션일 수 있으므로 테스트 쪽 롤백이 서버의 저장까지 지워주지 않아. 이 경계는 위 Boot 문서에도 설명돼 있어.

## SQL·동시 요청이 실제 문제가 됐을 때

### 09. 객체 관계를 SQL로도 읽기

[MySQL 8.4 — JOIN Clause](https://dev.mysql.com/doc/refman/8.4/en/join.html)

- **언제·어디를 읽을까:** 회원별 기록이나 일부 반납 목록처럼 테이블 여러 개를 조회할 때 `INNER JOIN`, `LEFT JOIN`, `ON`과 `WHERE` 부분을 읽어. 문서 처음의 전체 문법을 외울 필요는 없어.
- **직접 확인:** 같은 작은 데이터로 기록이 없는 회원까지 포함하는 조회와 기록이 있는 회원만 포함하는 조회를 비교해봐. 이후 JPA가 실행한 SQL이 어떤 행을 합치는지 설명하면 돼.
- **버전:** MySQL 8.4 기준이야. JPA의 연관관계 매핑 자체와 SQL JOIN을 실행한다는 행위를 동일시하지 않아야 해.

### 10. 트랜잭션 사이에서 무엇이 보이는가

[MySQL 8.4 — InnoDB Transaction Isolation Levels](https://dev.mysql.com/doc/refman/8.4/en/innodb-transaction-isolation-levels.html)

- **언제·어디를 읽을까:** 요청 두 개가 같은 데이터를 읽거나 변경할 때 `READ COMMITTED`와 `REPEATABLE READ` 설명부터 읽어. InnoDB의 문서상 기본은 REPEATABLE READ지만 실제 세션 설정은 따로 확인해야 해.
- **직접 확인:** DB 연결 두 개에서 한쪽이 읽고 있는 동안 다른 쪽이 변경·커밋하게 해, 첫 연결의 다음 조회가 무엇을 보는지 기록해봐. ‘한 작업 안의 원자성’과 ‘여러 작업 사이의 격리’를 구별하는 게 목적이야.
- **버전:** MySQL 8.4 InnoDB 기준이야. 다른 DB나 격리 수준의 결과를 그대로 예상하지 말고, 실험에 사용한 DB 버전·엔진·격리 수준을 함께 적어.

### 11. 조회 후 변경 사이의 경쟁

[MySQL 8.4 — InnoDB Locking Reads](https://dev.mysql.com/doc/refman/8.4/en/innodb-locking-reads.html)

- **언제·어디를 읽을까:** 두 요청이 동시에 ‘대출 가능’ 같은 조건을 통과하는 사례를 재현한 뒤 일반 SELECT와 `SELECT ... FOR UPDATE` 설명을 읽어. 후자는 트랜잭션 안에서 읽은 대상의 변경을 보호하기 위한 잠금 읽기이고, 잠금은 커밋·롤백 때 해제돼.
- **직접 확인:** 두 연결에서 같은 대상에 잠금 읽기를 시도해 두 번째 연결이 언제 기다리고 언제 진행하는지 관찰해봐. 실제 데이터를 바꾸기 전에는 잠금을 얻은 뒤의 상태를 기준으로 조건을 판단하는 흐름도 확인해야 해.
- **주의할 경계:** 일반 SELECT가 모두 차단되는 것은 아니고, 잠금 범위도 조회 조건·인덱스·격리 수준의 영향을 받아. 이 문서는 비관적 잠금의 한 선택지를 설명할 뿐, 모든 중복 요청에 같은 해결책을 강제하지 않아.

## API·변경 기록·배포를 다룰 때

### 12. HTTP 동작을 근거로 설명하기

[RFC 9110 — HTTP Semantics](https://www.rfc-editor.org/rfc/rfc9110.html)

- **언제·어디를 읽을까:** 메서드나 상태 코드를 선택하거나 재요청 동작을 정의할 때 9.2.1 안전성, 9.2.2 멱등성, 필요한 15절 상태 코드만 찾아 읽어. 멱등성은 같은 요청을 반복했을 때 의도된 서버 효과가 같은지에 관한 개념이야.
- **직접 확인:** ‘없는 도서 조회’, ‘현재 상태와 충돌하는 요청’, ‘같은 반납 요청 재전송’의 응답과 상태 변화 정책을 각각 설명해봐. 반복 응답 내용이 완전히 같아야만 멱등인 것은 아니야.
- **버전·범위:** HTTP 의미에 관한 표준이고 특정 Spring 버전과 무관해. 이 문서를 API 경로 이름까지 자동으로 결정해주는 설계 답안으로 쓰지는 않아.

### 13. 내가 만든 변경을 설명할 수 있게 남기기

[Pro Git 한국어 — 수정하고 저장소에 저장하기](https://git-scm.com/book/ko/v2/Git의-기초-수정하고-저장소에-저장하기)

- **언제·어디를 읽을까:** 한 기능을 구현·수정하고 기록할 때 상태 확인, 변경 비교, Staging Area와 커밋 부분을 읽어. 작업 폴더의 모든 변경과 다음 커밋에 포함할 변경은 같지 않을 수 있어.
- **직접 확인:** 커밋 전 차이를 읽고 ‘어떤 동작이 왜 달라졌고 어떻게 확인했는지’를 설명해봐. 별도 목적의 변경이 섞였다면 나눌지 판단하는 것도 연습이야.
- **버전:** Pro Git 2판 한국어 번역이야. 오래된 화면·기본 브랜치 이름은 현재 환경과 다를 수 있지만 상태·스테이징·커밋의 구별을 읽는 데 사용할 수 있어.

### 14. IDE 밖에서 실행하고 운영하기

[Spring Boot — Installing Spring Boot Applications](https://docs.spring.io/spring-boot/how-to/deployment/installing.html)

- **언제·어디를 읽을까:** 핵심 기능과 테스트가 갖춰져 다른 사람이 실행할 단계가 되면 JAR 실행과 서비스 실행 부분을 읽어. 현재 Windows에서 JAR로 실행하는 경험을 먼저 하고, Linux 배포를 선택하면 systemd 부분을 이어 읽으면 돼.
- **직접 확인:** IDE가 아닌 곳에서 같은 산출물을 실행하고, 프로세스 종료·재시작·로그 확인 방법을 실행 기록으로 남겨봐. 배포 설정과 실제 DB에 따라 재시작 후 데이터가 유지되는지도 확인하면 좋아.
- **버전·범위:** 확인한 문서는 Boot 4.1.1이야. Linux의 systemd 설정을 Windows에 그대로 실행하지 않아. 이 한 페이지로 공개 운영의 인증·HTTPS·비밀정보·백업까지 준비됐다고 판단하지는 않아.

## 자료를 사용한 뒤 남길 최소 기록

문서 제목과 읽은 절, 내 예상 한 문장, 실제로 관찰한 값·SQL·응답, 예상과 달랐던 이유만 남기면 돼. 결과가 예상과 같아도 다른 조건에서 어디까지 보장되는지 한 가지를 적어보면 좋아.

모든 항목의 실험을 지금 수행해야 하는 것은 아니야. 지금의 학습 과제에서 필요한 검증 하나를 골라 사용하고, 나머지는 그 문제가 실제로 등장했을 때 돌아오면 돼.


