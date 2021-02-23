package pl.polsl.processor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.polsl.processor.ReportsListener;
import pl.polsl.processor.model.WeatherReport;
import pl.polsl.processor.model.WeatherReportContainer;
import pl.polsl.processor.service.NodeService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ReportsController {
    private ReportsListener reportsListener;
    private NodeService nodeService;

    @Autowired
    public ReportsController(ReportsListener reportsListener,NodeService nodeService){
        this.reportsListener=reportsListener;
        this.nodeService=nodeService;
    }
    @RequestMapping(value = "/reports/{date}",method = RequestMethod.GET)
    public ResponseEntity<List<WeatherReport>> getReportsByDate (@PathVariable("date") String dateString)
    {

        try {
            Date date;
            DateFormat formatter;
            formatter = new SimpleDateFormat("MM-dd-yyyy");
            date = formatter.parse(dateString);
            List<WeatherReport> result = new ArrayList<>(nodeService.getWeatherReports(date));
            if (result == null)
                return new ResponseEntity(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (ParseException e) {
            System.out.println("Exception :" + e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @RequestMapping(value = "/next",method = RequestMethod.GET)
    public ResponseEntity<WeatherReportContainer> getNextReports(){
        WeatherReportContainer result= new WeatherReportContainer(new ArrayList<>(nodeService.getNext()));
        if (result == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @RequestMapping(value = "/previous",method = RequestMethod.GET)
    public ResponseEntity<WeatherReportContainer> getPreviousReports(){
        WeatherReportContainer result= new WeatherReportContainer(new ArrayList<>(nodeService.getPrevious()));
        if (result == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
