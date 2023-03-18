package com.sh.threesentences.common.log;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {

    @Pointcut("execution(* *..*Controller.*(..))")
    public void allController() {
    }

    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {
    }

}
