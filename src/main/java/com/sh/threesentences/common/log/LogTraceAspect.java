package com.sh.threesentences.common.log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Aspect
@Component
public class LogTraceAspect {

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    //    @Around("com.sh.threesentences.common.log.PointCuts.allController() ||"
    //        + " com.sh.threesentences.common.log.PointCuts.allService()")
    public Object controllerAroundLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Timestamp(System.currentTimeMillis()));

        HttpServletRequest request = null;
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        if (attribs != null) {
            request = (HttpServletRequest) ((NativeWebRequest) attribs).getNativeRequest();
        }

        String logUUID = UUID.randomUUID().toString();
        String clientIp = request.getRemoteAddr();
        String clientUrl = request.getRequestURL().toString();
        String callFunction =
            proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature()
                .getName();

        try {
            log.info("[START] | UUID: {} | timestamp: {} | clientIp: {} | clientUrl: {} | method: {}",
                logUUID, timeStamp, clientIp, clientUrl, callFunction);
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Timestamp(System.currentTimeMillis()));
            log.info(
                "[EXCEPTION] |  UUID: {} | timestamp: {} | clientIp: {} | clientUrl: {} | method: {} | exception: {}",
                logUUID, timeStamp, clientIp, clientUrl, callFunction, e.getMessage());
            throw e;
        } finally {
            timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Timestamp(System.currentTimeMillis()));
            log.info("[END] | UUID: {} | timestamp: {} | clientIp: {} | clientUrl: {} | method: {}",
                logUUID, timeStamp, clientIp, clientUrl, callFunction);
        }
    }
}
