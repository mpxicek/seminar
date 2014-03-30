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
public interface RecipeManager {

    /**
     * Stores new recipe into database. Id for the new recipe is automatically
     * generated and stored into id attribute.
     *
     * @param recipe recipe to be created.
     * @throws IllegalArgumentException when recipe is null, or recipe has already
     * assigned id.
     * @throws  ServiceFailureException when db operation fails.
     */
    void createRecipe(Recipe recipe) throws ServiceFailureException;

    /**
     * Deletes recipe from database.
     *
     * @param recipe recipe to be deleted from db.
     * @throws IllegalArgumentException when recipe is null, or recipe has null id.
     * @throws  ServiceFailureException when db operation fails.
     */
    void deleteRecipe(Recipe recipe) throws ServiceFailureException;

    /**
     * Returns list of all recipes in the database.
     *
     * @return list of all recipes in database.
     * @throws  ServiceFailureException when db operation fails.
     */
    List<Recipe> findAllRecipes() throws ServiceFailureException;

    /**
     * Finds and Returns recipe with given id.
     *
     * @param id primary key of requested recipe.
     * @return recipe with given id or null if such recipe does not exist.
     * @throws IllegalArgumentException when given id is null.
     * @throws  ServiceFailureException when db operation fails.
     */
    Recipe findRecipeById(Long id) throws ServiceFailureException;

    /**
     * Updates recipe elements in database.
     *
     * @param recipe updated recipe to be stored into database.
     * @throws IllegalArgumentException when recipe is null, or recipe has null id.
     * @throws  ServiceFailureException when db operation fails.
     */
    void updateRecipe(Recipe recipe) throws ServiceFailureException;

}
