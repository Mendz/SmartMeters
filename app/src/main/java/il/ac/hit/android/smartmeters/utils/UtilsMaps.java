package il.ac.hit.android.smartmeters.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Static methods for uses google maps.
 */
public class UtilsMaps
{
    public static final LatLng LAT_LNG_CENTER = new LatLng(31.808124, 35.225466);

    public static void setZommButtons(GoogleMap googleMap)
    {
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

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
