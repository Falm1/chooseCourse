package com.example.config;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.VO.UserVO;
import com.example.entity.domain.AuthUser;
import com.example.mapper.UserMapper;
import com.example.utils.RestBean;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.constant.UserConstant.USER_LOGIN_STATE;
import static com.example.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * 配置Security登录
 */
@Api(tags = "用户登录")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    UserMapper userMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()                    //设置页面访问权限
                //.authorizeRequests()  方法已弃用
                //.antRequest()方法已经启用
                .antMatchers("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs",
                        "/swagger-ui.html", "/**/*.js", "/**/*.css", "/**/*.png",
                        "/**/*.ico", "/**/*.gif", "/**/*.jpg").permitAll()
                .antMatchers("/user/auth/**", "39.101.78.157/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()                                            //设置表单登录相关接口
                .loginProcessingUrl("/user/auth/login")
                .successHandler(this::onAuthenticationSuccess)
                .failureHandler(this::onAuthenticationFailure)
                .and()
                .logout()                                               //设置登出接口
                .logoutUrl("/user/auth/logout")
                .logoutSuccessHandler(this::onAuthenticationSuccess)
                .and()
                .csrf().disable()                                       //关闭跨域请求拦截，这个为一个页面登录成功后
                //若另外一个页面没登陆，请求一登陆的页面，则使用CORS
                .cors()
                .configurationSource(this.configurationSource())
                .and()
                .exceptionHandling()                                    //设置异常处理接口
                .authenticationEntryPoint(this::commence)
                .and()
                .build();
    }


    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 功能：设置哪些页面可以跨域资源共享
     * 允许页面：此处设为全部允许
     * */
    //跨域请求配置
    private CorsConfigurationSource configurationSource(){
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    //成功处理
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //在响应中主动告诉浏览器使用UTF-8编码格式来接收数据
        response.setHeader("Content-Type", "text/html;charset=UTF-8");

        //可以使用封装类简写Content-Type，使用该方法则无需使用setCharacterEncoding
        response.setContentType("text/html;charset=UTF-8");
        //如果请求时login结尾的，表明这时登录成功的返回
        if(request.getRequestURL().toString().endsWith("login")){
            //使用Spring-Security后，登录成功会将用户信息保存到Security的User类里面
            User userName = (User)authentication.getPrincipal();                        //这个是Security默认保存的，只保存了用户名
            AuthUser user = userMapper.getUserByUsername(userName.getUsername());
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            request.getSession().setAttribute(USER_LOGIN_STATE, userVO);
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登陆成功")));
        }else{
            //处理登出成功返回内容
            request.getSession().removeAttribute(USER_LOGIN_STATUS);
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功")));

        }
    }

    //失败处理
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        //在响应中主动告诉浏览器使用UTF-8编码格式来接收数据
        response.setHeader("Content-Type", "text/html;charset=UTF-8");

        //可以使用封装类简写Content-Type，使用该方法则无需使用setCharacterEncoding
        response.setContentType("text/html;charset=UTF-8");
        //如果请求时login结尾的，表明这时登录成功的返回
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(4001, e.getMessage(), "用户名或密码错误")));
    }

    //统一异常处理，只要Security处理过程发生异常都会走这里
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        //在响应中主动告诉浏览器使用UTF-8编码格式来接收数据
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        //可以使用封装类简写Content-Type，使用该方法则无需使用setCharacterEncoding
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(4000, e.getMessage(), "服务器异常")));
    }

}
