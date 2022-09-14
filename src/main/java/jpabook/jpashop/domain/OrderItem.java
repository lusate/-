package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//protected OrderItem() {} 기본 생성자 만들어서 setCount() 이런 식으로 만들지 못하게 막는다.
//유지보수가 어렵기 때문에
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량


    //== 비즈니스 로직 ==  Order 클래스에 있는 orderItem.cancel();
    public void cancel() {
        //OrderItem이 item을 가지고 있다.
        //item의 재고를 늘려야 한다.
        getItem().addStock(count); //아이템을 가져와서 재고를 다시 주문 수량만큼 늘려줘야 한다.
    }

    //주문 상품 전체 가격 조회.
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }


    //== 생성 메서드 OrderItem
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //주문했으니 재고를 줄여야 함.
        return orderItem;
    }
}
