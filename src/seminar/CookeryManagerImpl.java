/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seminar;

import common.IllegalEntityException;
import common.ServiceFailureException;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author mpx
 */
public class CookeryManagerImpl {

    private static final Logger logger = Logger.getLogger(
            CookeryManagerImpl.class.getName());

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }    

    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    List<Recipe> findRecipeWithIngredients(Ingredient ingredients) throws ServiceFailureException, IllegalEntityException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    List<Ingredient> getIngredientsInRecipe(Recipe recipe) throws ServiceFailureException, IllegalEntityException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void putIngredientsInRecipe(Recipe recipe, Ingredient ingredient) throws ServiceFailureException, IllegalEntityException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void removeIngredientsFromRecipe(Recipe recipe, Ingredient ingredient) throws ServiceFailureException, IllegalEntityException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
