package com.example.nearspot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Context ctx;
    GoogleMap mMap;
    public int MY_PERMISSIONS_REQUEST = 0264;
    ImageView mylocation;
//
    LinearLayout item_store_card_1, item_store_card_2, item_store_card_3;
    ViewPager vp;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;

//        startActivity(new Intent(this, MapsActivity.class));

        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        //COARSE_LOCATION

        // Here, thisActivity is the current activity
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 체크되어있지 않으면
//
//            ActivityCompat.requestPermissions(MainActivity.this, // 하위 호환성 측면에서 앱과 함께 사용할 특정 플랫폼 기능이 없어도 앱에 지원 라이브러리 클래스를 사용하기 위해 ActivityCompat사용
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_REQUEST);
//        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            init();
        }
    }

    public void init(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mylocation = (ImageView)findViewById(R.id.mylocation);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMyLocation();
            }
        });
//
//        item_store_card_1 = (LinearLayout)findViewById(R.id.item_store_card);
//        item_store_card_2 = (LinearLayout)findViewById(R.id.item_store_card);
//        item_store_card_3 = (LinearLayout)findViewById(R.id.item_store_card);

        vp = (ViewPager)findViewById(R.id.vp);

        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == ((LinearLayout) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater inflater = new LayoutInflater(ctx) {
                    @Override
                    public LayoutInflater cloneInContext(Context newContext) {
                        return null;
                    }
                };
                View itemView = inflater.inflate(R.layout.item_store_card, container, false);

                LinearLayout item_store_card = itemView.findViewById(R.id.item_store_card);
                item_store_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this, InformationActivity.class);
                        startActivity(intent); //
                    }
                });
//                ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//                imageView.setImageResource(mResources[position]);

                container.addView(itemView);

                return itemView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((LinearLayout) object);
            }
        });
        vp.setCurrentItem(0); //앱이 실행됬을 때 첫번째 페이지로 초기화 시키는 부분

        vp.setOnClickListener(movePageListener);
        vp.setTag(0);
        vp.setOnClickListener(movePageListener);
        vp.setTag(1);
        vp.setOnClickListener(movePageListener);
        vp.setTag(2);
  //
    }
 //
    public View.OnClickListener movePageListener = new View.OnClickListener(){
        public void onClick(View v)
        {

            int tag = (int)v.getTag();
//            vp.setCurrentItem(tag); // tag 값에 페이지에 해당하는 Position을 넣어주면 해당 페이지로 이동하는 부분
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        goMyLocation();
    }

    public void goMyLocation(){
        if(mMap.getMyLocation()!=null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()),18));
//        mMap.getMyLocation();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goMyLocation();
                }
            },500);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0264: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



}


