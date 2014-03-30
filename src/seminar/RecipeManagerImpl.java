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
public class RecipeManagerImpl {
    
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
    
    void createRecipe(Recipe recipe) throws ServiceFailureException {
        
        checkDataSource();
        
        if (recipe == null) {
            throw new IllegalArgumentException("recipe is null");            
        }
        if (recipe.getId() != null) {
            throw new IllegalArgumentException("recipe id is already set");            
        }
        if (recipe.getName() == null) {
            throw new IllegalArgumentException("recipe name is null");            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement(
                    "INSERT INTO RECIPE (name,directions) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, recipe.getName());
            st.setString(2, recipe.getDirections());
            int addedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(addedRows, recipe, true);

            Long id = DBUtils.getId(st.getGeneratedKeys());
            recipe.setId(id);
            
            conn.commit();   
            
        } catch (SQLException ex) {
            String msg = "Error when inserting recipe into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    void deleteRecipe(Recipe recipe) throws ServiceFailureException {
        
        checkDataSource();
        
        if (recipe == null) {
            throw new IllegalArgumentException("recipe is null");            
        }
        if (recipe.getId() == null) {
            throw new IllegalArgumentException("recipe id is null");            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement(
                    "DELETE FROM RECIPE WHERE id = ?");
            st.setLong(1, recipe.getId());
            int deletedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(deletedRows, recipe, false);
            conn.commit();     
            
        } catch (SQLException ex) {
            String msg = "Error when deleting recipe from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);        
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    List<Recipe> findAllRecipes() throws ServiceFailureException {
        
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            
            st = conn.prepareStatement(
                    "SELECT id,name,directions FROM recipe");
            return executeQueryForMultipleRecipes(st);
            
        } catch (SQLException ex) {
            String msg = "Error when getting all recipes from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    Recipe findRecipeById(Long id) throws ServiceFailureException {
        
        checkDataSource();
        
        if (id == null) {
            throw new IllegalArgumentException("recipe id is null");            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            
            st = conn.prepareStatement(
                    "SELECT id,name,directions FROM recipe WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleRecipe(st);
            
        } catch (SQLException ex) {
            String msg = "Error when getting recipe with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    void updateRecipe(Recipe recipe) throws ServiceFailureException {
        
        checkDataSource();
        
        if (recipe == null) {
            throw new IllegalArgumentException("updated recipe is null");            
        }
        if (recipe.getId() == null) {
            throw new IllegalArgumentException("updated recipe id is null");            
        }
        if (recipe.getName() == null) {
            throw new IllegalArgumentException("updated recipe name is null");            
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement(
                    "UPDATE RECIPE SET name = ?,directions = ? WHERE id = ?");
            st.setString(1, recipe.getName());
            st.setString(2, recipe.getDirections());
            st.setLong(3, recipe.getId());
            
            int updatedRows = st.executeUpdate();
            DBUtils.checkUpdatesCount(updatedRows, recipe, false);
            conn.commit();
            
        } catch (SQLException ex) {
            String msg = "Error when updating recipe in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    static Recipe executeQueryForSingleRecipe(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Recipe recipe = rowToRecipe(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                        "Internal integrity error: more recipes with the same id found!");                   
                }            
                return recipe;
            } else {
                return null;
            }
    }
    
    static List<Recipe> executeQueryForMultipleRecipes(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
            
        List<Recipe> result = new ArrayList<Recipe>();
        while (rs.next()) {
            result.add(rowToRecipe(rs));
        }
        return result;
    }
    
    private static Recipe rowToRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getLong("id"));
        recipe.setName(rs.getString("name"));
        recipe.setDirections(rs.getString("directions"));
        return recipe;
    }
}
