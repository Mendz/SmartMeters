package il.ac.hit.android.smartmeters.client;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.database.DatabaseOperations;
import il.ac.hit.android.smartmeters.database.Meter;
import il.ac.hit.android.smartmeters.database.Tables;
import il.ac.hit.android.smartmeters.utils.UtilsMaps;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientMap extends FragmentActivity
{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private String _id;

    private Map<Marker, Meter> markerMeterMap = new HashMap<>();

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_map);

        _id = getIntent().getStringExtra(Tables.ClientTable.UserId);

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

        CameraPosition cameraPosition = new CameraPosition.Builder().target(UtilsMaps.LAT_LNG_CENTER) // Sets the center of the map to Israel
                .zoom(7).build(); // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        UtilsMaps.setZommButtons(mMap);


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(Marker marker)
            {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker)
            {
                View view = getLayoutInflater().inflate(R.layout.marker, null);

                TextView textViewMeterId = (TextView) view.findViewById(R.id.textViewMarkerMeterId);
                TextView textViewAddress = (TextView) view.findViewById(R.id.textViewMarkerAddress);
                TextView textViewKwh = (TextView) view.findViewById(R.id.textViewMarkerKwh);

                Meter meter = markerMeterMap.get(marker);

                textViewMeterId.setText(getString(R.string.marker_meter_id) + "  " + meter.getMeterId());
                textViewAddress.setText(meter.getAddress());
                textViewKwh.setText(meter.getkWh());

                return view;
            }
        });




        DatabaseOperations databaseOperations = new DatabaseOperations(this);

        List<Meter> metersById = databaseOperations.getAllMetersById(_id);

        Log.d("client_map", "The meters: : " + Arrays.toString(metersById.toArray()));
        String address, meterID, kwhCumulative;

        for (Meter meter : metersById)
        {
            Log.d("client_map", "Get the meter: " + meter.toString());
            address = meter.getAddress();
            meterID = meter.getMeterId();
            kwhCumulative = meter.getkWh();

            //todo: do it in another thread
            markMetersMap(address, meterID, kwhCumulative);
        }
    }

    //todo:  customize the contents and design of info windows for the snippet
    //todo: http://stackoverflow.com/questions/15783227/aligning-the-text-in-google-maps-marker-snippet
    private void markMetersMap(String address, final String meterId, String kwh)
    {
        Log.d("client_map", "within markMetersMap");
        try
        {
            LatLng latLngAddress = UtilsMaps.getCoordinatesByAddress(this, address);

            String snippet = getString(R.string.marker_meter_address) + address + " ||| " + getString(R.string.marker_meter_kwhCumulative) + kwh;

            //todo: take care if null
            if (latLngAddress != null)
            {

                Marker marker = mMap.addMarker(new MarkerOptions().title(getString(R.string.marker_title) + meterId)
                        //                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .position(latLngAddress));

                Meter meter = new Meter();
                meter.setMeterId(meterId);
                meter.setAddress(address);
                meter.setkWh(kwh);

                markerMeterMap.put(marker, meter);
                //marker.showInfoWindow();

                //                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                //                {
                //                    @Override
                //                    public boolean onMarkerClick(Marker marker)
                //                    {
                //                        final Dialog dialog = new Dialog(ClientMap.this);
                //                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //                        dialog.setContentView(R.layout.marker);
                //
                //                        TextView textViewMeterId = (TextView) dialog.findViewById(R.id.textViewMarkerMeterId);
                //                        TextView textViewAddress = (TextView) dialog.findViewById(R.id.textViewMarkerAddress);
                //                        TextView textViewKwh = (TextView) dialog.findViewById(R.id.textViewMarkerKwh);
                //
                //                        Meter meter = markerMeterMap.get(marker);
                //
                //                        textViewMeterId.setText(getString(R.string.marker_meter_id) +"  " +meter.getMeterId());
                //                        textViewAddress.setText(meter.getAddress());
                //                        textViewKwh.setText(meter.getkWh());
                //
                //                        dialog.show();
                //                        return true;
                //                    }
                //                });
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
