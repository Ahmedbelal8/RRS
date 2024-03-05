package com.octane.rrs.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Order(2)
public class DtoMapperAdvice<T> implements ResponseBodyAdvice<T> {


    private static final Logger logger = LoggerFactory.getLogger(DtoMapperAdvice.class);
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }


    @Override
    public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        logger.debug("DtoMapperAdvice>>beforeBodyWrite");

        if (isNonJsonReturnType(selectedContentType)) {
            logger.debug("DtoMapperAdvice>>beforeBodyWrite | Returned body is not json ... return the body as it is");
            return body;
        }
        if (body == null) {
            logger.debug("DtoMapperAdvice>>beforeBodyWrite | Returned body is NULL ... return Null");
            return null;
        }
        if (isListTypeBody(body)) {
            return handleListTypeBody(body);
        } else if (body instanceof Map<?, ?>) {
            return handleMapTypeBody(body);
        } else if (isNotPageImpleTypeBody(body)) {
            return handleNonPageImplTypeBody(body);
        } else {
            return handlePageImplTypeBody(body);
        }
    }

    private T handlePageImplTypeBody(T body) {
        logger.debug("DtoMapperAdvice>>beforeBodyWrite>> Type Page found/");
        PageImpl<?> page = (PageImpl<?>) body;
        List list = new ArrayList();
        if (page.hasContent()) {
            Class classType = getTargetType(page.getContent().get(0));
            for (Object object : page) {
                Object dto = modelMapper.map(object, classType);
                list.add(dto);
            }
            logger.debug("DtoMapperAdvice>>beforeBodyWrite | Returned body is Page ... Page Dto Content: {}", list);
        }
        return (T) new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    private T handleNonPageImplTypeBody(T body) {
        Class type = getTargetType(body);
        if (type == null) {
            logger.debug("DtoMapperAdvice>>beforeBodyWrite | Target class type is null NULL ... return the body as it is");
            return body;
        }
        T dto = (T) modelMapper.map(body, type);
        logger.debug("DtoMapperAdvice>>beforeBodyWrite | Returned body is model ... Dto: {}", dto);
        return dto;
    }

    private boolean isNotPageImpleTypeBody(T body) {
        return !(body instanceof PageImpl<?>);
    }

    private T handleListTypeBody(T body) {
        logger.debug("DtoMapperAdvice>>beforeBodyWrite>> Type List found");
        List list = (List) body;
        List dtoList = new ArrayList();
        if (list.size() > 0) {
            Class classType = getTargetType(list.get(0));
            for (Object object : list) {
                Object dto = modelMapper.map(object, classType);
                dtoList.add(dto);
            }
            logger.debug("DtoMapperAdvice>>beforeBodyWrite | Returned body is List ... Dto List: {}", dtoList);
        }
        return (T) dtoList;
    }

    private T handleMapTypeBody(T body) {
        logger.debug("DtoMapperAdvice>>beforeBodyWrite>> Type Map found");
        Map<Object, Object> map = (Map) body;
        Map<Object, Object> dtoMap = new HashMap();
        if (map.size() > 0) {
            Map.Entry<Object, Object> firstEntry = map.entrySet().iterator().next();
            Object value = firstEntry.getValue();

            if (value instanceof List) {
                List list = (List) value;
                if (list.size() > 0) {
                    Class classType = getTargetType(list.get(0));
                    for (Map.Entry<Object, Object> entry : map.entrySet()) {
                        List dtoList = new ArrayList();
                        for (Object object : (List) entry.getValue()) {
                            Object dto = modelMapper.map(object, classType);
                            dtoList.add(dto);
                        }
                        dtoMap.put(entry.getKey(), dtoList);
                    }
                }
            }
        }
        return (T) dtoMap;
    }

    private boolean isListTypeBody(T body) {
        return body instanceof List;
    }

    private boolean isNonJsonReturnType(MediaType selectedContentType) {
        return !selectedContentType.includes(MediaType.APPLICATION_JSON);
    }


    private Class getTargetType(Object o) {
        try {
            String objectClassName = o.getClass().getName();
            String targetClassName = getTargetClassName(objectClassName);
            return Class.forName(targetClassName);
        } catch (ClassNotFoundException e) {
            logger.debug("DtoMapperAdvice >> beforeBodyWrite >> Type {} has no mapping Dto.", o.getClass());

            return o.getClass();
        }
    }


    private String getTargetClassName(String objectClassName) {
        logger.debug("getTargetClassNameBasedOnApiVersion() | start");
        String targetClassName = "";
        String objectClassNameReplacement = objectClassName.replace("model", "dto");
        if (objectClassName.endsWith("Dto")) {
            targetClassName = objectClassName.replace("dto", "model").replace("Dto", "");
        } else {
            targetClassName = objectClassNameReplacement;
        }
        logger.debug("getTargetClassNameBasedOnApiVersion() | target class name:{} ", targetClassName);
        return targetClassName + "Dto";
    }


}
