package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;
import weatherfit.weatherfit_back.entity.Coordinate;
import weatherfit.weatherfit_back.service.CoordinateService;

@RestController
@Slf4j
@RequestMapping("/coordinate")
@RequiredArgsConstructor



public class CoordinateController {
    private final CoordinateService coordinateService;


    //착장 정보 조회
    @GetMapping("/list")
    public ResponseEntity<List<Coordinate>> getCoordinateList() {
        List<Coordinate> coordinateList = coordinateService.getCoordinateList();
        return ResponseEntity.ok(coordinateList);
    }


    
}
