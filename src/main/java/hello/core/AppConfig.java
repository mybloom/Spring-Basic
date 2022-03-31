package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//method 명을 보는 순간 역할을 알 수가 있게 된다.
//application전체 구성을 빠르게 파악할 수 있다.

//application의 설정 정보
@Configuration
public class AppConfig {

	@Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

	@Bean
	public MemoryMemberRepository memberRepository() {
		return new MemoryMemberRepository();
	}

	@Bean
	public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(),discountPolicy());
    }

	@Bean
	public DiscountPolicy discountPolicy() {
		return new FixDiscountPolicy();
	}
}
