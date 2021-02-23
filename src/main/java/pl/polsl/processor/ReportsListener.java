package pl.polsl.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.polsl.processor.model.WeatherReport;
import pl.polsl.processor.model.WeatherReportBase;
import pl.polsl.processor.model.WeatherReportContainer;
import pl.polsl.processor.repository.WeatherReportRepository;
import pl.polsl.processor.service.NodeService;

@Service
public class ReportsListener {
    private final String topicName="Reports";
    @Autowired
    private NodeService nodeService;
    @Autowired
    private WeatherReportRepository weatherReportRepository;

    @KafkaListener(topics = topicName, containerFactory = "reportsKafkaListenerContainerFactory")
    public void listenReports(WeatherReportContainer weatherReportContainer) {
        if(weatherReportContainer!=null) {
            System.out.println("Recieved reports");
            for (WeatherReport w : weatherReportContainer.getWeatherReports()) {
                nodeService.updateTree(w);
                weatherReportRepository.save(new WeatherReportBase(w));
            }

        }

    }
}
