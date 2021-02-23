package pl.polsl.processor.service;


import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.polsl.processor.model.Node;
import pl.polsl.processor.model.WeatherReport;
import pl.polsl.processor.model.WeatherReportBase;
import pl.polsl.processor.repository.WeatherReportRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NodeService {
    private Node treeRoot;
    private Node lastNode;
    private Node lastSearched;
    private Integer treeDepth=0;
    private WeatherReportRepository weatherReportRepository;
//    public NodeService(){
//        treeRoot=new Node();
//        lastNode=treeRoot;
//        ArrayList<WeatherReport> arrayList=generateReports();
//        for(WeatherReport w: arrayList){
//            updateTree(w);
//        }
//    }
    @Autowired
    public NodeService(WeatherReportRepository weatherReportRepository){
        this.weatherReportRepository=weatherReportRepository;
        Long count = weatherReportRepository.count();
        System.out.println("There is a total of "+count+" records in database would you like to download all?(y/n)");
//        Scanner sc = new Scanner(System.in);
//        String input = sc.nextLine();
        List<WeatherReportBase> weatherReports1;
//        if(input=="y") {
            weatherReports1 = weatherReportRepository.findAll();
//        }else{
//            weatherReports1 = weatherReportRepository.findTop100ByOrderByReportIDAsc();
//        }
        List<WeatherReport> weatherReports = weatherReports1.stream().map(temp -> {
            WeatherReport obj = new WeatherReport(temp);
            return obj;
        }).collect(Collectors.toList());
        treeRoot=new Node();
        lastNode=treeRoot;
        if(!weatherReports.isEmpty()){
            for(WeatherReport w:weatherReports){
                updateTree(w);
            }
        }
    }
    private Long reportID= 0l;
    private String []cities={"Krakow",
            "Bielsko-Biala",
            "Warszawa",
            "Poznan",
            "Katowice",
            "Gliwice"};


    public ArrayList<WeatherReport> generateReports(){
        ArrayList<WeatherReport> weatherReports=new ArrayList<>();
        Date currentDate=new Date();
        Random random = new Random();
        for (int i=0;i<cities.length;i++){
            Double temperature = random.nextDouble();
            temperature*=100;
            reportID++;
            WeatherReport current=new WeatherReport(reportID,currentDate,cities[i],temperature);
            weatherReports.add(current);
        }
        return weatherReports;
    }
    public List<WeatherReport> getWeatherReports(Date date){
        List<WeatherReport> result;
        lastSearched= treeRoot.getWeatherReports(date);
        result=lastSearched.getWeatherReports();
        return result;

    }
    public Date getCurrentDate(){
        if(lastSearched.getKey()==null)
            return new Date();
        else return lastSearched.getKey();
    }
    public List<WeatherReport> getNext(){
        if(lastSearched.getHasNext()){
            lastSearched=lastSearched.getNext();
            return lastSearched.getWeatherReports();
        }else{
            return lastSearched.getWeatherReports();
        }
    }
    public List<WeatherReport> getPrevious(){
        if(lastSearched.getPrevious()!=null){
            lastSearched=lastSearched.getPrevious();
            return lastSearched.getWeatherReports();
        }else{
            return lastSearched.getWeatherReports();
        }
    }
    public Boolean updateTree(WeatherReport weatherReport){
        if(treeRoot.findAndAddIfNotFullNode(weatherReport,lastNode)){
            if(lastNode.getHasNext())
                lastNode=lastNode.getNext();
            return true;
        }else{
            treeDepth++;
            Node help=new Node();
            help.setKey(treeRoot.getKey());
            help.setValue(treeRoot.getValue());
            help.setViews(treeRoot.getViews());
            help.setDepth(treeDepth);
            ArrayList<Node> temp=new ArrayList<>();
            temp.add(treeRoot);
            temp.add(new Node(treeDepth-1));
            help.setSubNodes(temp);
            if(treeDepth==1){
                lastNode.setNext(temp.get(temp.size()-1));
                lastNode.setHasNext(true);
            }
            help.findAndAddIfNotFullNode(weatherReport,lastNode);
            if(lastNode.getHasNext())
                lastNode=lastNode.getNext();
            treeRoot=help;
            return true;
        }

    }
}

