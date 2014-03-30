/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seminar;

import common.DBUtils;
import common.ServiceFailureException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author mpx
 */
public class IngredientManagerImpl {
    
    public static final Logger logger = Logger.getLogger(IngredientManagerImpl.class.getName());
    
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    void createIngredient(Ingredient ingredient) throws ServiceFailureException {
        
        checkDataSource();
        
        if (ingredient == null) {
            throw new IllegalArgumentException("ingredient is null");            
        }
        if (ingredient.getId() != null) {
            throw new IllegalArgumentException("ingredient id is already set");            
        }
        if (ingredient.getName() == null) {
            throw new IllegalArgumentException("ingredient name is null");            
        }

        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement(
                    "INSERT INTO INGREDIENT (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, ingredient.getName());
            int addedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(addedRows, ingredient, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            ingredient.setId(id);
            conn.commit();
            
        } catch (SQLException ex) {
            String msg = "Error when inserting ingredient into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    List<Ingredient> findAllIngredients() throws ServiceFailureException {
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,name FROM ingredient");
            return executeQueryForMultipleIngredients(st);
            
        } catch (SQLException ex) {
            String msg = "Error when getting all ingredients from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }  
    }

    Ingredient findIngredientById(Long id)  throws ServiceFailureException {
        
        checkDataSource();
        
        if (id == null) {
            throw new IllegalArgumentException("ingredient id is null");            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,name FROM ingredient WHERE id = ?");
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            
            return executeQueryForSingleIngredient(st);
            
        } catch (SQLException ex) {
            String msg = "Error when getting ingredient with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    static Ingredient executeQueryForSingleIngredient(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Ingredient result = rowToIngredient(rs);                
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more ingredients with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    static List<Ingredient> executeQueryForMultipleIngredients(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Ingredient> result = new ArrayList<Ingredient>();
        while (rs.next()) {
            result.add(rowToIngredient(rs));
        }
        return result;
    }

    static private Ingredient rowToIngredient(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getLong("id"));
        ingredient.setName(rs.getString("name"));
        return ingredient;
    }
}
