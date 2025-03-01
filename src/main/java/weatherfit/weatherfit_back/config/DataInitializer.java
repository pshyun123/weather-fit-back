package weatherfit.weatherfit_back.config;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import weatherfit.weatherfit_back.entity.Coordinate;
import weatherfit.weatherfit_back.repository.CoordinateRepository;

import java.io.FileReader;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final CoordinateRepository coordinateRepository;

    @Override
    public void run(String... args) {
       //데이터 베이스 내용이 바뀌면, 내용 변경.
       initializeCoordinateData();
    }

    private void initializeCoordinateData() {
        String csvFile = "src/main/resources/data/Coordinate.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            reader.readNext(); // 헤더 건너뛰기 (변수 할당 없이)
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                Coordinate coordinate = Coordinate.builder()
                    .tpo(line[4])
                    .weatherCondition(line[5])
                    .coordinateImg(line[1])
                    .preference(line[2])
                    .targetAgeGroup(line[3])
                    .build();
                
                coordinateRepository.save(coordinate);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("CSV 파일 읽기 실패: " + e.getMessage());
        }
    }
}