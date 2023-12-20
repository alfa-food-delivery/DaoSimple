package it.alfasoft.daosimple;

import it.alfasoft.propertiesmanager.PropertiesManager;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoImpl<T,I> implements IDao<T,I>, Serializable {

    String tableName;

    public DaoImpl(){}

    public String getTableName(){ return this.tableName;}
    public void setTableName(String tableName){this.tableName = tableName;}
    public Connection getConnection() throws DaoException {
        Connection connection = null;
        String dburl = null;
        String dbuser = null;
        String dbpwd = null;
        try {
            dburl = PropertiesManager.getProperties().getProperty("db.url");
            dbuser =PropertiesManager.getProperties().getProperty("db.user");
            dbpwd = PropertiesManager.getProperties().getProperty("db.password");
        }
        catch (FileNotFoundException e) { e.printStackTrace();throw new DaoException(e);}
        //Il driver manager consente di aprire una connessione con il DB
        try { connection = DriverManager.getConnection(dburl,dbuser,dbpwd);}
        catch (SQLException e) { e.printStackTrace(); throw new DaoException(e);}
        return connection;
    }

    public T getById(I id) throws DaoException {
        try( Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(getSelectByIdQuery(id)) )
        {
            rs.next();
            return convertToDto(rs);
        }
        catch(Exception e){ e.printStackTrace(); throw new DaoException(e);}
    }


    public List<T> getList(ResultSet rs) throws DaoException{
        try
        {
            List<T> oggetti = new ArrayList<>();
            while(rs.next()){ oggetti.add( convertToDto(rs)); }
            return oggetti;
        }
        catch(SQLException e){ e.printStackTrace(); throw new DaoException(e);}
    }
    //CREATE
    public I create(T elemento) throws DaoException{
        if(elemento == null) throw new DaoException();
        try( Statement stmt = getConnection().createStatement() )
        {
            int qty = stmt.executeUpdate( getInsertQuery(elemento),Statement.RETURN_GENERATED_KEYS );
            return getGeneratedKey(stmt);
        }
        catch (Exception e) { e.printStackTrace(); throw new DaoException(); }
    }
    //READ
    public List<T> read() throws DaoException {
        try ( Statement stmt = getConnection().createStatement();
              ResultSet rs = stmt.executeQuery(getSelectAllQuery()) )
        {
            List<T> oggetti = new ArrayList<>();
            while(rs.next()){ oggetti.add( convertToDto(rs)); }
            return oggetti;
        }catch (Exception e) { e.printStackTrace(); throw new DaoException(e); }
    }
    //UPDATE (PUT) - Campi NULL salvati come tali
    public int update(I id, T elemento) throws DaoException {
        if(checkOggetto(elemento)){
            try( Statement stmt = getConnection().createStatement() )
            { return stmt.executeUpdate( getUpdateQuery(id,elemento) ); }
            catch (Exception e) { e.printStackTrace(); throw new DaoException(e); }
        }
        return 0;
    }

    //REPLACE (PATCH) - Campi NULL ignorati
    public int replace(I id, T elemento) throws DaoException {
        if(checkOggetto(elemento)){
            try( Statement stmt = getConnection().createStatement() )
            { return stmt.executeUpdate( getReplaceQuery(id,elemento) ); }
            catch (Exception e) { e.printStackTrace(); throw new DaoException(e); }
        }
        return 0;
    }
    //DELETE
    public int delete(I id) throws DaoException {
        try( Statement stmt = getConnection().createStatement() )
        { return stmt.executeUpdate( getDeleteQuery(id) ); }
        catch (Exception e) { e.printStackTrace(); throw new DaoException(e); }
    }
    //FIND BY STRING
    public List<T> find(String searchText) throws DaoException {
        try( Statement stmt = getConnection().createStatement() )
        {
            List<T> oggetti = new ArrayList<>();
            ResultSet rs = stmt.executeQuery( getSearchByStringQuery(searchText) );
            while(rs.next()){ oggetti.add( convertToDto(rs)); }
            return oggetti;
        }catch(Exception e){ e.printStackTrace(); throw new DaoException(e);}
    }
    //FIND BY OBJECT
    public List<T> find(T searchObj) throws DaoException {
        try( Statement stmt = getConnection().createStatement() )
        {
            List<T> oggetti = new ArrayList<>();
            ResultSet rs = stmt.executeQuery( getSearchByObjectQuery(searchObj) );
            while(rs.next()){ oggetti.add( convertToDto(rs)); }
            return oggetti;
        }catch(Exception e){ e.printStackTrace();throw new DaoException(e);}
    }

    // FUNZIONI SQL INEJCTORS
    public abstract String getSelectByIdQuery(I id);
    public abstract String getSelectAllQuery();
    public abstract String getInsertQuery(T elemento);
    public abstract String getDeleteQuery(I id);
    public abstract String getUpdateQuery(I id, T elemento);
    public abstract String getReplaceQuery(I id, T elemento);
    public abstract String getSearchByStringQuery(String searchText);
    public abstract String getSearchByObjectQuery(T elemento);

    public abstract T convertToDto(ResultSet rs) throws DaoException;
    public abstract boolean checkOggetto(T elemento) throws DaoException;
    public abstract I getGeneratedKey(Statement stmt) throws DaoException;
}
