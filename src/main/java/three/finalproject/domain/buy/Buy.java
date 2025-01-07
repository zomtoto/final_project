package three.finalproject.domain.buy;

import lombok.Data;

@Data
public class Buy {
    private Long buy_no; //구매번호
    private Long member_no;
    private Long product_no;
    private String date; //구매날짜(YYY-MM-DD)
    private Integer quantity; //구매수량
    private String seal_service; //각인여부
    private Integer total_price; //총구매액
    private String method; //구매방식

    public Buy() {
    }

    public Buy(Long buy_no, Long member_no, Long product_no, String date,
               Integer quantity, String seal_service, Integer total_price, String method) {
        this.buy_no = buy_no;
        this.member_no = member_no;
        this.product_no = product_no;
        this.date = date;
        this.quantity = quantity;
        this.seal_service = seal_service;
        this.total_price = total_price;
        this.method = method;
    }
}
