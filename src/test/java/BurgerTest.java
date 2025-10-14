import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;

    private Bun bunMock;
    private Ingredient ingredientMock1;
    private Ingredient ingredientMock2;

    public BurgerTest(Bun bunMock, Ingredient ingredientMock1, Ingredient ingredientMock2) {
        this.bunMock = bunMock;
        this.ingredientMock1 = ingredientMock1;
        this.ingredientMock2 = ingredientMock2;
    }

    @Before
    public void setUp() {
        burger = new Burger();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Bun bunMock = mock(Bun.class);
        when(bunMock.getName()).thenReturn("Test Bun");
        when(bunMock.getPrice()).thenReturn(100f);

        Ingredient ingredientMock1 = mock(Ingredient.class);
        when(ingredientMock1.getName()).thenReturn("Hot Sauce");
        when(ingredientMock1.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredientMock1.getPrice()).thenReturn(50f);

        Ingredient ingredientMock2 = mock(Ingredient.class);
        when(ingredientMock2.getName()).thenReturn("Sausage");
        when(ingredientMock2.getType()).thenReturn(IngredientType.FILLING);
        when(ingredientMock2.getPrice()).thenReturn(70f);

        return Arrays.asList(new Object[][]{
                {bunMock, ingredientMock1, ingredientMock2}
        });
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bunMock);
        assertEquals(bunMock, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(ingredientMock1);
        assertTrue(burger.ingredients.contains(ingredientMock1));
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);
        int initialSize = burger.ingredients.size();
        burger.removeIngredient(0);
        assertEquals(initialSize - 1, burger.ingredients.size());
        assertFalse(burger.ingredients.contains(ingredientMock1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientOutOfBounds() {
        burger.removeIngredient(0);
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);
        burger.moveIngredient(0, 1);

        assertEquals(ingredientMock2, burger.ingredients.get(0));
        assertEquals(ingredientMock1, burger.ingredients.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientOutOfBounds() {
        burger.moveIngredient(0, 1);
    }

    @Test
    public void testGetPrice() {
        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);

        float expectedPrice = bunMock.getPrice() * 2 + ingredientMock1.getPrice() + ingredientMock2.getPrice();
        float actualPrice = burger.getPrice();

        assertEquals(expectedPrice, actualPrice, 0.0001);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPriceWithoutBun() {
        burger.getPrice();
    }

    @Test
    public void testGetReceipt() {
        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);

        String receipt = burger.getReceipt();

        // Проверяем структуру чека
        String expectedHeader = String.format("(==== %s ====)%n", bunMock.getName());
        String expectedFooter = String.format("(==== %s ====)%n", bunMock.getName());
        String expectedSauceLine = String.format("= %s %s =%n", ingredientMock1.getType().toString().toLowerCase(), ingredientMock1.getName());
        String expectedFillingLine = String.format("= %s %s =%n", ingredientMock2.getType().toString().toLowerCase(), ingredientMock2.getName());
        String expectedPriceLine = String.format("%nPrice: %f%n", burger.getPrice());

        assertTrue(receipt.startsWith(expectedHeader));
        assertTrue(receipt.contains(expectedSauceLine));
        assertTrue(receipt.contains(expectedFillingLine));
        assertTrue(receipt.contains(expectedFooter));
        assertTrue(receipt.endsWith(expectedPriceLine));
    }

    @Test(expected = NullPointerException.class)
    public void testGetReceiptWithoutBun() {
        burger.getReceipt();
    }
}
