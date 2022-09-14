package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { //item은 JPA에 데이터 저장 전까지 Id가 없다.
            em.persist(item);
        }//그래서 JPA가 제공하는 persist를 사용
        else{ //이미 DB에 등록이 되서 가져온 것
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() { //여러 개 찾는거라면 JPQL 작성
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
