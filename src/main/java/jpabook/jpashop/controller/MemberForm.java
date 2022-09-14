package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}


/*
@NotEmpty() 사용 시 build.gradle 에 이거 쓰기
implementation 'org.springframework.boot:spring-boot-starter-validation'

아니면 스프링 부트 버전을 2.1.x 를 사용
 */