package il.ac.hit.android.smartmeters.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Static methods for uses google maps.
 */
public class UtilsMaps
{
    public static LatLng getCoordinatesByAddress(Context context,String address) throws IOException
    {
        //TODO: take care not hebrew.
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        double latitude;
        double longitude;


        addresses = geocoder.getFromLocationName(address, 1);

        if (addresses.size() > 0)
        {
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();

            return new LatLng(latitude, longitude);
        }

        return null;
    }
}
