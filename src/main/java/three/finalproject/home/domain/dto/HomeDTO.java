package three.finalproject.home.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import three.finalproject.analyze.domain.dto.YearDTO;
import three.finalproject.member.domain.dto.MemberDTO;
import three.finalproject.product.domain.dto.ProductDTO;

@Getter
@Setter
@ToString
public class HomeDTO {


    private MemberDTO member;
    private ProductDTO product;
    private YearDTO year;

    private Integer yearly_amount; //연도별 누적 금액
    private Integer product_quantity; //상품별 누적 판매 개수

    public HomeDTO() {
        this.member = new MemberDTO();
        this.product = new ProductDTO();
        this.year = new YearDTO();
    }
}
