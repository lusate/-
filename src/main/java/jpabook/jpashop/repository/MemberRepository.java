package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //스프링에서 제공하는 @Repository로 인해 컴포넌트 스캔에 의해서 자동으로 스프링 빈으로 관리가 된다.
//@RequiredArgsConstructor
public class MemberRepository {
    @PersistenceContext //JPA가 제공하는 표준 어노테이션
    private EntityManager em; //스프링이 EntityManager를 만들어서 인젝션해준다.

    //EntityManager는 @Autowired로 안되고 @PersistenceContext 가 있어야 인젝션이 가능하다.
    //스프링 부트는 @Autowired도 인젝션 되게 지원해준다. (Spring Data JPA가 있어야 가능하다.)

    /* 여기도 @RequiredArgsConstructor 사용할 수 있음
    private final EntityManager em;
    */
    public void save(Member member) { //저장
        em.persist(member);
    }

    public Member findOne(Long id) { //조회
        return em.find(Member.class, id); //멤버를 찾아서 반환
    }

    //리스트 조회
    public List<Member> findAll() {
        //전부 다 찾아야되서 jpql 사용
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}