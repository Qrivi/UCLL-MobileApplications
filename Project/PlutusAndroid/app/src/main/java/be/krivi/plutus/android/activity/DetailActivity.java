package be.krivi.plutus.android.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends BaseActivity implements OnMapReadyCallback{

    private GoogleMap map;
    private Marker marker;
    private Transaction transaction;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );

        setUpView();
        setUpMap();
    }

    private void setUpView(){
        if( app.getTransactionDetail() != null ){

            this.transaction = app.getTransactionDetail();

        }else{
            Message.obtrusive( this, getString( R.string.error_loading_transaction ) );
        }
    }

    private void setUpMap(){
        if( map == null && transaction != null ){
            FragmentManager manager = getFragmentManager();

            MapFragment mapFragment = (MapFragment)manager.findFragmentById( R.id.map );
            mapFragment.getMapAsync( this );

        }else{
            Message.obtrusive( this, getString( R.string.error_loading_map ) );
        }
    }

    @Override
    public void onMapReady( GoogleMap map ){
        double lat = transaction.getLocation().getLat();
        double lng = transaction.getLocation().getLng();
        String title = transaction.getLocation().getName();
        this.map = map;

        this.map.addMarker( new MarkerOptions()
                .position( new LatLng( lat, lng ) )
                .title( title )
                .snippet( "Population: 4,137,400" )
                .flat(true));
    }
}
