package three.finalproject.analyze.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import three.finalproject.analyze.domain.dto.AnalyzeDTO;
import three.finalproject.analyze.repository.AnalyzeRepository;
import three.finalproject.image.domain.dto.ImageDTO;
import three.finalproject.image.repository.ImageRepository;
import three.finalproject.login.LoginService;
import three.finalproject.member.repository.MemberRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/analyze/analyzes")
public class AnalyzeController {

    private final AnalyzeRepository analyzeRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final LoginService loginService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public String getAnalyzes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        // 1) offset 구하기 (주의: 페이지가 1부터 시작하므로, offset = (page - 1) * size)
        int offset = (page - 1) * size;

        // 2) DB에서 해당 페이지만 가져옴
        List<AnalyzeDTO> analyzes = analyzeRepository.findPaginated(offset, size);

        // 3) 전체 개수
        int totalCount = analyzeRepository.countAnalyzes();

        // 4) 페이지 계산
        int totalPages = (int) Math.ceil((double) totalCount / size);

        int maxPagesToShow = 10;
        int startPage = Math.max(1, page - maxPagesToShow / 2);
        int endPage = Math.min(totalPages, startPage + maxPagesToShow - 1);
        if (endPage - startPage + 1 < maxPagesToShow && startPage > 1) {
            startPage = Math.max(1, endPage - maxPagesToShow + 1);
        }

        // 5) 모델에 담아 뷰로 전달
        model.addAttribute("analyzes", analyzes);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageSize", size);

        return "analyze/analyzes";
    }

    @GetMapping("/add")
    public String addAnalyze(Model model) {
        model.addAttribute("analyze", new AnalyzeDTO());
        return "analyze/analyzeAddForm";
    }

    @PostMapping("/add")
    public String addAnalyze(
            @RequestParam("analyze_year") Integer year,
            @RequestParam("graph_type") String graphType,
            @RequestParam("image_name") String image_name,
            @RequestParam("image_description") String image_description,
            @RequestParam("file") MultipartFile file,
            HttpSession session
    ) throws IOException {
        // Ensure the upload directory exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                throw new IOException("Failed to create the upload directory: " + uploadDir);
            }
        }

        // Generate a unique file name
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException("Invalid file: No name provided.");
        }

        String savedFilename = System.currentTimeMillis() + "_" + originalFilename;

        // Use File to construct the full path
        File savedFile = new File(directory, savedFilename);
        file.transferTo(savedFile);

        // Prepare paths for the database
        String originPath = savedFilename;
        String savePath = uploadDir +"/"+ savedFilename; // Relative path for database

        // 1) DB에 새 이미지 정보 INSERT → 생성된 PK(=image_no) 획득
        Long generated_image_no = analyzeRepository.addImage(originPath, savePath, image_name, image_description);

        // Create and save AnalyzeDTO
        AnalyzeDTO analyzeDTO = new AnalyzeDTO();

        Long member_no = (Long) session.getAttribute("member_no");

        analyzeDTO.getImage().setImage_no(generated_image_no);
        analyzeDTO.getMember().setMember_no(member_no);
        analyzeDTO.setAnalyze_year(year);
        analyzeDTO.setGraph_type(graphType);
        analyzeDTO.getImage().setImage_name(image_name);
        analyzeDTO.setImage_description(image_description);

        // Save to the database
        analyzeRepository.addAnalyze(analyzeDTO, generated_image_no, member_no);

        // Redirect to the analyze detail page with the new image_no
        return "redirect:/analyze/analyzes/" + generated_image_no;
    }

    @GetMapping("/{image_no}")
    public String analyze(@PathVariable Long image_no, Model model) {
        ImageDTO image = imageRepository.findByNo(image_no);
        AnalyzeDTO analyze = analyzeRepository.findByImageNo(image_no);
        if(image == null) {
            log.warn("사진이 존재하지 않습니다.: {}", image_no);
            return "redirect:/analyze/analyzes";
        }
        model.addAttribute("image", image);
        model.addAttribute("analyze", analyze);
        return "analyze/analyze";
    }

    // Edit an analysis (form)
    @GetMapping("/{image_no}/edit")
    public String editAnalyze(@PathVariable Long image_no, Model model) {
        AnalyzeDTO analyze = analyzeRepository.getAnalyzeByImageNo(image_no);
        if (analyze == null) {
            return "redirect:/analyze/analyzes";
        }
        model.addAttribute("analyze", analyze);
        return "analyze/analyzeEditForm";
    }

    // Save the edited analysis
    @PostMapping("/{image_no}/edit")
    public String updateAnalyze(
            @PathVariable Long image_no,
            @Valid @ModelAttribute AnalyzeDTO analyzeDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "analyze/analyzeEditForm";
        }
        analyzeRepository.updateAnalyze(image_no, analyzeDTO);
        return "redirect:/analyze/analyzes";
    }



}
