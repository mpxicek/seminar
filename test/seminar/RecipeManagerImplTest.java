/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package seminar;

import common.DBUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 *
 * @author mpx
 */
public class RecipeManagerImplTest {

    private RecipeManagerImpl manager;
    private DataSource ds;

    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby://localhost:1527/contactDB;create=true");
        return ds;
    }
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,RecipeManager.class.getResource("CreateTables.sql"));
        manager = new RecipeManagerImpl();
        manager.setDataSource(ds);
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,RecipeManager.class.getResource("DropTables.sql"));
    }

    @Test
    public void createRecipe() {

        Recipe recipe = newRecipe("candlesauce","make it quick");
        manager.createRecipe(recipe);

        Long recipeId = recipe.getId();
        assertNotNull(recipeId);
        Recipe result = manager.findRecipeById(recipeId);
        assertEquals(recipe, result);
        assertNotSame(recipe, result);
        assertDeepEquals(recipe, result);
    }

    @Test
    public void findAllRecipes() {

        assertTrue(manager.findAllRecipes().isEmpty());

        Recipe recipe1 = newRecipe("candlesauce","make it quick");
        Recipe recipe2 = newRecipe("roast chicken","kill it first");

        manager.createRecipe(recipe1);
        manager.createRecipe(recipe2);

        List<Recipe> expected = new ArrayList();
        //Arrays.asList(recipe1,recipe2);
        expected.add(recipe1);
        expected.add(recipe2);
        List<Recipe> actual = manager.findAllRecipes();

        Collections.sort(actual,recipeComparator);
        Collections.sort(expected,recipeComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addRecipeWithNullAttribute() {

        manager.createRecipe(null);
    }

    /*@Test(expected=IllegalArgumentException.class)
    public void addTwoSameRecipe() {

        Recipe recipe1 = newRecipe("candlesauce","make it quick");
        Recipe recipe2 = newRecipe("candlesauce","make it quick");

        manager.createRecipe(recipe1);
        manager.createRecipe(recipe2);
    }*/

    @Test(expected=IllegalArgumentException.class)
    public void addRecipeWhitHardcodedId() {

        Recipe recipe1 = newRecipe("candlesauce","make it quick");
        recipe1.setId(1l);

        manager.createRecipe(recipe1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addRecipeWithNullNameAttribute() {

        Recipe recipe1 = newRecipe(null,"make it quick");

        manager.createRecipe(recipe1);
    }

    @Test
    public void addRecipeWithNullDirectionsAttribute() {
        Recipe recipe1 = newRecipe("candlesauce", null);
        manager.createRecipe(recipe1);
        Recipe result = manager.findRecipeById(recipe1.getId());
        assertNotNull(result);
        assertNull(result.getDirections());
    }

    @Test
    public void updateNameInRecipe() {
        Recipe recipe = newRecipe("candlesauce","make it quick");
        Recipe recipe2 = newRecipe("roast chicken","kill it first");
        manager.createRecipe(recipe);
        manager.createRecipe(recipe2);
        Long recipeId = recipe.getId();

        recipe = manager.findRecipeById(recipeId);
        recipe.setName("rumpsteak");
        manager.updateRecipe(recipe);
        assertEquals("rumpsteak", recipe.getName());
        assertEquals("make it quick", recipe.getDirections());
        
        // Check if updates didn't affected other records
        assertDeepEquals(recipe2, manager.findRecipeById(recipe2.getId()));
    }

    @Test
    public void updateDirectionsInRecipe() {
        Recipe recipe = newRecipe("candlesauce","make it quick");
        Recipe recipe2 = newRecipe("roast chicken","kill it first");
        manager.createRecipe(recipe);
        manager.createRecipe(recipe2);
        Long recipeId = recipe.getId();

        recipe = manager.findRecipeById(recipeId);
        recipe.setDirections("make it slow");
        manager.updateRecipe(recipe);
        assertEquals("candlesauce", recipe.getName());
        assertEquals("make it slow", recipe.getDirections());


        recipe = manager.findRecipeById(recipeId);
        recipe.setDirections(null);
        manager.updateRecipe(recipe);
        assertEquals("candlesauce", recipe.getName());
        assertNull(recipe.getDirections());

        // Check if updates didn't affected other records
        assertDeepEquals(recipe2, manager.findRecipeById(recipe2.getId()));
    }

    @Test(expected=IllegalArgumentException.class)
    public void updateRecipeWithNullAttribute() {

        Recipe recipe = newRecipe("candlesauce","make it quick");
        manager.createRecipe(recipe);
        Long recipeId = recipe.getId();

        manager.updateRecipe(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void updateRecipeWithNullIdAttribute() {

        Recipe recipe = newRecipe("candlesauce","make it quick");
        manager.createRecipe(recipe);
        Long recipeId = recipe.getId();

        recipe = manager.findRecipeById(recipeId);
        recipe.setId(null);
        manager.updateRecipe(recipe);
    }

    /*@Test(expected=IllegalArgumentException.class)
    public void updateRecipeWithDecreasedIdAttribute() {

        Recipe recipe = newRecipe("candlesauce","make it quick");
        manager.createRecipe(recipe);
        Long recipeId = recipe.getId();

        recipe = manager.findRecipeById(recipeId);
        recipe.setId(recipeId - 1);
        manager.updateRecipe(recipe);
    }*/

    @Test(expected=IllegalArgumentException.class)
    public void updateRecipeWithNullNameAttribute() {

        Recipe recipe = newRecipe("candlesauce","make it quick");
        manager.createRecipe(recipe);
        Long recipeId = recipe.getId();

        recipe = manager.findRecipeById(recipeId);
        recipe.setName(null);
        manager.updateRecipe(recipe);
    }

    @Test
    public void deleteRecipe() {

        Recipe recipe = newRecipe("candlesauce","make it quick");
        Recipe recipe2 = newRecipe("roast chicken","kill it first");
        manager.createRecipe(recipe);
        manager.createRecipe(recipe2);

        assertNotNull(manager.findRecipeById(recipe.getId()));
        assertNotNull(manager.findRecipeById(recipe2.getId()));

        manager.deleteRecipe(recipe);

        assertNull(manager.findRecipeById(recipe.getId()));
        assertNotNull(manager.findRecipeById(recipe2.getId()));

    }

    @Test(expected=IllegalArgumentException.class)
    public void deleteRecipeWithNullAttribute() {

        Recipe recipe = newRecipe("candlesauce","make it quick");

        manager.deleteRecipe(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void deleteRecipeWithNullIdAttribute() {

        Recipe recipe = newRecipe("candlesauce","make it quick");

        recipe.setId(null);
        manager.deleteRecipe(recipe);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findRecipeWithNullIdAttribute() {

        manager.findRecipeById(null);
    }

    private void assertDeepEquals(List<Recipe> expectedList, List<Recipe> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Recipe expected = expectedList.get(i);
            Recipe actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Recipe expected, Recipe actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDirections(), actual.getDirections());
    }

    private static Recipe newRecipe(String name, String directions) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDirections(directions);
        return recipe;
    }

    private static Comparator<Recipe> recipeComparator = new Comparator<Recipe>() {

        @Override
        public int compare(Recipe o1, Recipe o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };
}
