package com.simplespring.aop;

import com.simplespring.ioc.BeanPostProcessor;
import com.simplespring.ioc.factory.BeanFactory;
import com.simplespring.ioc.factory.BeanFactoryAware;
import com.simplespring.ioc.xml.XmlBeanFactory;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;


public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {
    private XmlBeanFactory xmlBeanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        /* 这里两个 if 判断很有必要，如果删除将会使程序进入死循环状态，
         * 最终导致 StackOverflowError 错误发生
         */
        if(bean instanceof AspectJExpressionPointcutAdvisor){
           return bean;
       }
        if(bean instanceof MethodInterceptor){
            return bean;
        }
        // 1.  从 BeanFactory 查找 AspectJExpressionPointcutAdvisor 类型的对象
        List<AspectJExpressionPointcutAdvisor>advisors =
                xmlBeanFactory.getBeanForType(AspectJExpressionPointcutAdvisor.class);
        for(AspectJExpressionPointcutAdvisor advisor : advisors){
            // 2. 使用 Pointcut 对象匹配当前 bean 对象
            if(advisor.getPoint().getClassFilter().matchers(bean.getClass())){
                ProxyFactory advisedSupport = new ProxyFactory();
                /**
                 * Interceptor继承了Aspect
                 */
                TargetSource targetSource = new TargetSource(bean,bean.getClass(),bean.getClass().getInterfaces());
                advisedSupport.setTargetSource(targetSource);
                advisedSupport.setMethodInterceptor((MethodInterceptor)advisor.getAdvice());
                advisedSupport.setMethodMatcher(advisor.getPoint().getMethodMatcher());
                return advisedSupport.getProxy();
            }
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws Exception {
        xmlBeanFactory = (XmlBeanFactory)beanFactory;
    }
}
