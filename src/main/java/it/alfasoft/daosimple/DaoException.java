package it.alfasoft.daosimple;

public class DaoException extends RuntimeException{

    public DaoException(){ super();}
    public DaoException(String s){ super(s);}
    public DaoException(Throwable cause) { super(cause);}
}
