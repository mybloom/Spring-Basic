## chapter7 의존 관계 주입  

1. 생성자 주입
2. 수정자 주입(setter)
3. 필드 주입
4. 일반 메서드 주입

> 각 의존관계 주입별 특징

- 생성자 주입
    - 생성자 호출 시점에 딱 1번만 호출되는 것이 보장
    - `불변, 필수` 의존관계에 사용


> 롬복 셋팅
- <img src="화면 캡처 2022-05-02 104334.jpg">
- ctrl + f12 : 클래스에 있는 메서드 보여주는 단축키
    - 해당 단축키로 확인하면 생성자(메서드로 표현되어있음)가 만들어진 것을 확인할 수 있다.
- 생성자가 1개이면 @Autowired 생략 가능하다.

### 조회 빈이 2개 이상 일 때
- @Autowired 필드명 매칭
- @Quilifier 끼리 매칭 -> 빈 이름 매칭
- @Primary 사용

> @Autowired 필드 명 매칭
- @Autowired는 타입 매칭을 시도하고 이때 여러 빈이 있으면 필드 명, 파라미터 명으로 찾아온다.


### 애노테이션 만들기

- @Quailfier("MainDiscountPolicy")라고 하면 타입 체크가 되지 않는다.
- 그러므로 애노테이션을 만들어보자!
- 무분별하게 쓰진말자. 하지만 퀄리파이어의 경우 문자열을 사용하므로 애노테이션 만들어 쓰는게 괜찮다고 생각한다고 하심.

### 조회 빈이 모두 필요할 때 

예시 : 고객이 할인의 종류(fix, rate)를 선택할 수 있을 때
스프링을 사용하면 전략 패턴을매우 간단하게 구현할 수 있다. 

---
## chap8 빈 생명주기 콜백

스프링 빈은 간단하게 `객체생성 -> 의존관계 주입` 과 같은 라이프사이클을 가진다. 
스프링 빈은 객체를 생성하고 의존관계 주입이 다 끝난 다음에 필요한 데이터를 사용할 수 있는 준비가 완료된다.

그렇다면 개발자가 의존관계 주입이 모두 완료된 시점을 어떻게 알 수 있을까?

스프링은 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공한다.
또한 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 준다.

1. 인터페이스 : InitializingBean, DisposableBean
2. 설정 정보에 초기화 메서드, 종료 메서드 지정
3. @PostConstruct, @PreDestory 애노테이션 지원

> 스프링 빈의 이벤트 라이프사이클
- 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백(싱글톤의 경우) -> 스프링 종료




