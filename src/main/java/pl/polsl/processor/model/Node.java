package pl.polsl.processor.model;

import lombok.Data;
import pl.polsl.processor.utility.DateUtil;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Data
public class Node {
    private int views=0;
    private int depth=0;
    private List<WeatherReport> weatherReports=new ArrayList<>();

    public void setKey(Date key) {
        this.key = DateUtil.removeTime(key);
    }

    private Date key;
    private Double value=0.0d;
    private List<Node> subNodes;
    private Boolean hasNext=false;
    private Node next;
    private Node previous;

public Node getWeatherReports(Date date){

    if(subNodes==null){
        views++;
        return this;}
    else{
        Node help=null;
        for(Node n:subNodes){
            if(help==null&&n.getKey().after(date)){
                views++;
                return n.getWeatherReports(date);
            }
            if(n.getKey().after(date)) {
                views++;
                return help.getWeatherReports(date);
            }else if(n.getKey().equals(date)){
                views++;
                return n.getWeatherReports(date);
            }
            help=n;
        }
        return help.getWeatherReports(date);
    }
}
    public Node getLatestNode(){
        if(subNodes!=null){
            return subNodes.get(subNodes.size()-1).getLatestNode();
        }else{
            return this;
        }
    }
    public Node(List<WeatherReport> weatherReports){
        this.weatherReports=weatherReports;
        key=DateUtil.removeTime(weatherReports.get(0).getDate());
        int size=weatherReports.size();
        if(size>4) {
            subNodes=new ArrayList<Node>();
            for (int i = 0; i < (size/4<4?size/4:4); i++) {

                subNodes.add(new Node(weatherReports.subList(4*i,(4*i+3)<size? 4*i+3:size)));
            }
        }
    }
    public Node(){
    }
    public Node(Integer depth){
        this.depth=depth;
        if(depth>0){
            subNodes=new ArrayList<>();
            subNodes.add(new Node(depth-1));
        }
    }

    public void addWeatherReport(WeatherReport weatherReport,Date value){
        if((!value.before(this.key))&&subNodes==null){
            weatherReports.add(weatherReport);
        }else{
            for(Node n:subNodes){
                if(!n.key.before(value)) {
                    n.addWeatherReport(weatherReport,value);
                    break;
                }
            }
        }
    }

    public Boolean findAndAddIfNotFullNode(WeatherReport weatherReport,Node latest){
        if(weatherReports.size()<4&&subNodes==null){
            weatherReports.add(weatherReport);
            Double sum=0.0;
            for(WeatherReport w: weatherReports){
                sum+=w.getTemperature();
            }
            value=sum/weatherReports.size();
            key=DateUtil.removeTime(weatherReports.get(0).getDate());
            if(latest!=this){
                latest.setNext(this);
                latest.setHasNext(true);
                this.setPrevious(latest);
            }
            return true;
        }else if(weatherReports.size()==4&&subNodes==null){
            return false;
        }else if(subNodes.size()==4){
            if(subNodes.get(subNodes.size()-1).findAndAddIfNotFullNode(weatherReport,latest)){
                Double sum=0.0;
                for(Node n: subNodes){
                    sum+=n.getValue();
                }
                value=sum/subNodes.size();
                return true;
            }return false;
        }else{
            if(!subNodes.get(subNodes.size()-1).findAndAddIfNotFullNode(weatherReport,latest)) {
                Node newest=new Node( depth - 1);
                newest.findAndAddIfNotFullNode(weatherReport,latest);
                subNodes.add(newest);
                if(depth==1){
                    latest.setNext(newest);
                    latest.setHasNext(true);
                }
            }
            Double sum=0.0;
            for(Node n: subNodes){
                sum+=n.getValue();
            }
            value=sum/subNodes.size();
            key=DateUtil.removeTime(subNodes.get(0).getKey());
            return true;
        }
    }
}


