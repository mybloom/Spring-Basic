package hello.core.autowired;

import static org.assertj.core.api.Assertions.*;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class AllBeanTest {

	//아래 관련코드 이해하지 않은 채로 넘어간다.
	@Test
	void findAllBean() {
		//생성자에 클래스 정보를 넘기면 해당 클래스가 스프링 빈으로 자동 등록된다.
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
			AutoAppConfig.class, DiscountService.class);

		DiscountService discountService = applicationContext.getBean(DiscountService.class);
		Member member = new Member(1L, "userA", Grade.VIP);
		int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

		assertThat(discountService).isInstanceOf(DiscountService.class);
		assertThat(discountPrice).isEqualTo(1000);

		int rateDiscountPolicy = discountService.discount(member, 20000, "rateDiscountPolicy");

	}

	static class DiscountService {

		private final Map<String, DiscountPolicy> policyMap;
		private final List<DiscountPolicy> policies;

		public DiscountService(
			Map<String, DiscountPolicy> policyMap,
			List<DiscountPolicy> policies) {
			this.policyMap = policyMap;
			this.policies = policies;
			System.out.println("policyMap = " + policyMap);
			System.out.println("policies = " + policies);
		}

		public int discount(Member member, int price, String discountCode) {
			DiscountPolicy discountPolicy = policyMap.get(discountCode);
			System.out.println("discountCode = " + discountCode);
			System.out.println("discountPolicy = " + discountPolicy);

			return discountPolicy.discount(member, price);
		}
	}
}
