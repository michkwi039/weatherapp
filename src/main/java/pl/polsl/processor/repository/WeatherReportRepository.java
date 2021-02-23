package pl.polsl.processor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.processor.model.WeatherReport;
import pl.polsl.processor.model.WeatherReportBase;


import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WeatherReportRepository extends JpaRepository<WeatherReportBase,Long> {

    List<WeatherReportBase> findAllByTimeAfter(Timestamp after);
    List<WeatherReportBase> findTop100ByOrderByReportIDAsc();
}
