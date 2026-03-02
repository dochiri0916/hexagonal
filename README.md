# Hexagonal Architecture Template

스프링 기반 애플리케이션에서 변경에 강한 구조를 유지하기 위한 최소 규칙 문서다.

## 핵심 원칙

1. 의존 방향은 항상 안쪽으로만 향한다.  
   `Presentation -> Application -> Domain`
2. Domain은 순수 모델로 유지한다.  
   스프링, JPA, 웹, 보안 기술에 의존하지 않는다.
3. 외부 연동은 Port로 추상화하고 Adapter에서 구현한다.
4. 계층 경계마다 모델을 분리한다.  
   `Request/Response`(Web), `Command/Query/Result`(UseCase), `Domain`, `Entity`
5. 내부 DB 식별자와 외부 공개 식별자(publicId)를 분리한다.

## 규칙과 이유

| 규칙 | 이유 |
| --- | --- |
| Presentation은 Application(UseCase)만 호출한다. | UI/API 변경이 비즈니스 로직에 전파되지 않게 하기 위해 |
| Application은 `port.out` 인터페이스에만 의존한다. | DB/JWT/외부 API 기술 교체 영향을 최소화하기 위해 |
| Infrastructure는 Port 구현만 담당한다. | 기술 상세와 비즈니스 규칙을 분리하기 위해 |
| Domain에서 프레임워크 import를 금지한다. | 핵심 도메인 규칙을 장기적으로 안정화하기 위해 |
| Web DTO(Request/Response)와 UseCase DTO(Command/Query/Result)를 분리한다. | 표현 계층 요구사항과 유스케이스 모델의 결합을 막기 위해 |
| API/토큰/로그에는 publicId만 노출한다. | 내부 DB 키 노출 위험을 줄이고 외부 계약 안정성을 확보하기 위해 |
| 비즈니스 예외는 의미 있는 타입으로 정의하고 HTTP 매핑은 Presentation에서 처리한다. | 도메인 의미와 전송 프로토콜 책임을 분리하기 위해 |

## 레이어별 필수 규칙

### Domain 규칙

| 규칙 | 이유 |
| --- | --- |
| 엔티티/VO 생성 시점에 불변식(유효성)을 검증한다. | 잘못된 상태가 시스템 안쪽으로 들어오는 것을 차단하기 위해 |
| 상태 변경은 도메인 메서드로만 수행하고 setter 남용을 금지한다. | 비즈니스 의미 없는 변경을 막고 규칙을 한 곳에 모으기 위해 |
| 도메인 예외는 기술 예외가 아닌 비즈니스 의미로 정의한다. | 실패 원인을 유스케이스 관점에서 일관되게 다루기 위해 |
| Domain에서 Spring/JPA/Web 의존을 금지한다. | 도메인 규칙 수명을 프레임워크 수명보다 길게 유지하기 위해 |

### Application 규칙

| 규칙 | 이유 |
| --- | --- |
| 유스케이스는 Application에서만 조합/오케스트레이션한다. | 흐름 제어 책임을 한 계층에 고정하기 위해 |
| 외부 접근(DB, 토큰, API)은 반드시 `port.out`을 통해 수행한다. | 기술 변경 시 유스케이스 코드 수정 범위를 줄이기 위해 |
| 트랜잭션 경계는 Application에서 선언한다. | 비즈니스 단위의 원자성을 유스케이스 기준으로 보장하기 위해 |
| Presentation DTO/Entity를 Application에 직접 노출하지 않는다. | 경계 모델 오염을 방지하기 위해 |

### Presentation 규칙

| 규칙 | 이유 |
| --- | --- |
| Controller는 검증, 변환, UseCase 호출, 응답 매핑만 수행한다. | HTTP 처리와 비즈니스 로직을 분리하기 위해 |
| 비즈니스 분기/정책 판단 코드는 Controller에 두지 않는다. | 규칙 중복과 엔드포인트별 동작 불일치를 막기 위해 |
| Request/Response는 웹 스키마 전용 모델로 유지한다. | API 계약 변경이 내부 모델에 전파되지 않게 하기 위해 |
| 예외를 HTTP 상태코드로 변환하는 책임은 Presentation이 가진다. | 도메인/애플리케이션을 전송 프로토콜로부터 분리하기 위해 |

### Infrastructure 규칙

| 규칙 | 이유 |
| --- | --- |
| Infrastructure는 Port 구현과 기술 설정만 담당한다. | 기술 상세를 바깥 계층에 격리하기 위해 |
| Entity는 영속성 모델로만 사용하고 Domain과 분리한다. | ORM 제약이 도메인 모델에 침투하는 것을 막기 위해 |
| Domain <-> Entity 변환은 Mapper에서 일관되게 처리한다. | 변환 규칙 중복과 누락을 줄이기 위해 |
| Infrastructure에서 유스케이스 흐름/규칙을 구현하지 않는다. | 핵심 비즈니스 규칙의 위치를 Application/Domain에 고정하기 위해 |

## 빠른 체크리스트

- Domain에 `org.springframework`, `jakarta.persistence` import가 없는가
- Application이 `presentation`, `infrastructure` 타입을 직접 참조하지 않는가
- Controller가 Repository/Entity를 직접 다루지 않는가
- Adapter가 `port.out` 인터페이스를 구현하는가
- API 응답/토큰/로그에 내부 DB id(Long)가 노출되지 않는가

## 한 줄 운영 원칙

비즈니스 규칙은 안쪽(Domain/Application)에, 기술 상세는 바깥쪽(Presentation/Infrastructure)에 둔다.
