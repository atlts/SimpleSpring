package com.simplespring.aop;

import org.aopalliance.aop.Advice;
import com.simplespring.aop.PointcutAdvisor;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    private Advice advice;

    public void setExpression(String expression) {
        pointcut.setExpression(expression);
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Pointcut getPoint() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }
}
