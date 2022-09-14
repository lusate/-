package jpabook.jpashop.domain.item;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item { //구현체를 가지고 할 거기 때문에 추상으로 해줌
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    //위 3개 변수 상속하는 얘를 만들어줌

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // ==비즈니스 로직==  (재고를 늘리고 줄임)
    //@Setter를 해서 변경하는 것이 아니라 여기 메서드들 안에서 변경 (이게 객체 지향적임)
    //재고 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
        //this.stockQuantity => 현재 나의 수량
    }

    //재고 감소
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity; //남은 수량
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        } //NotEnoughStockException 을 exception 패키지에 만들어줌
        this.stockQuantity = restStock;
    }


//    public void change(int price, String name, int stockQuantity) {
//        this.price = price;
//        this.name = name;
//        this.stockQuantity = stockQuantity;
//    }
}

//상속관계 매핑이기 때문에 상속관계 전략을 지정해줘야 하는데 전략을 부모 클래스에서 잡아줘야 한다.
//싱글 테이블 전략을 쓰므로 SINGLE_TABLE 을 쓴다.