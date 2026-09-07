package exo3_2;
import java.time.LocalDate;
import java.time.LocalTime;

public class NowDate {

    LocalDate localDate = LocalDate.now();
    LocalTime localTime = LocalTime.now();


    public String DateToString() {
        return localDate.toString();
    }
    public String TimeToString() { return localTime.toString(); }

    public LocalTime getLocalTime() {
        return localTime;
    }


}

