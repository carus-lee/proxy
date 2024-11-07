package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;

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
}
