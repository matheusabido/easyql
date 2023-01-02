package dev.abidux.easyql.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Reader<E> {

    E read(ResultSet rs) throws SQLException;

}