package be.krivi.plutus.android.activity;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.view.Message;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class DetailActivity extends BaseActivity implements OnMapReadyCallback{

    private GoogleMap map;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );
        setUpMap();

        Message.toast( this, app.getTransactionDetail().getTimestamp().toString() );

        //TODO alles
    }

    private void setUpMap(){
        if(map==null){

            FragmentManager manager = getFragmentManager();

            MapFragment mapFragment = (MapFragment)manager.findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady( GoogleMap map ){
        this.map = map;
       // map.setMyLocationEnabled(true);
    }
}
