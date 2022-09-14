package jpabook.jpashop.controller;

import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    //모든 상품 공통 속성
    private long id;

    private String name;
    private int price;
    private int stockQuantity;

    //책 관련 속성
    private String author;
    private String isbn;
}