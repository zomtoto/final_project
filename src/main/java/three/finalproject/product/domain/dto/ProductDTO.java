package three.finalproject.product.domain.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDTO {

    private Long product_no;

    @NotNull(message = "Category number cannot be null")
    private Long category_no; // 카테고리번호(FK)

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name; // 상품이름

    @Size(max = 100, message = "Company name must be up to 100 characters")
    private String company; // 회사명

    @NotNull(message = "In price cannot be null")
    @PositiveOrZero(message = "In price must be zero or positive")
    private Integer in_price; // 입고가

    @NotNull(message = "Out price cannot be null")
    @PositiveOrZero(message = "Out price must be zero or positive")
    private Integer out_price; // 판매가

    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity; // 재고

    @NotNull(message = "Seal service must not be null")
    private Boolean seal_service; // 각인서비스
    
    @NotNull
    private Boolean delete; //삭제여부
}
