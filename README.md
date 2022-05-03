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
   - 단점 : 스프링 전용 인터페이스, 해당 코드가 스프링 전용 인터페이스에 의존한다. 
   - 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
2. 설정 정보에 초기화 메서드, 종료 메서드 지정 
   - @Bean(initMethod = "init", destroyMethod = "close")로 메서드 지정
   - 장점: 외부 라이브러리에 적용 가능! 
   - destroyMethod는 inferred(추론)으로 등록되어 있어서 지정하지 않아도 "close" 또는 "shutdown"이 자동호출 된다
3. @PostConstruct, @PreDestory 애노테이션 지원
   - `자바`에서 지원하는 애노테이션 : javax 패키지
   - 스프링에 종속적인 기술이 아니라서 다른 컨테이너에서도 동작한다.
   - @ComponentScan 과도 잘 동작한다.
     - import javax.annotation.PostConstruct;
     - import javax.annotation.PreDestroy;
   - 단점 : 외부 라이브러리에서 사용할 수 없다.

> 스프링 빈의 이벤트 라이프사이클
- 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸전 콜백(싱글톤의 경우) -> 스프링 종료

## chap9 빈 스코프

- 싱글톤 : 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프
- 프로토타입 
- 웹 관련 스코프
  - request : 웹 요청이 들어오고 나갈 때 까지 유지되는 스코프
  - session : 웹 세션이 생성되고 종료될 때까지 유지되는 스코프
  - application : 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프

> 프로토타입 스코프

- 프로토파입 스코프는 조회할 때마다 항상 새로운 인스턴스를 생성해서 반환한다.
- 그러므로 스프링 컨테이너는 관리하지 않는다. 
- **그래서 @PreDestory와 같은 종료 메서드 호출되지 않는다.**
  - 프로토타입을 조회한 클라이언트가 수동으로 해당 Bean의 close를 호출해야 한다.
  `prototypeBean1.destroy();`

> 프로토타입 스코프, 싱글톤 빈과 함께 사용 시 문제점
- 싱글톤 빈에서 프로토타입 스코프의 빈을 사용하면 생성시점에 이미 프로토타입 빈의 의존성 주입이 된 상태이다.
- 프로토타입 스코프 빈을 새로 생성하면서 사용하고 싶다면 즉, 정말 의도한대로 프로토타입 빈을 사용하고 싶다면 다른 방법을 사용해야 한다.