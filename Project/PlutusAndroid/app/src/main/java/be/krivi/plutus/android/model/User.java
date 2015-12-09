package be.krivi.plutus.android.model;

/**
 * Created by Jan on 27/11/2015.
 */
public class User{

    private String studentId;
    private String password;
    private double balance;

    public User( double balance, String studentId ){
        setStudentId( studentId );
        setBalance( balance );
    }

    public double getBalance(){
        return balance;
    }

    private void setBalance( double balance ){
        this.balance = balance;
    }

    public String getStudentId(){
        return studentId;
    }

    private void setStudentId( String id ){
        this.studentId = id;
    }

    public String getPassword(){
        return password;
    }

    private void setPassword( String password ){
        this.password = password;
    }
}
