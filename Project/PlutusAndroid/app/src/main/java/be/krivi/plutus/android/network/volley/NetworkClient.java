package be.krivi.plutus.android.network.volley;

import be.krivi.plutus.android.model.User;

/**
 * Created by Jan on 17/12/2015.
 */
public class NetworkClient{

    private Client volleyClient;

    public NetworkClient(){
        this.volleyClient = new Client();
    }

    public void populateDatabase(User user, int page) {
        volleyClient.populateDatabase( user, page);
    }

}
