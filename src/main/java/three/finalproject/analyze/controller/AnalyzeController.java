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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // 1) analyzes 목록 자체는 계속 model에 저장
        model.addAttribute("analyzes", analyzes);

        // 2) AnalyzeDTO에서 graph.graph_type을 추출하여 중복 없이 Set에 담기
        //    (혹은 a.getGraph_type() 필드를 사용해도 됨)
        Set<String> graphTypes = new HashSet<>();
        for (AnalyzeDTO dto : analyzes) {
            if (dto.getGraph() != null && dto.getGraph().getGraph_type() != null) {
                graphTypes.add(dto.getGraph().getGraph_type());
            }
        }

        // 3) Set을 List로 변환하여 정렬할 수도 있음
        List<String> graphTypeList = new ArrayList<>(graphTypes);
        // Collections.sort(graphTypeList);  // 필요하다면 정렬

        // 4) model에 담아서 뷰로 전달
        model.addAttribute("graphTypes", graphTypeList);

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

        // 로그 혹은 간단한 print로 확인
        if (graphs.isEmpty()) {
            log.warn("========== [Graph List is EMPTY!] ==========");
        } else {
            for (GraphDTO g : graphs) {
                log.info("GRAPH DTO => {}", g);
            }
        }

        model.addAttribute("years", years);
        model.addAttribute("graphs", graphs);

        return "analyze/analyzeAddForm";
    }


    @PostMapping("/add")
    public String addAnalyze(
            @RequestParam("member_no") Long member_no,
            @RequestParam("analyze_year") Integer analyze_year,
            @RequestParam(name="graph_no") String graphNoString,
            @RequestParam("image_name") String image_name,
            @RequestParam("image_description") String image_description,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws IOException {

        Long graph_no = Long.valueOf(graphNoString);
        if (graph_no == -1L) {
            // 에러 메시지를 모델에 담아 다시 폼으로 돌려보냄
            model.addAttribute("errorMessage", "그래프를 선택하셔야 합니다.");

            // 필요하면 기존 입력값(멤버, 연도, 이미지 이름 등)도 모델에 다시 담아줌
            model.addAttribute("member_no", member_no);
            model.addAttribute("analyze_year", analyze_year);
            model.addAttribute("image_name", image_name);
            model.addAttribute("image_description", image_description);

            // 다시 드롭다운에 필요한 데이터도 담아줌
            List<YearDTO> years = yearRepository.findAllYears();
            List<GraphDTO> graphs = graphRepository.findAllGraphs();
            model.addAttribute("years", years);
            model.addAttribute("graphs", graphs);

            return "analyze/analyzeAddForm"; // 다시 폼으로 이동
        }


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
        String savePath = uploadDir + "/" + savedFilename; // Relative path for database

        // 1) DB에 새 이미지 정보 INSERT → 생성된 PK(=image_no) 획득
        Long generatedImageNo = analyzeRepository.addImage(originPath, savePath, image_name, image_description);

        // Create and save AnalyzeDTO
        AnalyzeDTO analyzeDTO = new AnalyzeDTO();

        analyzeDTO.getImage().setImage_no(generatedImageNo);
        analyzeDTO.getMember().setMember_no(member_no);
        analyzeDTO.setAnalyze_year(analyze_year);

        // 새 구조에 따라 GraphDTO에 graph_no를 설정
        GraphDTO graph = new GraphDTO();
        graph.setGraph_no(graph_no);
        analyzeDTO.setGraph(graph);

        analyzeDTO.getImage().setImage_name(image_name);
        analyzeDTO.getImage().setImage_description(image_description);

        // Save to the database
        analyzeRepository.addAnalyze(analyzeDTO);

        // Redirect to the analyze detail page with the new image_no
        return "redirect:/analyze/analyzes";
    }

    // 그래프 추가 폼
    @GetMapping("/graphs/add")
    public String addGraphForm() {
        return "analyze/graphAddForm";
    }

    @PostMapping("/graphs/add")
    public String addGraph(@RequestParam("graph_type") String graph_type) {
        // DB에 INSERT
        graphRepository.addGraph(graph_type);

        // 그래프 추가 후, 분석추가 페이지로 이동(혹은 다른 곳으로 이동 가능)
        return "redirect:/analyze/analyzes/add";
    }

}
