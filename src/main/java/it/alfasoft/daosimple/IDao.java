package it.alfasoft.daosimple;

import java.sql.*;
import java.util.List;

public interface IDao<T,I> {
    Connection getConnection() throws DaoException;
    //CRUD OPERATIONS
    I create(T elemento) throws DaoException;
    List<T> read() throws DaoException;
    int update(I id, T elemento) throws DaoException;
    int replace(I id, T elemento) throws DaoException;
    int delete(I id) throws DaoException;
    T getById(I id) throws DaoException;
    List<T> find(String searchText) throws DaoException;
    List<T> find(T searchObj) throws DaoException;

    T convertToDto(ResultSet rs) throws DaoException;
    boolean checkOggetto(T elemento) throws DaoException;
    I getGeneratedKey(Statement stmt) throws DaoException;

    String getSelectByIdQuery();
    String getSelectAllQuery();
    String getInsertQuery();
    String getDeleteQuery();
    String getUpdateQuery();
    String getReplaceQuery();
    String getSearchByStringQuery();
    String getSearchByObjectQuery();
}
