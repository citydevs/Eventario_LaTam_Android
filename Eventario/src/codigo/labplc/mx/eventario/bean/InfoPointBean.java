package codigo.labplc.mx.eventario.bean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Bean para el mapa y traer la rutra
 * @author zaced
 *
 */
public class InfoPointBean {
	ArrayList<HashMap<String, Object>> addressFields=new ArrayList<HashMap<String, Object>>();
    String strFormattedAddress="";
    double dblLatitude=0;
    double dblLongitude=0;
    public ArrayList<HashMap<String, Object>> getAddressFields() {
        return addressFields;
    }
    public void setAddressFields(ArrayList<HashMap<String, Object>> addressFields) {
        this.addressFields = addressFields;
    }
    public String getStrFormattedAddress() {
        return strFormattedAddress;
    }
    public void setStrFormattedAddress(String strFormattedAddress) {
        this.strFormattedAddress = strFormattedAddress;
    }
    public double getDblLatitude() {
        return dblLatitude;
    }
    public void setDblLatitude(double dblLatitude) {
        this.dblLatitude = dblLatitude;
    }
    public double getDblLongitude() {
        return dblLongitude;
    }
    public void setDblLongitude(double dblLongitude) {
        this.dblLongitude = dblLongitude;
    }
}