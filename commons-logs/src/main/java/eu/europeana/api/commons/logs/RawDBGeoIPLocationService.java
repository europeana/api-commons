package eu.europeana.api.commons.logs;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Reads the GeoLite2 database to get the geo location based on IP
 * Created by Srishti on 14 September 2020
 */
public class RawDBGeoIPLocationService {
    private DatabaseReader dbReader;
    protected static final String fileName = "GeoLite2-City.mmdb";

    public RawDBGeoIPLocationService() throws IOException {
        File database = new File(getClass().getClassLoader().getResource(fileName).getFile());
        dbReader = new DatabaseReader.Builder(database).build();
    }

    public  GeoIP getLocation(String ip) throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = dbReader.city(ipAddress);
        String cityName = response.getCity().getName();
        String latitude = response.getLocation().getLatitude().toString();
        String longitude = response.getLocation().getLongitude().toString();
        return new GeoIP(ip, cityName, latitude, longitude);
    }
}
