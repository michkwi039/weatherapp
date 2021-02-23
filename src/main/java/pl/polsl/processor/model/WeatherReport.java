package pl.polsl.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherReport {

    private Long reportID;
    private Date date;
    private String city;
    private Double temperature;

    public WeatherReport(WeatherReportBase weatherReportBase){
        city=weatherReportBase.getCity();
        date=weatherReportBase.getTime();
        temperature=weatherReportBase.getTemperature();
        reportID=weatherReportBase.getReportID();
    }
}

