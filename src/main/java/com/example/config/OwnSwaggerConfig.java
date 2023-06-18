package com.example.config;

import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class OwnSwaggerConfig implements ApiListingScannerPlugin
{
    /**
     * 实现此方法可手动添加ApiDescriptions
     */
    @Override
    public List<ApiDescription> apply(DocumentationContext context)
    {
        List<Parameter> parameters=new ArrayList<>();
        parameters.add(new ParameterBuilder().name("username").modelRef(new ModelRef("String")).defaultValue("test").description("用户名").build());

        parameters.add(new ParameterBuilder().name("password").modelRef(new ModelRef("String")).defaultValue("123").description("密码").build());

        Operation usernamePasswordOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("用户名密码登录")
                .notes("用户登陆获取token")
//赋值参数
                .parameters(parameters)
                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .build();

        ApiDescription loginApiDescription = new ApiDescription("用户服务", "/api/user/auth/login", "描述", Arrays.asList(usernamePasswordOperation), false);

        return Arrays.asList(loginApiDescription);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}