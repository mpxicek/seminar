/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seminar;

import common.IllegalEntityException;
import common.ServiceFailureException;
import java.util.List;

/**
 *
 * @author mpx
 */
public interface CookeryManager {

    /**
     * Find recipe that contains given ingredients. If the ingredient is not placed 
     * in any recipe, null is returned.
     * 
     * @param ingredient ingredient that we want to find
     * @return collection of recipes that contains given ingredient or null if there is no such recipe
     * @throws IllegalArgumentException when ingredient is null.
     * @throws IllegalEntityException when given ingredient has null id 
     * @throws ServiceFailureException when db operation fails
     */
    List<Recipe> findRecipeWithIngredients(Ingredient ingredient) throws ServiceFailureException, IllegalEntityException;

    /**
     * Find all ingredients that are in given recipe.
     * 
     * @param recipe recipe that we want to search
     * @return collection of ingredients contained in given recipe
     * @throws IllegalArgumentException when recipe is null
     * @throws IllegalEntityException when given recipe has null id 
     * @throws ServiceFailureException when db operation fails
     */
    List<Ingredient> getIngredientsInRecipe(Recipe recipe) throws ServiceFailureException, IllegalEntityException;

    /**
     * Inserts ingredient into given recipe.
     * 
     * @param recipe recipe for placing ingredients
     * @param ingredient ingredient to be placed to given recipe
     * @throws IllegalArgumentException when recipe or ingredient is null
     * @throws IllegalEntityException when ingredient is already placed in some recipe,
     * when grave is already full or when body or grave have null id or do 
     * not exist in database 
     * @throws ServiceFailureException when db operation fails.
     */
    void putIngredientsInRecipe(Recipe recipe, Ingredient ingredient) throws ServiceFailureException, IllegalEntityException;

    /**
     * Removes body from given grave.
     * 
     * @param recipe recipe for removing given ingredient
     * @param ingredient ingredient to be removed from given recipe
     * @throws IllegalArgumentException when recipe or ingredient is null
     * @throws IllegalEntityException when given ingredient is not placed in given 
     * recipe or when ingredient or recipe have null id or do not exist in database
     * @throws ServiceFailureException when db operation fails.
     */
    void removeIngredientsFromRecipe(Recipe recipe, Ingredient ingredient) throws ServiceFailureException, IllegalEntityException;

}
