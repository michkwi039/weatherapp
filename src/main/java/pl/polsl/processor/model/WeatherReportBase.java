package pl.polsl.processor.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
public class WeatherReportBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportID;
    @NotNull
    @Column(name="create_time", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp time;
    @NotNull
    private Double temperature;
    @NotNull
    private String city;
    public WeatherReportBase(WeatherReport weatherReport){
        time=new Timestamp(weatherReport.getDate().getTime());
        temperature=weatherReport.getTemperature();
        city=weatherReport.getCity();
    }
}