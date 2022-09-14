package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //JPA가 실제 DB까지 돌아가는 것을 보기 위해 메모리 모드로 DB까지 모두 엮음
@SpringBootTest
@Transactional //트랜잭션 롤백이 가능해짐
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    //스프링 롤백 해버리면 JPA 입장에서 Insert 쿼리를 DB에 날리지 않는다.
    //롤백은 DB에 있는 것들을 모두 버리기 때문에 Insert 쿼리가 나가지 않음.
    //정확히 말하면 영속성 컨텍스트 flush 안 함.
    //롤백이지만 날린 쿼리 보고싶다면.
    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false) DB에 들어갔는지 확인하기 위함
    public void 회원가입() throws Exception {
        //given (이렇게 주어졌을 때)
        Member member = new Member();
        member.setName("lusate");

        //when (이렇게 하면)
        Long savedId = memberService.join(member);//join은 중복 회원 검증한 것

        //then (이렇게 해라 -> 결과)
        em.flush(); //영속성 컨텍스트에 있는 어떤 변경이나 등록 내용을 DB에 반영.
        assertEquals(member, memberRepository.findOne(savedId)); //member가 memberRepository에서 찾음
    }

    @Test(expected = IllegalStateException.class) //expected 해주면 try~catch 안 해줘도 됨.
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("lusate");

        Member member2 = new Member();
        member2.setName("lusate");

        //when
        memberService.join(member1);
        memberService.join(member2);

        /*
        try{
            memberService.join(member2); //예외 발생
        }catch(IllegalStateException e){
            return;
        }
        */
        //then
        fail("예외가 발생해야 한다.");
        //코드가 돌다가 여기로 오면 안됌. (오면 뭔가 잘못된 것)
    }
}