package com.vmock.base.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.vmock.base.constant.CommonConst;
import com.vmock.biz.entity.MockLog;
import com.vmock.biz.entity.MockResponse;
import com.vmock.biz.entity.MockUrl;
import com.vmock.biz.service.IMockLogService;
import com.vmock.biz.service.IMockResponseService;
import com.vmock.biz.service.IMockUrlLogicService;
import com.vmock.biz.service.IMockUrlService;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 主要业务过滤器处理类
 *
 * @author vt
 * @since 2019年12月4日
 */
@Component
public class MockFilterHandler {


    /**
     * get urls service
     */
    @Autowired
    private IMockUrlService mockUrlService;

    /**
     * get responses service
     */
    @Autowired
    private IMockResponseService responseService;

    /**
     * logic service
     */
    @Autowired
    private IMockUrlLogicService mockUrlLogicService;

    /**
     * log
     */
    @Autowired
    private IMockLogService logService;

    /**
     * 发射消息
     *
     * @param response 响应
     */
    @SneakyThrows
    private static void outMsg(String msg, HttpServletResponse response) {
        @Cleanup PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.flush();
    }

    /**
     * 执行处理
     *
     * @param request
     * @param response
     */
    public boolean execute(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        // get request url path
        String requestUrl = getProcessedUrl(request);
        // mockUrl entity
        // 1. 最好情况，直接命中，可能Path传参的url
        MockUrl mockUrl = mockUrlService.selectMockUrlByUrl(requestUrl);
        // 2. 可能有path传参，则需要根据logic推测命中了某个url
        String requestUrlLogic = null;
        if (mockUrl == null) {
            // 获取url logic字符串
            requestUrlLogic = mockUrlLogicService.selectLogicStrByUrl(requestUrl);
            mockUrl = mockUrlService.selectMockUrlByLogic(requestUrlLogic);
        }
        // 3. 最坏情况，path传参但是命中了其他sub_url,导致反推没找到url，则需要创建一个支持正则的Connection
        if (mockUrl == null) {
            mockUrl = mockUrlService.getUrlByRegexp(requestUrlLogic);
        }
        // 如果还是没有url, 那是真没有了
        if (mockUrl == null) {
            this.notFondUrl(response);
            return false;
        }
        // 没找到返回体 ----
        MockResponse mockResponse = responseService.selectMainResponse(mockUrl.getUrlId());
        if (mockResponse == null) {
            this.notFondResponse(response);
            return false;
        }
        // 方法校验  ----
        if (!request.getMethod().equalsIgnoreCase(mockResponse.getMethod())) {
            this.methodNotVaild(response);
            return false;
        }
        // 成功返回场合
        successAndLog(mockUrl, mockResponse, request, response);
        return true;
    }

    /**
     * 处理url
     *
     * @param request url
     * @return 处理过的URL
     */
    private String getProcessedUrl(HttpServletRequest request) {
        // get request url path
        String requestUrl = request.getRequestURI();
        //去除前缀(contextPatn)
        requestUrl = StrUtil.removePrefix(requestUrl, CommonConst.RESTFUL_PATH);
        // 空的话匹配 [/]
        requestUrl = StrUtil.isBlank(requestUrl) ? StrUtil.SLASH : requestUrl;
        return requestUrl;
    }

    /**
     * 没找到url的情况
     *
     * @param response
     */
    private void notFondUrl(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        outMsg("SYSTEM_MESSAGE：该路径未在系统中配置，请在[接口一览]模块进行配置。", response);
    }

    /**
     * 没找到response的情况
     *
     * @param response
     */
    private void notFondResponse(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        outMsg("SYSTEM_MESSAGE：系统存在该路径，但是还没有启用一个返回体，请与[接口一览 > 返回体]模块进行配置。", response);
    }

    /**
     * 方法错误
     *
     * @param response
     */
    private void methodNotVaild(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        outMsg("SYSTEM_MESSAGE：HTTP请求方式与系统中配置不匹配。", response);
    }

    /**
     * 成功场合
     *
     * @param mockResponse
     * @param response
     */
    private void successAndLog(MockUrl mockUrl, MockResponse mockResponse,
                               HttpServletRequest request, HttpServletResponse response) {
        // 日志处理，只有成功命中的场合才记录详细，防止产生过多垃圾数据。
        JSONObject responseDetailJson = new JSONObject();
        MockLog mockLog = new MockLog();
        // 命中url，系统中配置的，可能是带path占位符的
        mockLog.setHitUrl(mockUrl.getUrl());
        // 实际url
        mockLog.setRequestUrl(getProcessedUrl(request));
        // ip
        mockLog.setRequestIp(ServletUtil.getClientIP(request));
        // request method
        mockLog.setRequestMethod(request.getMethod());
        // request detail
        mockLog.setRequestDetail(requestToJson(request));
        // header逻辑处理
        String customHeader = mockResponse.getCustomHeader();
        if (StrUtil.isNotBlank(customHeader)) {
            // 将custom header存储的json 反序列化，并遍历存入header.
            JSONArray jsonArray = new JSONArray(customHeader);
            jsonArray.forEach(jsonItem -> {
                String key = ((JSONObject) jsonItem).getStr("key");
                String val = ((JSONObject) jsonItem).getStr("val");
                response.addHeader(key, val);
            });
            // header
            responseDetailJson.put("respHeader", jsonArray);
        }
        // 默认返回contentType为json
        String contentType = response.getContentType();
        if (StrUtil.isBlank(contentType)) {
            response.setContentType(ContentType.JSON.toString(UTF_8));
        }
        // 响应http码
        responseDetailJson.put("respStatus", mockResponse.getStatusCode());
        response.setStatus(mockResponse.getStatusCode());
        // 相应内容
        responseDetailJson.put("respContent", mockResponse.getContent());
        mockLog.setResponseDetail(responseDetailJson.toString());
        // 异步插入
        logService.asyncInsert(mockLog);
        outMsg(mockResponse.getContent(), response);
    }

    /**
     * 包装请求参数为json
     *
     * @param request 请求
     * @return requestJson
     */
    @SneakyThrows
    private String requestToJson(HttpServletRequest request) {
        JSONObject requestJsonObj = new JSONObject();
        // get all header
        Map<String, String> headerMap = ServletUtil.getHeaderMap(request);
        requestJsonObj.put("headers", headerMap);
        // get all param
        Map<String, String> paramMap = ServletUtil.getParamMap(request);
        requestJsonObj.put("params", paramMap);
        // body
        @Cleanup BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        requestJsonObj.put("body", body);
        return requestJsonObj.toString();
    }
}
