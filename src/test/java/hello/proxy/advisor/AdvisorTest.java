package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice()); //Pointcut.TRUE => 항상 "참"인 포인트컷
        proxyFactory.addAdvisor(advisor); // 프록시 팩토리에 어드바이저(포인트컷 + 어드바이스) 추가
        ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();

        proxy.save();
        proxy.find();

        //AopUtils에서 제공하는 메서드는 ProxyFactory 통해서 만들어진 객체만 검증 가능
        log.info("AOP 프록시 여부={}, ", AopUtils.isAopProxy(proxy));
        log.info("JDK 동적 프록시 여부={}, ", AopUtils.isJdkDynamicProxy(proxy));
        log.info("CGLIB 여부={}, ", AopUtils.isCglibProxy(proxy));
        //테스트 결과
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        //직접 만든 Pointcut - MyPointCut() - 적용
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointCut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor); // 프록시 팩토리에 어드바이저(포인트컷 + 어드바이스) 추가
        ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    /**
     * 포인트컷 직접 구현
     */
    static class MyPointCut implements Pointcut {
        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            //메서드 비교
            return new MyMethodMatcher();
        }
    }

    /**
     * 메서드 비교 - MethodMatcher 인터페이스를 구현
     */
    @Slf4j
    static class MyMethodMatcher implements MethodMatcher {
        private String matchName = "save"; //포인트컷 적용할 메서드 이름
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName);
            log.info("포인트컷 호출 method={}, targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과={}", result);
            return result;
        }

        // false면 정적인 상단의 matches() 사용 - 캐싱 가능 (속도가 빠름)
        // true면 동적인 하단의 matches() 사용 - 내부 인수(args)를 받아서 처리하므로 속도 저하 (캐싱 불가) -
        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }
}
