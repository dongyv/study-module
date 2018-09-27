package application.controller;

import application.config.HttpConfig;
import application.modle.*;
import application.resource.LoanBalance;
import application.resource.Random;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HelloWorldController {
    /**
     * 负载均衡
     */
    @RequestMapping("/load")
    @ResponseBody
    public void load(HttpServletRequest request, HttpServletResponse response){
        LoanBalance loan = new Random();
        //获取转发的ip地址
        String ip = loan.getServer();
        if(request.getMethod().equals(HttpConfig.getMethod)){
            try {
                String url = HttpConfig.getUrl(ip,request);
                //重定向
                response.sendRedirect(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(request.getMethod().equals(HttpConfig.postMethod)){
            //使用工厂模式生产每个模块的url
            HandlerFactory memberFactory = new HandlerFactory(new MemberHandler(ip),request);
            HandlerFactory userFactory = new HandlerFactory(new UserHandler(ip),request);
            String memberUrl = memberFactory.productRequest().getUrl();
            String userUrl = userFactory.productRequest().getUrl();
            //使用责任链模式，将每个模块进行对应转发
            String url = RequestHandlerAll.getRequestHandler(ip).handleRequest(request);
        }
    }

}