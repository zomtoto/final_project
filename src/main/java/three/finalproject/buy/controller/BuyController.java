package three.finalproject.buy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import three.finalproject.buy.domain.dto.BuyDTO;
import three.finalproject.buy.repository.BuyRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/buy/buys")
@RequiredArgsConstructor
public class BuyController {

    private final BuyRepository buyRepository;

    /*
    @GetMapping
    public String buys(Model model) {
        List<BuyDTO> buys = buyRepository.findAll();
        model.addAttribute("buys", buys);
        return "buy/buys";
    }*/

    @GetMapping
    public String buys(
            Model model,
            @RequestParam(defaultValue ="1") int page,
            @RequestParam(defaultValue ="10") int size) {

        int offset = (page - 1) * size;

        List<BuyDTO> buys = buyRepository.findPaginated(offset, size);

        // Total count of members
        int totalCount = buyRepository.countBuys();

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // Calculate startPage and endPage for pagination links
        int maxPagesToShow = 10;
        int startPage = Math.max(1, page - maxPagesToShow / 2);
        int endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);

        // Adjust startPage if near the end
        if (endPage - startPage + 1 < maxPagesToShow && startPage > 1) {
            startPage = Math.max(1, endPage - maxPagesToShow + 1);
        }

        //Add attributes to the model
        model.addAttribute("buys", buys);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageSize", size);
        return "buy/buys";
    }
}
