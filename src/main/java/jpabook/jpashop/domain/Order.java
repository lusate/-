package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
//@Table 하는 이유 :
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //Order 와 Member는 다대일 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") //매핑을 뭘로 할 건지 설정
    private Member member; //주문 회원
    //member_id 외래키 값이 다른 member로 변경이 된다.

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    //ALL -> Order 저장할 때 delivery도 같이 persist 된다. (원래라면 delivery도 직접 persist, Order도 직접 persist)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송 정보

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태 -> ORDER, CANCEL

    // ==연관관계 편의 메서드==
    public void setMember(Member member) { //Member와 Order가 양방향
        this.member = member; //Order 입장에서 member를 넣어야 함.
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) { //OrderItem 과 Order가 양방향
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery() { //Order 와 Delivery 가 양방향
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==(주문) 생성 메서드==
    //정적 팩토리 메서드라는 패턴
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); //처음 status 를 ORDER롤 설정
        order.setOrderDate(LocalDateTime.now()); //주문 시간 정보
        return order;
    }
    //자바 가변인자 (OrderItem...)

    //==비즈니스 로직 (주문 취소)
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {//배송이 이미 시작하면 취소 불가능
            throw new IllegalStateException("이미 배송 완료된 상품은 취호가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); //한 번 주문할 때 고객이 상품 n개 주문할 수 있음.
            //이 n개에 모두 cancel을 날려주어야 함.
        }
    }


    //== 조회 로직 필요 ==
    //전체 주문 가격 조회.
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        //자바 람다 or 스트림 써서 간단하게 구현 가능

        /*int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
            //주문할 때 주문 가격(orderPrice)과 수량(count)이 OrderItem 클래스에 있다.
            //둘을 곱해야 한다.
        }

        return totalPrice;*/
    }
}
