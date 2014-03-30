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
import java.util.Arrays;
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
public class IngredientManagerImplTest {

    private IngredientManagerImpl manager;
    private DataSource ds;

    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby://localhost:1527/contactDB;create=true");
        return ds;
    }
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,IngredientManager.class.getResource("CreateTables.sql"));
        manager = new IngredientManagerImpl();
        manager.setDataSource(ds);
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,IngredientManager.class.getResource("DropTables.sql"));
    }

    @Test
    public void createIngredient() {

        Ingredient ingredient = newIngredient("egg");
        manager.createIngredient(ingredient);

        Long ingredientId = ingredient.getId();
        assertNotNull(ingredientId);
        Ingredient result = manager.findIngredientById(ingredientId);
        assertEquals(ingredient, result);
        assertNotSame(ingredient, result);
        assertDeepEquals(ingredient, result);
    }

    @Test
    public void findAllIngredients() {

        assertTrue(manager.findAllIngredients().isEmpty());

        Ingredient ingredient1 = newIngredient("egg");
        Ingredient ingredient2 = newIngredient("milk");

        manager.createIngredient(ingredient1);
        manager.createIngredient(ingredient2);

        List<Ingredient> expected = new ArrayList();
        //Arrays.asList(recipe1,recipe2);
        expected.add(ingredient1);
        expected.add(ingredient2);
        List<Ingredient> actual = manager.findAllIngredients();

        Collections.sort(actual,ingredientComparator);
        Collections.sort(expected,ingredientComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addIngredientWithNullAttribute() {
  
            manager.createIngredient(null);
    }

    /*@Test(expected=IllegalArgumentException.class)
    public void addTwoSameIngredients() {

        Ingredient ingredient1 = newIngredient("egg");
        Ingredient ingredient2 = newIngredient("egg");

        manager.createIngredient(ingredient1);
        manager.createIngredient(ingredient2);
    }*/

    @Test(expected=IllegalArgumentException.class)
    public void addIngredientWithHardcodedId() {

        Ingredient ingredient1 = newIngredient("egg");

        ingredient1.setId(1l);

        manager.createIngredient(ingredient1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addIngredientWithNullNameAttribute() {

        Ingredient ingredient1 = newIngredient(null);

        manager.createIngredient(ingredient1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void findIngredientWithNullIdAttribute() {

        manager.findIngredientById(null);
    }
    
    private void assertDeepEquals(List<Ingredient> expectedList, List<Ingredient> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Ingredient expected = expectedList.get(i);
            Ingredient actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Ingredient expected, Ingredient actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    static Ingredient newIngredient(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        return ingredient;
    }

    private static Comparator<Ingredient> ingredientComparator = new Comparator<Ingredient>() {

        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };
}
