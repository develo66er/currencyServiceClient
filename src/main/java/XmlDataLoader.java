import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class XmlDataLoader {
    private static final Long nMsPerDay = (long)86400000;
    private static final String get_xml = "1";
    private final static String endpointURL = "https://www.bnm.md/en/official_exchange_rates";
    private static final String rootPathXML = System.getProperty("user.dir")+File.separator +"XML"+File.separator;
    private static final String rootPathJSON = System.getProperty("user.dir")+File.separator +"JSON"+File.separator;
    public XmlDataLoader(){

    }
    public static Document getXMLDocument(GregorianCalendar calendar) throws ParserConfigurationException, IOException, SAXException {
        String path =rootPathXML + String.valueOf(calendar.get(Calendar.YEAR))+File.separator+String.valueOf(calendar.get(Calendar.MONTH)+1)+File.separator+"XMl_"+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+".xml";
        File file = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    public static ValCurs getValCursByDate(int day, int month, int year){
        String path = String.valueOf(year)+File.separator+String.valueOf(month)+File.separator+"XMl_"+String.valueOf(day) + ".xml";
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        return (ValCurs)xstream.fromXML(new File(rootPathXML+path));
    }
    public static void convertXmlToJSON(){
        ValCurs curs = null;
        File file,directory;
        BufferedWriter bw;
        String directoryPath;
        String filePath;
        Calendar current;
        CalendarGenerator generator = new CalendarGenerator(3);
        while((current = generator.generateData())!=null){
            directoryPath =rootPathJSON + String.valueOf(current.get(Calendar.YEAR))+File.separator+String.valueOf(current.get(Calendar.MONTH)+1)+File.separator;
            filePath = "JSON_"+String.valueOf(current.get(Calendar.DAY_OF_MONTH));
            curs = getValCursByDate(current.get(Calendar.DAY_OF_MONTH),current.get(Calendar.MONTH)+1,current.get(Calendar.YEAR));
            XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
            xstream.setMode(XStream.NO_REFERENCES);
            directory = new File(directoryPath);
            if(!directory.exists()){
                directory.mkdirs();
            }
            file = new File(directoryPath + File.separator + filePath + ".json");

            if (!file.exists()) try {
                file.createNewFile();
                bw = new BufferedWriter(new FileWriter(file));
                bw.write(xstream.toXML(curs));
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private static int loadXML(CalendarGenerator generator){
        int n = 0;
        Calendar current = null;
        File file,directory;
        BufferedWriter bw;
        String directoryPath;
        String filePath;
        try {
            FileUtils.deleteDirectory(new File(rootPathXML));
            FileUtils.deleteDirectory(new File(rootPathJSON));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        while((current = generator.generateData())!=null){
            n++;
            directoryPath =rootPathXML + String.valueOf(current.get(Calendar.YEAR))+File.separator+String.valueOf(current.get(Calendar.MONTH)+1)+File.separator;
            String dt = dateFormat.format(current.getTime());
            URL obj = null;
            String res="";
            try {
                obj = new URL(endpointURL+"?get_xml="+get_xml+"&date="+ dt);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                InputStream in = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(isr);
                filePath = "XMl_"+String.valueOf(current.get(Calendar.DAY_OF_MONTH));
                directory = new File(directoryPath);
                if(!directory.exists()){
                    directory.mkdirs();
                }

                file = new File(directoryPath + File.separator + filePath + ".xml");

                if (!file.exists()) file.createNewFile();
                bw = new BufferedWriter(new FileWriter(file));
                while((res = br.readLine())!=null) {
                    bw.write(res);
                }
                bw.close();
                isr.close();
                br.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return n;
    }
    public static int loadXMLFromService(int numOfMonthAgo){
        int n;
        CalendarGenerator generator = new CalendarGenerator(numOfMonthAgo);
        n = loadXML(generator);
        return n;
    }
    public static int loadXMLFromServiceByDateRange(Long start, Long end){
        int n ;
        CalendarGenerator generator = new CalendarGenerator(start,end);
        n = loadXML(generator);
        return n;
    }
    public static void main(String[] args) {
       try {
           FileUtils.deleteDirectory(new File(rootPathXML));
           FileUtils.deleteDirectory(new File(rootPathJSON));
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadXMLFromService(3);
        convertXmlToJSON();
    }
}
