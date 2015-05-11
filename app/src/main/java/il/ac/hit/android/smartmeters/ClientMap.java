package il.ac.hit.android.smartmeters;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import il.ac.hit.android.smartmeters.utils.UtilsMaps;

import java.io.IOException;

public class ClientMap extends FragmentActivity
{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final LatLng LAT_LNG_CENTER = new LatLng(31.808124, 35.225466);
    private static final String DUMMY_TABLE_USER_WORKING_METERS = "rishon lezion:123456789:10000";

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_map);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
            {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap()
    {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(LAT_LNG_CENTER) // Sets the center of the map to Israel
                .zoom(7)
                .build(); // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //        for (int i = 0; i < 3; i++)
        //        {
        String[] pieces = DUMMY_TABLE_USER_WORKING_METERS.split(":");

        String address = pieces[0];
        String meterID = pieces[1];
        String kwhCumulative = pieces[2];

        //todo: do it in another thread
        markMetersMap(address, meterID, kwhCumulative);
        //}
    }

    //todo:  customize the contents and design of info windows for the snippet
    //todo: http://stackoverflow.com/questions/15783227/aligning-the-text-in-google-maps-marker-snippet
    private void markMetersMap(String address, String meterId, String kwh)
    {
        try
        {
            LatLng latLngAddress = UtilsMaps.getCoordinatesByAddress(this, address);

            String snippet = getString(R.string.marker_meter_address) + address
                    + " ||| "
                    + getString(R.string.marker_meter_kwhCumulative) + kwh;

            //todo: take care if null
            if (latLngAddress != null)
            {
                mMap.addMarker(new MarkerOptions().title(getString(R.string.marker_title) + meterId)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(latLngAddress)
                );
            }
            else
            {
                Log.e("client_map", "Can't find coordinates by this address: " + address);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
