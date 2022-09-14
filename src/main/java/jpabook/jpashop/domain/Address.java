package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter //값 타입은 기본적으로 immutable(불변) 해야 한다.
//Setter 버림
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //JPA 스펙상 엔티티나 임베디드 타입은 자바 기본 생성자를 public 또는 protected(안전)로 설정
    //JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록 지원해야 하기 때문이다.
    protected Address() {} //기본 생성자를 써야 프록시, 리플렉션 같은 기술을 쓸 수 있다.

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
