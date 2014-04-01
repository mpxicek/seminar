/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package seminar;

import common.DBUtils;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
/*kkkkuuuuk*/
import static org.junit.Assert.*;
import static seminar.IngredientManagerImplTest.newIngredient;
import static cz.muni.fi.pv168.gravemanager.backend.BodyManagerImplTest.assertBodyCollectionDeepEquals;
import static cz.muni.fi.pv168.gravemanager.backend.BodyManagerImplTest.assertBodyDeepEquals;
import static seminar.RecipeManagerImplTest.newRecipe;
import static cz.muni.fi.pv168.gravemanager.backend.GraveManagerImplTest.assertGraveCollectionDeepEquals;
import static cz.muni.fi.pv168.gravemanager.backend.GraveManagerImplTest.assertGraveDeepEquals;

/**
 *
 * @author mpx
 */
public class CookeryManagerImplTest {
    
    private CookeryManagerImpl manager;
    private IngredientManagerImpl ingredientManager;
    private RecipeManagerImpl recipeManager;
    private DataSource ds;

    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby://localhost:1527/contactDB;create=true");
        return ds;
    }

    private Recipe r1, r2, r3, recipeWithNullId, recipeNotInDB;
    private Ingredient i1, i2, i3, i4, i5, ingredientWithNullId, ingredientNotInDB;
    
    private void prepareTestData() {

        r1 = newRecipe(1, 2, 1, "Grave 1");
        r2 = newGrave(10, 11, 2, "Grave 2");
        r3 = newGrave(2, 2, 3, "Grave 3");
        
        i1 = newBody("Body 1", null, null, false);
        i2 = newBody("Body 2", null, null, true);
        i3 = newBody("Body 3", null, null, false);
        i4 = newBody("Body 4", null, null, false);
        i5 = newBody("Body 5", null, null, false);
        
        bodyManager.createBody(b1);
        bodyManager.createBody(b2);
        bodyManager.createBody(b3);
        bodyManager.createBody(b4);
        bodyManager.createBody(b5);
        
        graveManager.createGrave(g1);
        graveManager.createGrave(g2);
        graveManager.createGrave(g3);

        graveWithNullId = newGrave(1,1,1,"Grave with null id");
        graveNotInDB = newGrave(1,1,1,"Grave not in DB");
        graveNotInDB.setId(g3.getId() + 100);
        bodyWithNullId = newBody("Body with null id", null, null, true);
        bodyNotInDB = newBody("Body not in DB", null, null, true);
        bodyNotInDB.setId(b5.getId() + 100);
        
    }
    
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,CookeryManager.class.getResource("CreateTables.sql"));
        manager = new CookeryManagerImpl();
        manager.setDataSource(ds);
        ingredientManager = new IngredientManagerImpl();
        ingredientManager.setDataSource(ds);
        recipeManager = new RecipeManagerImpl();
        recipeManager.setDataSource(ds);
        prepareTestData();
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,CookeryManager.class.getResource("DropTables.sql"));
    }
}
