package be.krivi.plutus.android.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends BaseActivity implements OnMapReadyCallback{

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    private GoogleMap map;
    private Transaction transaction;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );
        ButterKnife.bind( this );

        setSupportActionBar( mToolbar );
        if( getSupportActionBar() != null )
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
    }

    @Override
    protected void onResume(){
        super.onResume();
        setUpView();
        setUpMap();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        switch( item.getItemId() ){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private void setUpView(){
        this.transaction = app.getTransactionDetail();
    }

    private void setUpMap(){
        if( map == null ){
            MapFragment mapFragment = (MapFragment)getFragmentManager()
                    .findFragmentById( R.id.map );
            mapFragment.getMapAsync( this );
        }
    }

    @Override
    public void onMapReady( GoogleMap map ){
        this.map = map;

        if( transaction == null ){
            Message.obtrusive( this, getString( R.string.error_loading_transaction ) );
        }else{
            LatLng latLng = new LatLng( transaction.getLocation().getLat(), transaction.getLocation().getLng() );
            map.addMarker( new MarkerOptions()
                    .position( latLng )
                    .title( "swagschool" )
                    .icon( BitmapDescriptorFactory.fromResource( R.drawable.ic_location_on_pink_48dp ) ) );

            //map.getUiSettings().setMapToolbarEnabled( false );
            map.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng, 15 ) );
            map.animateCamera( CameraUpdateFactory.zoomIn() );
            map.animateCamera( CameraUpdateFactory.zoomTo( 15 ), 2000, null );
        }


    }
}
