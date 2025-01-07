package three.finalproject.domain.product;

import lombok.Data;

import java.util.List;

@Data
public class Product {

    private Long product_no; //상품번호
    private Long category_no; //카테고리번호(FK)
    private String name; //상품이름
    private String company; //화시명
    private Integer in_price; //입고가
    private Integer out_price; //판매가
    private Integer sell_count; //판매량
    private Integer quantity; //재고
    private Integer visit; //조회수
    private Boolean seal_service; //각인서비스
    private Boolean delete; //삭제여부

    public Product() {
    }

    public Product(Long product_no, Long category_no, String name, String company,
                   Integer in_price, Integer out_price, Integer sell_count, Integer quantity,
                   Integer visit, Boolean seal_service, Boolean delete) {
        this.product_no = product_no;
        this.category_no = category_no;
        this.name = name;
        this.company = company;
        this.in_price = in_price;
        this.out_price = out_price;
        this.sell_count = sell_count;
        this.quantity = quantity;
        this.visit = visit;
        this.seal_service = seal_service;
        this.delete = delete;
    }
}
