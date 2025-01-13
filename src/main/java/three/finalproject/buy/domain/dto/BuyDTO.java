package three.finalproject.buy.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import three.finalproject.member.domain.dto.MemberDTO;
import three.finalproject.product.domain.dto.ProductDTO;

@Getter
@Setter
@ToString
public class BuyDTO {

    private Long buy_no; //구매번호

    private String date; //구매날짜(YYY-MM-DD)
    private Integer quantity; //구매수량
    private String seal_service; //각인여부
    private Integer total_price; //총구매액
    private String method; //구매방식

    //DTO
    private MemberDTO member; //member_no
    private ProductDTO product; //product_no

    public BuyDTO() {
        this.member = new MemberDTO();
        this.product = new ProductDTO();
    }
}
