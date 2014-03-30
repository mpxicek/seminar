/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seminar;

import common.ServiceFailureException;
import java.util.List;

/**
 *
 * @author mpx
 */
public interface IngredientManager {

    /**
     * Stores new ingredient into database. Id for the new ingredient is automatically
     * generated and stored into id attribute.
     *
     * @param ingredient ingredient to be created.
     * @throws IllegalArgumentException when ingredient is null, or ingredient has already
     * assigned id.
     * @throws  ServiceFailureException when db operation fails.
     */
    void createIngredient(Ingredient ingredient) throws ServiceFailureException;

    /**
     * Returns list of all ingredients in the database.
     *
     * @return list of all ingredients in database.
     * @throws  ServiceFailureException when db operation fails.
     */
    List<Ingredient> findAllIngredients() throws ServiceFailureException;

    /**
     * Finds and Returns ingredient with given id.
     *
     * @param id primary key of requested ingredient.
     * @return ingredient with given id or null if such ingredient does not exist.
     * @throws IllegalArgumentException when given id is null.
     * @throws  ServiceFailureException when db operation fails.
     */
    Ingredient findIngredientById(Long id)  throws ServiceFailureException;
}
