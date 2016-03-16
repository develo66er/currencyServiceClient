import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class XmlDataLoader {
    private static final String get_xml = "1";
    private static String dt;
    private static Calendar current;
    private static SimpleDateFormat dateFormat;
    private final static String endpointURL = "https://www.bnm.md/en/official_exchange_rates";
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static BufferedWriter bw;
    private static File directory;
    private static File file;
    private static final String rootPathXML = System.getProperty("user.dir")+File.separator +"XML"+File.separator;
    private static final String rootPathJSON = System.getProperty("user.dir")+File.separator +"JSON"+File.separator;
    public XmlDataLoader(){

    }
    public static ValCurs getCursByDate(int day, int month, int year){
        String path = String.valueOf(year)+File.separator+String.valueOf(month)+File.separator+"XMl_"+String.valueOf(day) + ".xml";
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        ValCurs curs = (ValCurs)xstream.fromXML(new File(rootPathXML+path));
        return curs;
    }

    public static void convertXmlToJSON(){
        ValCurs curs = null;
        String directoryPath;
        String filePath;
        CalendarGenerator generator = new CalendarGenerator(3);
        while((current = generator.generateData())!=null){
            directoryPath =rootPathJSON + String.valueOf(current.get(Calendar.YEAR))+File.separator+String.valueOf(current.get(Calendar.MONTH)+1)+File.separator;
            filePath = "JSON_"+String.valueOf(current.get(Calendar.DAY_OF_MONTH));
            curs = getCursByDate(current.get(Calendar.DAY_OF_MONTH),current.get(Calendar.MONTH)+1,current.get(Calendar.YEAR));
            XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
            xstream.setMode(XStream.NO_REFERENCES);
            //xstream.alias("ValCurs", ValCurs.class);
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
    public static void loadXMLFromService(){

        String directoryPath;
        String filePath;
        current = null;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        CalendarGenerator generator = new CalendarGenerator(3);
        while((current = generator.generateData())!=null){
            directoryPath =rootPathXML + String.valueOf(current.get(Calendar.YEAR))+File.separator+String.valueOf(current.get(Calendar.MONTH)+1)+File.separator;
            dt = dateFormat.format(current.getTime());
            URL obj = null;
            String res="";
            try {
                obj = new URL(endpointURL+"?get_xml="+get_xml+"&date="+dt);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                InputStream in = con.getInputStream();
                isr =new InputStreamReader(in);
                br = new BufferedReader(isr);
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

    }
    public static void main(String[] args) {
       try {
           FileUtils.deleteDirectory(new File(rootPathXML));
           FileUtils.deleteDirectory(new File(rootPathJSON));
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadXMLFromService();
        convertXmlToJSON();
    }
}
