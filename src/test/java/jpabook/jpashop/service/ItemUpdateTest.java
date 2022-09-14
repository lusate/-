package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        //TX (트랜잭션)
        book.setName("이름 바꿈");

        //트랜잭션 커밋(commit)


    }
}


/*
변경 감지 == dirty checking
위처럼 이름 바꾸고 commit하게 되면 변경분에 대해서 JPA가 찾아서 업데이트 쿼리를 자동으로
생성해서 DB에 반영한다.
JPA의 엔티티를 바꿀 수 있다. (내가 원하는 데이터로 업데이트 가능)
엔티티의 값을 바꿔놓으면 JPA가  트랜잭션 커밋 시점에 바뀐 얘 찾아서 DB에 UPDATE문 날리고
트랜잭션 커밋한다. flush할 때 dirty checking이 일어난다.
이게 JPA에서 데이터 변경할 때 기본 메커니즘

준영속 엔티티 : 영속성 컨텍스트가 더는 관리하지 않는 엔티티

 */