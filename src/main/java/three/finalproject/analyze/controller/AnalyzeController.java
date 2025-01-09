package three.finalproject.analyze.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import three.finalproject.analyze.domain.dto.AnalyzeDTO;
import three.finalproject.analyze.repository.AnalyzeRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/analyze/analyzes")
public class AnalyzeController {

    private final AnalyzeRepository analyzeRepository;

    /**
     * Display all analyses from the analyze_table on the analyzes.html page.
     */
    @GetMapping
    public String getAnalyzes(Model model) {
        // Retrieve all analyses from the repository
        List<AnalyzeDTO> analyzes = analyzeRepository.getAllAnalyzes();
        model.addAttribute("analyzes", analyzes);
        return "analyze/analyzes"; // Render the analyzes.html template
    }

    @GetMapping("/add")
    public String addAnalyze(Model model) {
        model.addAttribute("analyze", new AnalyzeDTO());
        return "analyze/analyzeAddForm";
    }

    @PostMapping("/add")
    public String addAnalyze(
            @RequestParam("member_no") Long memberNo,
            @RequestParam("analyze_year") Integer year,
            @RequestParam("graph_type") String graphType,
            @RequestParam("image_name") String imageName,
            @RequestParam("image_description") String imageDescription,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        // Define the upload directory inside resources
        String uploadDir = new File("src/main/resources/uploads").getAbsolutePath();

        // Create the directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file with a unique name
        String originalFilename = file.getOriginalFilename();
        String savedFilename = System.currentTimeMillis() + "_" + originalFilename;
        File savedFile = new File(uploadDir + File.separator + savedFilename);
        file.transferTo(savedFile);

        // Prepare paths for the database
        String originPath = originalFilename;
        String savePath = "/uploads/" + savedFilename; // Relative path for database

        // Create and save AnalyzeDTO
        AnalyzeDTO analyzeDTO = new AnalyzeDTO();
        analyzeDTO.setMember_no(memberNo);
        analyzeDTO.setAnalyze_year(year);
        analyzeDTO.setGraph_type(graphType);
        analyzeDTO.setImage_name(imageName);
        analyzeDTO.setImage_description(imageDescription);

        // Save to the database
        Long imageNo = analyzeRepository.addImage(originPath, savePath);
        analyzeDTO.setImage_no(imageNo);
        analyzeRepository.addAnalyze(analyzeDTO, imageNo);

        return "redirect:/analyze/analyzes";
    }
}
