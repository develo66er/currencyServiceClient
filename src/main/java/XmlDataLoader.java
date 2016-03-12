import com.thoughtworks.xstream.XStream;
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
    private static final int year = 1999;
    private static final String rootPath = System.getProperty("user.dir")+File.separator +"XML"+File.separator;
    public XmlDataLoader(){

    }
    public static void getCursByDate(int day, int month, int year){
        String path = String.valueOf(year)+File.separator+String.valueOf(month)+File.separator+"XMl_"+String.valueOf(day) + ".xml";
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        ValCurs curs = (ValCurs)xstream.fromXML(new File(rootPath+path));
        System.out.println("Data : "+curs.getDate());
        for(Valute valute : curs.getValute()){
            System.out.println("-------------------------------");
            System.out.println();
            System.out.println("valute ID : "+valute.getId());
            System.out.println("valute num code : "+valute.getNumCode());
            System.out.println("valute char code : "+valute.getCharCode());
            System.out.println("valute nominal : "+valute.getNominal());
            System.out.println("valute name : "+valute.getName());
            System.out.println("valute value : "+valute.getValue());
            System.out.println();
            System.out.println("-------------------------------");
        }
    }
    public static void loadXMLFromService(){

        String directoryPath;
        String filePath;
        current = null;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        CalendarGenerator generator = new CalendarGenerator(year);
        while((current = generator.generateData())!=null){
            directoryPath =rootPath + String.valueOf(current.get(Calendar.YEAR))+File.separator+String.valueOf(current.get(Calendar.MONTH)+1)+File.separator;
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
            FileUtils.deleteDirectory(new File(rootPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadXMLFromService();
        getCursByDate(20, 2, 2012);
    }
}
