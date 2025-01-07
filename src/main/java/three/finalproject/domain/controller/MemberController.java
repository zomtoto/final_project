package three.finalproject.domain.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import three.finalproject.domain.member.Member;
import three.finalproject.domain.member.MemberRepository;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/member/members")
public class MemberController {
    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    // Display details of a single member
    @GetMapping("/{member_no}")
    public String member(@PathVariable long member_no, Model model) {
        Member member = memberRepository.findByNo(member_no);
        if (member == null) {
            log.warn("Member not found: {}", member_no);
            return "redirect:/member/members";
        }
        model.addAttribute("member", member);
        return "member/member"; // Corresponding HTML: member.html
    }

    // Show the form to add a new member
    @GetMapping("/add")
    public String addMemberForm(Model model) {
        model.addAttribute("member", new Member()); // Initialize empty Member object
        return "member/addMember"; // Corresponding HTML: addMember.html
    }

    // Save the new member
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/addMember"; // Show form again if validation fails
        }
        memberRepository.save(member);
        return "redirect:/member/members"; // Redirect to member list
    }

    @GetMapping
    public String members(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // Calculate the offset and limit for pagination
        int offset = (page - 1) * size;

        // Fetch paginated members
        List<Member> members = memberRepository.findPaginated(offset, size);

        // Total count of members
        int totalCount = memberRepository.countMembers();

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

        // Add attributes to the model
        model.addAttribute("members", members);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageSize", size);

        return "member/members";
    }

    // Filter members by year
    @GetMapping("/filter/{year}")
    public String filterByYear(@PathVariable String year, Model model) {
        List<Member> members = memberRepository.findByJoinYear(year);
        model.addAttribute("members", members);
        model.addAttribute("years", memberRepository.findUniqueYears());
        return "member/members";
    }




}
