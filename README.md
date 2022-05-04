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
- <img src="https://user-images.githubusercontent.com/55780251/166690254-6a8a1141-7d62-418b-b54a-7d37e09e44cc.jpg">
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

> Dependency Lookup
1. 스프링 컨테이너의 빈을 대신 조회해주는 인터페이스.
  - ObjectFactory : getObject()만 제공
  - ObjectProvider: 나중에 나온 것으로 편의기능 추가적으로 더 제공
  - 스프링이 제공하는 기능을 사용하지만 기능이 단순해서 단위테스트를 만들거나 mock 코드 만들기는 훨씬 쉬워진다.

2. JSR-330 Provider 사용
 - `javax.inject.Provider` 라는 JSR-330 자바 표준 사용
 - gradle에 의존성을 추가해야 한다 : 별도의 라이브러리 사용
 - get() 메서드 하나로 기능이 매우 단순

> 프로토타입 스코프는 언제 사용할까?
- 의존관계 주입이 완료된 새로운 객체가 필요하면 사용
- 거의 사용할 일이 없다.

> 웹스코프
- 웹 환경에서만 동작
- 해당 스코프 종료 시점까지 스프링이 관리한다. 즉, 종료 메서드가 호출된다.
- 실습 : 웹 환경에서 동작하므로 웹 라이브러리 추가
    ```  
    implementation 'org.springframework.boot:spring-boot-starter'
    //web 라이브러리 추가
    implementation 'org.springframework.boot:spring-boot-starter-web'
    ``` 
> [유용] request 스코프 예제 개발
- **동시에 여러 HTTP 요청이 오면 정확히 어떤 요청이 남긴 로그인지 구분하기 어렵다.** 
- 이럴 때 사용하기 좋은 것이 request 스코프이다.
  - uuid를 로그의 접두어로 만든다.
  - 로그생성 공통 포멧: [UUID][requestURL] {message}
- 참고
  - requestUrl을 MyLogger에 저장하는 부분은 컨트롤러 보다는 공통 처리가 가능한 스프링 `인터셉터`나 `서블릿 필터` 같은 곳을 활용하는 것이 좋다. 
  - [x] [todo] 인터셉터를 사용해서 구현해보자!

> request 스코프 객체를 속성으로 가지고 있는 Controller의 의존성 주입 실패
- Controller 생성시 request스코프 속성의 의존관계 주입이 일어나는데, 아직 사용자 요청(web request)이 없기 때문에 의존관계 주입을 할 수 없다.
- 실제 고객요청이 왔을 때로 의존관계 주입을 지연해야 한다. 
- Provider를 이용해 해결해야 한다.
```
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'logDemoController' defined in file [C:\_NDATA\codesquad2022\spring\인프런김영한\core\out\production\classes\hello\core\web\LogDemoController.class]: Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'logDemoService' defined in file [C:\_NDATA\codesquad2022\spring\인프런김영한\core\out\production\classes\hello\core\web\LogDemoService.class]: Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.support.ScopeNotActiveException: Error creating bean with name 'myLogger': Scope 'request' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton; nested exception is java.lang.IllegalStateException: No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.
at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:800) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:229) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1372) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1222) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:582) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:542) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:953) ~[spring-beans-5.3.16.jar:5.3.16]
at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:918) ~[spring-context-5.3.16.jar:5.3.16]
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:583) ~[spring-context-5.3.16.jar:5.3.16]
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:145) ~[spring-boot-2.6.4.jar:2.6.4]
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:740) ~[spring-boot-2.6.4.jar:2.6.4]
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:415) ~[spring-boot-2.6.4.jar:2.6.4]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:303) ~[spring-boot-2.6.4.jar:2.6.4]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1312) ~[spring-boot-2.6.4.jar:2.6.4]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1301) ~[spring-boot-2.6.4.jar:2.6.4]
at hello.core.CoreApplication.main(CoreApplication.java:10) ~[classes/:na]
```

- 'myLogger': `Scope 'request' is not active` for the current thread
```
Caused by: org.springframework.beans.factory.support.ScopeNotActiveException: Error creating bean with name 'myLogger': Scope 'request' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton; nested exception is java.lang.IllegalStateException: No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:383) ~[spring-beans-5.3.16.jar:5.3.16]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.16.jar:5.3.16]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:276) ~[spring-beans-5.3.16.jar:5.3.16]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1389) ~[spring-beans-5.3.16.jar:5.3.16]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1309) ~[spring-beans-5.3.16.jar:5.3.16]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:887) ~[spring-beans-5.3.16.jar:5.3.16]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:791) ~[spring-beans-5.3.16.jar:5.3.16]
	... 33 common frames omitted
Caused by: java.lang.IllegalStateException: No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.
	at org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes(RequestContextHolder.java:131) ~[spring-web-5.3.16.jar:5.3.16]
	at org.springframework.web.context.request.AbstractRequestAttributesScope.get(AbstractRequestAttributesScope.java:42) ~[spring-web-5.3.16.jar:5.3.16]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:371) ~[spring-beans-5.3.16.jar:5.3.16]
	... 39 common frames omitted
```

> 스코프와 프록시
- 프록시 방식을 이용해서 ObjectProvide를 작성하는 것을 삭제할 수 있게 한다.
- request 스코프인 빈에 아래를 추가해준다.
`@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)`
- CGLIB라는 라이브러리로 내 클래스를 상속받은 가짜 프록시 객체를 만들어서 주입한다.
- 가짜 프록시 객체는 요청이 오면 그 때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
- 프록시 : 앞에서 요청을 받아서 대신 처리해주는 것

> 핵심 아이디어
- Provider이던 프록시이던, 진짜 객체 조회를 꼭 필요한 시점까지 `지연`처리하는 점. 
- 애노테이션 설정 변경만으로 원본 객체를 프록시 객체르 대체할 수 있다. 이것이 바로 `다형성과 DI컨테이너`가 가진 큰 강점이다!

> 주의점
- 특별한 scope는 특정한 곳에서 최소한으로 사용한다.
- 마치 싱글톤처럼 동작하게 만들 수 있지만, 다르게 동작하므로 주의해서 사용한다.
