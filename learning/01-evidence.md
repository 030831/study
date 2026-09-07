# 현재 코드에서 확인한 학습 경험 지도

확인일: 2026-09-07 · 용도: 학습 진단의 근거와 다음 경험 후보

이 문서는 코드에 존재하는 경험과 아직 확인할 능력을 구분해. 저장소에 구현이 있다는 사실만으로 네가 혼자 설계·구현할 수 있다거나, 반대로 지금의 질문만으로 기초가 부족하다고 판단하지 않아. 두 쇼핑몰이 미완성이라는 것은 네 설명이야. 이번에는 서버·DB를 실행하거나 테스트를 돌리지 않았어.

## 무엇을 실제로 확인했나

| 대상 | 이번 확인 기준 | 읽기 범위와 한계 |
| --- | --- | --- |
| `product` | 공개 `main`의 `de81e952c7a3ff878ff7c7f67e0c7917a82c1958` | 전체 파일 경로 목록, 아래 링크한 핵심 코드와 일부 테스트. 커밋 시각은 2026-09-02 07:32:32 UTC. 배포·현재 실행 상태는 미검증 |
| `spring-commerce-api` | 공개 `main`의 `598c06a19815d59f899b4812361a2aef8b70df90` | 전체 파일 경로 목록, 주문·상품 핵심 코드, 주문 서비스 테스트, README. 커밋 시각은 2026-06-22 11:30:54 UTC. 실행 미검증 |
| `library` | 2026-09-07의 로컬 작업 파일 | 도서 API와 4개 엔티티, DTO·저장소·예외 처리, 테스트 및 빌드 설정. Git 저장소로 인식되지 않아 커밋 SHA 대신 파일 해시를 별도 명세에 기록 |

공개 `main`은 바뀔 수 있어. 아래 GitHub 링크는 확인한 커밋에 고정했어. 검사한 파일 목록과 읽은 범위는 [출처 명세](source-manifest.json)에 있어. 이 문서는 전수 품질 감사가 아니야.

`spring-commerce-api`의 README는 아직 회원·주문이 없다고 적고 있지만, 같은 커밋에 해당 컨트롤러·서비스·엔티티·테스트가 있어. 기능 존재 여부는 README의 진행표보다 실제 소스를 우선했어. [README의 옛 상태](https://github.com/030831/spring-commerce-api/blob/598c06a19815d59f899b4812361a2aef8b70df90/README.md#L20-L34)

## 현재 library의 출발점

- 도서 등록·단건 조회·전체 조회 코드가 있고, 응답 변환은 `ResponseBook(Book)` 생성자에 모여 있어.
- 이전 대화에서 사용자가 정상 등록 201, 빈 제목 400, 존재하는 도서 200, 없는 도서 404, 빈/비어 있지 않은 전체 목록 200을 확인했어. 이는 사용자 보고·스크린샷 근거이며, 오늘 재실행한 결과는 아니야.
- 현재 관계는 **Member 1:N Loan, Loan 1:N LoanItem, Book 1:N LoanItem**이야. `LoanItem`이 연결하는 대상은 **Loan과 Book**이야.
- `Member.java`의 목록은 `List<Loan>`이고 `mappedBy="member"`야. 별도 파일인 `Loan.java`에는 `Member member`가 있어.
- `Loan.java`의 목록은 `List<LoanItem>`이고 `mappedBy="loan"`이야. 별도 파일인 `LoanItem.java`에는 `Loan loan`이 있어.
- `Book.java`의 목록은 `List<LoanItem>`이고 `mappedBy="book"`이야. 별도 파일인 `LoanItem.java`에는 `Book book`이 있어.
- 세 목록은 초기화돼 있고, `Loan`에 `LocalDate loanDate/dueDate`, `LoanItem`에 `LocalDate returnedDate`가 있어. 이번 읽기 시점에 대출 생성자·생성/반납 동작·대출 저장소/서비스/컨트롤러는 아직 없어. 이전에 안내한 `Loan(Member, LocalDate)` 생성자도 현재 파일에는 없어.
- 테스트 소스는 `contextLoads()` 한 개야. 도서 API·대출 규칙을 자동 검증하는 테스트가 있다고 말할 근거는 없어. 최근 엔티티 매핑도 DB 저장으로 검증한 상태는 아니야.

## 비교에 쓸 구체적인 사례 8개

### E1. 계층을 만드는 연습과 데이터 변경을 설명하는 연습

**소스 사실:** `spring-commerce-api`는 `EntityManager`의 `persist/find/JPQL/remove`를 직접 사용하는 상품 저장소가 있어. 상품 수정 서비스는 트랜잭션 안에서 엔티티의 `changeInfo()`를 호출하고 별도의 수정 저장 호출을 하지 않아. `product`에도 저장소를 호출하는 등록·조회 흐름이 있고, `library`의 도서 저장소는 `JpaRepository`를 상속해. [이전 상품 저장소](https://github.com/030831/spring-commerce-api/blob/598c06a19815d59f899b4812361a2aef8b70df90/src/main/java/com/backend/shoppingmall/repository/ProductRepository.java#L14-L30), [이전 상품 수정](https://github.com/030831/spring-commerce-api/blob/598c06a19815d59f899b4812361a2aef8b70df90/src/main/java/com/backend/shoppingmall/service/ProductService.java#L43-L47), [product 상품 서비스](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/service/ProductService.java#L27-L60)

**학습 활용:** 새 도메인의 CRUD를 같은 지시로 다시 만드는 것은 이미 접한 형태를 반복할 가능성이 커. 다음에는 요청을 받은 뒤 어떤 객체가 관리되고, 변경 내용은 어느 시점에 DB에 반영되는지 예측한 뒤 SQL·재조회로 확인하는 경험이 적합해. “변경 감지를 이미 안다”는 판정은 하지 않아.

### E2. 주문상품과 대출도서가 연결하는 대상을 구별하기

**소스 사실:** 이전 쇼핑몰의 `Order`는 회원을 참조하고 주문상품 목록을 가져. `OrderItem`은 주문과 상품을 참조해. 현재 library의 `Loan`과 `LoanItem`도 관계의 모양은 비슷해. [이전 Order](https://github.com/030831/spring-commerce-api/blob/598c06a19815d59f899b4812361a2aef8b70df90/src/main/java/com/backend/shoppingmall/entity/Order.java#L20-L26), [이전 OrderItem](https://github.com/030831/spring-commerce-api/blob/598c06a19815d59f899b4812361a2aef8b70df90/src/main/java/com/backend/shoppingmall/entity/OrderItem.java#L16-L22)

**학습 활용:** 외운 화살표를 재현하는 대신 “이 행 한 개는 어떤 사건인가, 각 외래키는 어떤 행을 가리키는가”를 실제 ID 예시로 설명해보면 좋아. 먼저 `LoanItem` 한 행의 뜻을 정하고, 그다음 외래키와 매핑을 확인해. 과거 주문 코드를 갖고 있다는 사실은 현재 대출 모델을 정확히 이해했다는 증거가 아니야.

### E3. 연관관계 설정과 ‘중복 데이터’의 서로 다른 이유

**소스 사실:** 이전 `Order.addOrderItem()`은 자기 목록에 항목을 추가하고, 그 항목의 주문 참조도 설정해. `OrderItem`은 상품 참조 외에 주문 당시 상품명·가격도 복사해. `product`에도 비슷한 코드와 상품명 변경 후 주문상품명을 확인하는 테스트가 있어. [양쪽 객체 설정](https://github.com/030831/spring-commerce-api/blob/598c06a19815d59f899b4812361a2aef8b70df90/src/main/java/com/backend/shoppingmall/entity/Order.java#L44-L47), [주문 시점 값](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/domain/OrderItem.java#L31-L41), [값 변경 후 확인하는 테스트](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/test/java/com/example/product/service/OrderServiceTest.java#L23-L38)

**학습 활용:** 객체 양쪽의 관계를 맞추는 문제, 거래 당시 값을 보관하는 문제, 현재 항목에서 계산할 수 있는 값을 중복 저장하는 문제는 따로 판단해야 해. “중복이면 항상 없애자”로 일반화하지 않는 연습에 좋아. 대출 완료일 중복 저장을 피한 이유를 주문 가격 보관에 그대로 적용할 수 있는지 설명해보는 진단 후보야.

### E4. 일부 반납은 일부 주문 취소와 비교할 수 있어

**소스 사실:** `product`의 주문은 선택 항목을 취소하고 그 항목의 재고를 복구해. 모든 항목이 취소됐을 때 주문·배송 상태를 변경하고, 서비스는 취소 금액을 결제에 전달해. 일부 취소 후 나머지를 취소하는 테스트가 있어. [선택 취소](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/domain/Order.java#L123-L169), [항목 취소](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/domain/OrderItem.java#L60-L65), [결제 연결](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/service/OrderService.java#L44-L55)

**학습 활용:** `LoanItem` 한 개의 반납과 전체 대출의 완료 여부를 따로 생각하는 데 연결할 수 있어. 먼저 library 요구를 직접 설명·구현하고 나중에 비교해야 전이 여부를 확인하기 좋아. 주문 취소 코드의 상태·결제·배송 구조 전체를 대출에 가져올 이유는 없어.

### E5. 파일 수보다 한 요청에서 함께 바뀌는 데이터가 복잡도를 높여

**소스 사실:** `product`의 `CheckoutService.checkout()`은 장바구니를 판매자별로 나누고, 각 주문을 만든 뒤 하나의 결제를 만들고 장바구니를 비워. 이 메서드에는 트랜잭션 표시가 있어. 주문 생성 안에는 도서관 CRUD에 없던 회원·상품 조회, 재고 변경, 배송 연결도 있어. [Checkout 전체 흐름](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/service/CheckoutService.java#L22-L58), [주문 항목 추가와 재고 변경](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/domain/Order.java#L44-L55)

**학습 활용:** “두 번째 판매자의 상품 재고가 부족하다면 첫 번째 주문과 재고·장바구니는 무엇이 남아야 하는가”를 데이터 단위로 적어볼 수 있어. library에서도 두 권 중 두 번째 권이 대출 불가일 때 무엇을 보존할지 같은 방식으로 검증할 수 있어. 이는 사용자가 실제로 여기서 막혔다는 진단이 아니라, 확인할 가치가 있는 연결 지점이야.

### E6. 테스트 파일이 있는 것과 원하는 실패를 검증한 것은 달라

**소스 사실:** `product`의 Checkout 테스트에는 정상 흐름의 `flush/clear` 후 재조회와 판매자별 분할 확인이 있어. 읽은 그 파일의 두 테스트는 중간 실패의 롤백이나 동시 대출을 검증하지 않아. 이전 쇼핑몰의 주문 취소 테스트도 `flush/clear` 후 취소 상태를 재조회해. [Checkout 테스트](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/test/java/com/example/product/service/CheckoutServiceTest.java#L31-L99), [이전 취소 테스트](https://github.com/030831/spring-commerce-api/blob/598c06a19815d59f899b4812361a2aef8b70df90/src/test/java/com/backend/shoppingmall/service/OrderServiceTest.java#L74-L88)

**학습 활용:** 처음에는 양방향 매핑이 실제 저장·재조회에 반영됐는지, 그다음 여러 데이터의 실패 원자성을 확인하는 테스트를 한 개씩 만들 수 있어. 실패 테스트에서는 예외 발생만 확인하지 말고 실패 후 DB 값도 확인해야 해. 테스트 자체의 트랜잭션과 실제 서비스 경계가 같은지 검토하고, `flush/clear`를 실제 커밋과 혼동하지 않아야 해. 이번 분석은 테스트를 실행하지 않았고 전체 테스트의 부재까지 판정하지 않았어.

### E7. 검색 요구가 바뀔 때 수정해야 하는 곳 찾기

**소스 사실:** `product`의 검색 저장소는 QueryDSL로 결과 목록과 총개수를 각각 조회해. 두 조회에 이름·가격·재고·분류·판매 상태 조건이 들어 있어. [결과와 개수 조회](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/repository/ProductRepositoryImpl.java#L27-L56)

**학습 활용:** “판매자별 필터를 추가하면 무엇을 함께 바꿔야 하나”가 적절한 요구변경 진단이야. 검색 결과가 맞아도 총개수가 틀릴 수 있는지, 어떤 데이터로 확인할지 설명해보면 돼. 페이지 처리·조건 추가는 library 대출의 기본 흐름 다음에 골라볼 수 있는 별도 경험이지, 지금 필수로 추가할 과제는 아니야.

### E8. 요청에 담긴 ID와 요청한 사람의 권한 구별하기

**소스 사실:** `product` 상품 컨트롤러는 로그인 토큰에서 판매자 ID를 얻고, 서비스는 수정 대상 상품의 판매자와 비교해. 주문 조회/취소도 회원 소유 여부를 확인해. 프런트엔드에는 401 응답 시 토큰을 재발급하고 한 번 다시 요청하는 함수가 있어. [로그인 사용자 읽기](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/controller/product/ProductController.java#L41-L48), [상품 소유 확인](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/backend/src/main/java/com/example/product/service/ProductService.java#L52-L60), [재요청 흐름](https://github.com/030831/product/blob/de81e952c7a3ff878ff7c7f67e0c7917a82c1958/frontend/src/features/auth/authenticatedFetch.ts#L21-L32)

**학습 활용:** 서버가 왜 클라이언트가 보내는 `memberId`만으로 권한을 믿으면 안 되는지 설명하는 후속 사례로 쓸 수 있어. 인증·인가 전체의 안전성이나 재시도 멱등성이 검증됐다는 뜻은 아니야. 지금 library에 보안 프레임워크를 한꺼번에 추가하라는 추천도 아니야.

## 이 근거로 추천하는 범위

**지금:** library의 한 대출과 일부 반납을 요구사항에서 구현·저장·실패 확인까지 직접 이어보는 게 적합해 보여. 이미 준비한 관계를 사용하면서 객체 한 개의 행위에서 여러 객체의 일관성으로 범위를 넓힐 수 있어. 현재 안내한 생성자 과제는 아직 미완료이므로, 이 문서가 새로운 대규모 과제를 자동으로 시작시키지는 않아.

**그다음:** library에서 구현한 원리를 설명할 수 있게 되면 `product` 전체를 다시 읽기보다 E4의 부분 취소나 E7의 검색 조건 하나만 골라 기존 코드의 수정 범위를 스스로 찾아봐. 같은 구조를 다른 요구에 적용할 수 있는지 확인하는 경험이 돼.

**별도로 확보할 경험:** SQL·실행계획 측정, 동시 요청 재현, 실제 배포·로그 기반 문제 해결, 다른 사람의 기여와 코드 리뷰는 이번 소스 읽기만으로 확인되지 않았어. “해본 적 없다”는 사실로 기록하지 않고, 별도 경험 기록이나 이후 실습으로 확인할 항목으로 남겨.

**나중에 검토할 모델 질문:** 현재 `Book`은 ISBN이 유니크이고 대출 상태가 한 개야. 같은 ISBN의 실물 두 권을 동시에 운영해야 한다면 책의 서지 정보와 실물 소장을 어떻게 구분할지 정책부터 정해야 해. 현재 범위에서 버그라고 판정하거나 즉시 `BookCopy`를 추가할 과제로 바꾸지 않아.

## 읽은 코드에서 바로 단정하면 안 되는 것

- 테스트가 있으니 모두 정상이다 → 실행 결과와 각 테스트가 확인하는 값이 필요해.
- 트랜잭션이 있으니 동시 요청도 안전하다 → 실제 겹치는 요청의 결과를 따로 확인해야 해.
- README에 없다고 쓰였으니 구현도 없다 → 같은 커밋의 소스와 맞춰봐야 해.
- 이전에 같은 구조를 썼으니 지금도 이해한다 → 새 요구에서의 설명·예측·수정 결과가 필요해.
- 프로젝트가 작으니 의미 없다 / 도메인이 많으니 취업 준비가 됐다 → 기능 수 대신 본인 판단과 검증의 증거를 봐야 해.

이 문서의 ‘학습 활용’은 코드 관찰을 바탕으로 만든 제안이야. 사용자 능력 평가, 채용 수준 판정, 지금 고쳐야 할 버그 목록으로 사용하지 않아.
