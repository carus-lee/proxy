package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        //invocation.proceed()로 target클래스를 호출하고 그 결과를 받는다.
        //기존 파라미터(target, args 등)를 모두 처리.. invocation.getXXX()로 꺼내 쓸수 있다.
        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료, resultTime={}", resultTime);

        return result;
    }
}
