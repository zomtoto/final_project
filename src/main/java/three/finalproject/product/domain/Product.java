package three.finalproject.product.domain;

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

}
