package pl.polsl.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherReportContainer {
    private ArrayList<WeatherReport> weatherReports;
}
