package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }// 빈 객체 BookForm() 을 넘겨줘서 추적이 가능. -> createMemberForm.html에서 *{name}
    //같은 것들 추적 가능.

    @PostMapping("/items/new")
    public String create(BookForm form) { //생성 메서드 사용해서 set 없애는 것이 더 객체 지향적임
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    //등록 조회는 간단한데 수정이 복잡함.
    //수정
    @GetMapping("items/{itemId}/edit") //변경 될 수 있기 때문에 {} 안에 넣음
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) { //itemId 매핑시켜 줌.
        Book item = (Book) itemService.findOne(itemId); //책만 가져옴.

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        //form을 업데이터 하는데 BookForm을 보낸다.

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }
    //@PathVariable:

    @PostMapping("items/{itemId}/edit")
    //파라미터로 넘어온 준영속 상태의 엔티티.
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
//        Book book = new Book();
//        book.setId(form.getId()); //form을 book으로 바꿈
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

        //어설프게 엔티티를 파라미터로 안 쓰고 정확하게 필요한 데이터만 받음
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:items";
    }//@ModelAttribute("form"):
}

/*
준영속 엔티티 : 영속성 컨텍스트가 더는 관리하지 않는 엔티티
Book 객체는 이미 DB에 한 번 저장되어서 식별자가 존재한다.
객체는 새로운 Book 객체이지만 Id가 세팅이 되어 있다. 이 말은 JPA에 한 번 들어왔다 나온 얘라는 것이다.
이런 객체를 준영속 상태의 객체라 한다.

JPA가 관리하는 영속상태의 엔티티는 변경 감지가 일어난다. 직접 new 로 해서 만들어낸 객체여서
JPA가 관리하지 않는다.

영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법이다.
트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)
이 동작해서 데이터베이스에 UPDATE SQL 실행
 */