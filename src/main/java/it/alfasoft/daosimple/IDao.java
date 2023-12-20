package it.alfasoft.daosimple;

import java.sql.*;
import java.util.List;

public interface IDao<T,I> {
    //CRUD OPERATIONS
    I create(T elemento) throws DaoException;
    List<T> read() throws DaoException;
    int update(I id, T elemento) throws DaoException;
    int replace(I id, T elemento) throws DaoException;
    int delete(I id) throws DaoException;
    T getById(I id) throws DaoException;
    List<T> find(String searchText) throws DaoException;
    List<T> find(T searchObj) throws DaoException;
}
