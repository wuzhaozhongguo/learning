package com.hans.jhd.domain.message.facade.impl.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.hans.jhd.domain.message.core.exception.ApplicationException;
import com.hans.jhd.domain.message.core.utils.MailUtils;

import org.albert.common.constant.system.ConstLayerNumber;
import org.albert.common.constant.system.domain.ConstDomainNumber;
import org.albert.common.constant.system.infra.ConstInfraNumber;
import org.albert.common.domain.Response;
import org.albert.common.exception.BaseException;
import org.albert.common.exception.business.BusinessException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

@Aspect
@Named
public class FacadeInterceptor {

    private Logger logger  = LoggerFactory.getLogger(this.getClass());

    private List<String> receivers = new ArrayList(){
        {
            add("765105646@qq.com");
        }
    };



    @Pointcut("execution(* com.hans.jhd.domain.message.facade.impl.*.*(..))")
    private void anyMethod(){}

    @Around("anyMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
        Long startTime = System.currentTimeMillis();
        Object returnDate = new Response<>();
        Exception exception = null;
        try {
            returnDate = pjp.proceed();//执行
        } catch (Exception exc) {
            exception = exc;
        }
        //处理返回值
        returnDate = this.dealReturnDate(returnDate,exception);
        //打印日志
        this.printLog(pjp,returnDate,exception,startTime);
        return returnDate;
    }



    /**
     * 打印日志
     * @param pjp
     * @param returnDate
     * @param exception
     * @param startTime
     */
    private void printLog(ProceedingJoinPoint pjp,Object returnDate,Exception exception,Long startTime){
        Long endTime = System.currentTimeMillis();
        String methodName = this.getMethodName(pjp);
        Object[] args = pjp.getArgs();
        String parm = this.getParms(args);
        String errMessage;
        String returnStr = returnDate!=null ?JSONObject.toJSONString(returnDate):"没有返回值";
        String time = ""+(endTime - startTime)+"ms";
        if (exception!=null){
            exception.printStackTrace();
            errMessage = exception.getMessage();
        }else{
            errMessage = "无异常.";
        }
        logger.debug("\n=====执行方法:{}.\n参数:{}.\n返回结果:{}.\n耗时:{}.\n异常信息:{}",methodName,parm,returnStr,time,errMessage);
    }

    /**
     * 处理返回值
     * @param returnDate
     * @param exception
     */
    private Object dealReturnDate(Object returnDate,Exception exception){
        //没有异常直接返回
        if(exception == null){
            return returnDate;
        }

        String emailSubject = "";
        String emailContent = "";
        if (exception instanceof ApplicationException){//业务异常
            ApplicationException applicationException = (ApplicationException) exception;
            applicationException.code = ConstDomainNumber.MESSAGE+applicationException.code;
            returnDate = new Response(applicationException);
            emailSubject = applicationException.message;
        }else{//未捕获的异常与系统异常
            Response response = new Response();
            response.setCode(Response.CodeEnum.UNKNOWN.code);
            response.setMsg("server err.");
            returnDate = response;
            emailSubject = "未知异常";
            emailContent = ExceptionUtils.getStackTrace(exception);
        }
        //发邮件
        MailUtils.sendEmail(emailSubject,emailContent,receivers);
        return returnDate;
    }

    private String getMethodName(ProceedingJoinPoint pjp){
        String method = pjp.getStaticPart().getSignature().getDeclaringType().getName();
        String name = pjp.getStaticPart().getSignature().getName();
        return method+"."+name;
    }

    private String getParms(Object[] args){
        StringBuffer parm = new StringBuffer("(");
        if (ArrayUtils.isEmpty(args)){
            parm.append("无参数");
            return parm.toString();
        }
        for (Object arg : args){
            parm.append(arg).append(",");
        }
        parm.append(")");
        return parm.toString();
    }
}
