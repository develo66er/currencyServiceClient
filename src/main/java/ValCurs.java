import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
@XStreamAlias("ValCurs")
public class ValCurs {
    @XStreamAlias("Date")
    @XStreamAsAttribute
    private String date;

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;

    @XStreamImplicit(itemFieldName = "Valute")
    private List<Valute> valute = new ArrayList<Valute>();

    public ValCurs() {
    }


    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Valute> getValute() {
        return this.valute;
    }

    public void setValute(List<Valute> valute) {
        this.valute = valute;
    }
}