package three.finalproject.buy.domain;

public class Buy {
    private Long buy_no; //구매번호
    private Long member_no;
    private Long product_no;
    private String date; //구매날짜(YYY-MM-DD)
    private Integer quantity; //구매수량
    private String seal_service; //각인여부
    private Integer total_price; //총구매액
    private String method; //구매방식
}
