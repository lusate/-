package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional //쓰기
    public void saveItem(Item item) {
        itemRepository.save(item); //item 저장
    }

    @Transactional //변경 감지
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);

        //이렇게 단발성으로 하는 것보다 findItem.change(price, name,~);
        //과 같이 의미있는 메서드를 사용하는 것이 좋음.
        //findItem.change(price, name, stockQuantity);
    }
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}


//엔티티를 변경할 때는 항상 변경 감지를 사용하자. (merge 사용 X)

//Set. 해서 설정하지 말고 change(name, price, stockQuantity) 이런 식으로
//메서드를 넣는 것이 좋다.