package com.miriandor.hackathonapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Date;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapModule extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Geocoder geocoder;

    Events[] addedEvents = { //TODO: not hardcoded options
            new Events("Pnina Pie", "Sweets", "ניסים בכר, נחלאות", new LatLng(31.777883, 35.198348), new Date(),
                    "Buy Your Dessert for the Weekend! cakes and pies for 50NIS",
                    "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2011/10/5/0/FNM_110111-Insert-019_s4x3.jpg.rend.hgtvcom.581.436.suffix/1371600342161.jpeg"),
            new Events("התותים של משק חביביאן", "Farm", "Derech Balfour 1, Jerusalem", new LatLng(31.779388, 35.197939), new Date(),
                    "תותים טריים מהמשק ישר אליכם, חצי קילו ב40",
                    "https://res.cloudinary.com/jnto/image/upload/w_960,h_720,c_fill,f_auto,fl_lossy,q_auto/v1/media/filer_public/9d/b4/9db4c90e-be05-48e1-a4d9-eeb9a45af6cb/109_3323388_img_20190207_122751-01_pkecsd"),
            new Events("הפסקה פעילה", "Drinks", "הדשא הגדול בגבעת רם", new LatLng(31.777609, 35.200368), new Date(),
                    "הפסקה פעילה בגבעת רם! בירה ב10",
                    "https://st4.depositphotos.com/13194036/20452/i/1600/depositphotos_204525116-stock-photo-happy-multiracial-friends-beer-resting.jpg"),
            new Events("Hummus", "Homemade", "עזה 32, רחביה", new LatLng(31.775747, 35.196074), new Date(),
                    "Homemade Hummus",
                    "https://www.thespruceeats.com/thmb/mhFtcMDsXNobBwrKFFS74HKZSVY=/2048x1152/smart/filters:no_upscale()/hummus-with-tahini-2355351-15_preview-5af9c9a5c5542e00361bdc0d.jpeg"),
            new Events("Knaffeh", "Sweets", "Old City, Jerusalem", new LatLng(31.774630, 35.197691), new Date(),
                    "Sweet authentic Knaffeh",
                    "https://www.top40recipes.com/images/knafeh1.jpg")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_module);

        //Todo: ADD PLACES AUTOCOMPLETE

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        geocoder = new Geocoder(this, Locale.getDefault());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapModule.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(31.777028899999998, 35.1980509));
        LatLng latLng = new LatLng(31.777028899999998, 35.1980509);
        Marker home = map.addMarker(new MarkerOptions().position(latLng).title("Home"));
        home.setTag(-1);
        populateLocations();
        // moves camera to coordinates
        map.moveCamera(point);
        // animates camera to coordinates
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if ((int) (marker.getTag()) == -1) {
                    return false;
                } else {
                    final int position = (int) (marker.getTag());
                    Log.d("TAG", addedEvents[position].name);
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_window, null);


                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window tolken
                    popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
                    TextView title = popupView.findViewById(R.id.title);
                    TextView location = popupView.findViewById(R.id.location);
                    Button button = popupView.findViewById(R.id.button);
                    ImageView imageView = popupView.findViewById(R.id.imageView);
                    Picasso.get().load(addedEvents[position].imageURL).centerCrop()
                            .resize(400, 200)
                            .into(imageView);
                    title.setText(addedEvents[position].name);
                    location.setText(addedEvents[position].address);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToSaleIntent(position);
                        }
                    });
                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                }
                return false;
            }
        });
    }

    public void populateLocations() {
        float iconColor = BitmapDescriptorFactory.HUE_RED;
        for (int i = 0; i < addedEvents.length; i++) {
            Events l = addedEvents[i];
            if (l.category.equals("Sweets")) { //TODO: refactor this shit
                iconColor = BitmapDescriptorFactory.HUE_ROSE;
            } else if (l.category.equals("Farm")) {
                iconColor = BitmapDescriptorFactory.HUE_CYAN;
            } else if (l.category.equals("Drinks")) {
                iconColor = BitmapDescriptorFactory.HUE_GREEN;
            } else if (l.category.equals("Homemade")) {
                iconColor = BitmapDescriptorFactory.HUE_AZURE;
            }
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(l.latLng)
                    .title(l.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor)));
            marker.setTag(i);
        }

    }

    public void goToSaleIntent(int position) {
        Events event = addedEvents[position];
        Intent intent = new Intent(this, salePage.class);
        intent.putExtra("event item", event);
        startActivity(intent);
    }
}