package three.finalproject.analyze.controller;

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
import three.finalproject.analyze.domain.dto.GraphDTO;
import three.finalproject.analyze.domain.dto.YearDTO;
import three.finalproject.analyze.repository.AnalyzeRepository;
import three.finalproject.analyze.repository.GraphRepository;
import three.finalproject.analyze.repository.YearRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/analyze/analyzes")
public class AnalyzeController {

    private final AnalyzeRepository analyzeRepository;
    private final YearRepository yearRepository;
    private final GraphRepository graphRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // List all analyzes with pagination
    @GetMapping
    public String getAnalyzes(Model model) {
        List<AnalyzeDTO> analyzes = analyzeRepository.findAll();

        model.addAttribute("analyzes", analyzes);

        return "analyze/analyzes";
    }

    @GetMapping("/{image_no}")
    public String analyze(@PathVariable Long image_no, Model model) {
        AnalyzeDTO analyze = analyzeRepository.getAnalyzeByImageNo(image_no);
        if(analyze == null) {
            log.warn("사진이 존재하지 않습니다.: {}", image_no);
            return "redirect:/analyze/analyzes";
        }
        model.addAttribute("analyze", analyze);
        return "analyze/analyze";
    }

/*    // Filter analyzes by year
    @GetMapping("/filter/{year}")
    public String filterByYear(@PathVariable int year, Model model) {
        List<AnalyzeDTO> analyzes = analyzeRepository.findByYear(year);
        model.addAttribute("analyze", analyzes);
        model.addAttribute("years", analyzeRepository.findUniqueYears());
        return "analyze/analyzes";
    }*/

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

    @GetMapping("/add")
    public String addAnalyze(Model model) {
        List<YearDTO> years = yearRepository.findAllYears();
        List<GraphDTO> graphs = graphRepository.findAllGraphs();

        model.addAttribute("analyze", new AnalyzeDTO());
        model.addAttribute("years", years);
        model.addAttribute("graphs", graphs);
        return "analyze/analyzeAddForm";
    }

    @PostMapping("/add")
    public String addAnalyze(
            @RequestParam("member_no") Long member_no,
            @RequestParam("analyze_year") Integer analyze_year,
            @RequestParam("graph_type") String graph_type,
            @RequestParam("image_name") String image_name,
            @RequestParam("image_description") String image_description,
            @RequestParam("file") MultipartFile file
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
        Long generatedImageNo = analyzeRepository.addImage(originPath, savePath, image_name, image_description);

        // Create and save AnalyzeDTO
        AnalyzeDTO analyzeDTO = new AnalyzeDTO();

        analyzeDTO.getImage().setImage_no(generatedImageNo);
        analyzeDTO.getMember().setMember_no(member_no);
        analyzeDTO.setAnalyze_year(analyze_year);
        analyzeDTO.setGraph_type(graph_type);
        analyzeDTO.getImage().setImage_name(image_name);
        analyzeDTO.getImage().setImage_description(image_description);

        // Save to the database
        analyzeRepository.addAnalyze(analyzeDTO);

        // Redirect to the analyze detail page with the new image_no
        return "redirect:/analyze/analyzes";
    }

}
