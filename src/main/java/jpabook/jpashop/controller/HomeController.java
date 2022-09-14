package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j //lombok 쓰면 가능
public class HomeController {
    //Logger log = LoggerFactory.getLogger(getClass()); 이게 Slf4j임

    @RequestMapping("/") //첫 번째 화면을 이걸로 잡음
    public String home() {
        log.info("home controller");
        //log :
        return "home"; //home.html로 찾아가서 화면에 나오게 해줌
    }
}
