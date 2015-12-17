package be.krivi.plutus.android.model;

/**
 * Created by Jan on 27/11/2015.
 */
public class User{

    private String studentId;
    private String password;
    private double balance;

    public User(){}

    public User( String studentId, String password ){
        setStudentId( studentId );
        setPassword( password );
    }

    public User( String studentId, String password, double balance ){
        this(studentId, password );
        setBalance( balance );
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance( double balance ){
        this.balance = balance;
    }

    public String getStudentId(){
        return studentId;
    }

    public void setStudentId( String id ){
        this.studentId = id;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword( String password ){
        this.password = password;
    }
}
