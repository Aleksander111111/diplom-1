import org.junit.Before;
import org.junit.Test;
import praktikum.Bun;
import praktikum.Burger;
import praktikum.Ingredient;
import praktikum.IngredientType;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BurgerTest {

    private Burger burger;
    private Bun bunMock;
    private Ingredient sauceIngredientMock;
    private Ingredient fillingIngredientMock;

    @Before
    public void setUp() {
        burger = new Burger();

        bunMock = mock(Bun.class);
        when(bunMock.getName()).thenReturn("Test Bun");
        when(bunMock.getPrice()).thenReturn(100f);

        sauceIngredientMock = mock(Ingredient.class);
        when(sauceIngredientMock.getName()).thenReturn("Hot Sauce");
        when(sauceIngredientMock.getType()).thenReturn(IngredientType.SAUCE);
        when(sauceIngredientMock.getPrice()).thenReturn(50f);

        fillingIngredientMock = mock(Ingredient.class);
        when(fillingIngredientMock.getName()).thenReturn("Sausage");
        when(fillingIngredientMock.getType()).thenReturn(IngredientType.FILLING);
        when(fillingIngredientMock.getPrice()).thenReturn(70f);
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bunMock);
        assertEquals(bunMock, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(sauceIngredientMock);
        assertTrue(burger.ingredients.contains(sauceIngredientMock));
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(sauceIngredientMock);
        burger.addIngredient(fillingIngredientMock);

        burger.removeIngredient(0);

        assertFalse(burger.ingredients.contains(sauceIngredientMock));
        assertTrue(burger.ingredients.contains(fillingIngredientMock));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientWhenIndexOutOfBounds() {
        burger.removeIngredient(0);
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(sauceIngredientMock);
        burger.addIngredient(fillingIngredientMock);

        burger.moveIngredient(0, 1);

        assertEquals(fillingIngredientMock, burger.ingredients.get(0));
        assertEquals(sauceIngredientMock, burger.ingredients.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMoveIngredientWhenIndexOutOfBounds() {
        burger.moveIngredient(0, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPriceWhenBunNotSet() {
        burger.getPrice();
    }

    @Test(expected = NullPointerException.class)
    public void testGetReceiptWhenBunNotSet() {
        burger.getReceipt();
    }

    @Test
    public void testGetPriceWithOnlyBun() {
        burger.setBuns(bunMock);

        float expectedPrice = bunMock.getPrice() * 2;
        float actualPrice = burger.getPrice();

        assertEquals(expectedPrice, actualPrice, 0.0001);
    }

    @Test
    public void testGetReceiptWithOnlyBun() {
        burger.setBuns(bunMock);

        String receipt = burger.getReceipt();

        String expectedReceipt = String.format(
                "(==== %s ====)%n" +
                        "(==== %s ====)%n" +
                        "%nPrice: %f%n",
                bunMock.getName(),
                bunMock.getName(),
                burger.getPrice()
        );

        assertEquals(expectedReceipt, receipt);
    }

    @Test
    public void testGetReceiptWithMultipleIngredients() {
        burger.setBuns(bunMock);
        burger.addIngredient(sauceIngredientMock);
        burger.addIngredient(fillingIngredientMock);
        burger.addIngredient(sauceIngredientMock); // Добавляем еще один ингредиент

        String receipt = burger.getReceipt();

        String expectedReceipt = String.format(
                "(==== %s ====)%n" +
                        "= %s %s =%n" +
                        "= %s %s =%n" +
                        "= %s %s =%n" +
                        "(==== %s ====)%n" +
                        "%nPrice: %f%n",
                bunMock.getName(),
                sauceIngredientMock.getType().toString().toLowerCase(),
                sauceIngredientMock.getName(),
                fillingIngredientMock.getType().toString().toLowerCase(),
                fillingIngredientMock.getName(),
                sauceIngredientMock.getType().toString().toLowerCase(),
                sauceIngredientMock.getName(),
                bunMock.getName(),
                burger.getPrice()
        );

        assertEquals(expectedReceipt, receipt);
    }

    @Test
    public void testMoveIngredientToSamePosition() {
        burger.addIngredient(sauceIngredientMock);
        burger.addIngredient(fillingIngredientMock);

        Ingredient firstBeforeMove = burger.ingredients.get(0);
        Ingredient secondBeforeMove = burger.ingredients.get(1);

        burger.moveIngredient(0, 0);

        assertEquals(firstBeforeMove, burger.ingredients.get(0));
        assertEquals(secondBeforeMove, burger.ingredients.get(1));
    }

    @Test
    public void testRemoveIngredientFromMiddle() {
        Ingredient thirdIngredientMock = mock(Ingredient.class);
        when(thirdIngredientMock.getName()).thenReturn("Cheese");
        when(thirdIngredientMock.getType()).thenReturn(IngredientType.FILLING);
        when(thirdIngredientMock.getPrice()).thenReturn(90f);

        burger.addIngredient(sauceIngredientMock);
        burger.addIngredient(fillingIngredientMock);
        burger.addIngredient(thirdIngredientMock);

        burger.removeIngredient(1); // Удаляем средний ингредиент

        assertEquals(2, burger.ingredients.size());
        assertEquals(sauceIngredientMock, burger.ingredients.get(0));
        assertEquals(thirdIngredientMock, burger.ingredients.get(1));
    }

    @Test
    public void testAddMultipleIngredientsAndCheckOrder() {
        burger.addIngredient(sauceIngredientMock);
        burger.addIngredient(fillingIngredientMock);

        assertEquals(2, burger.ingredients.size());
        assertEquals(sauceIngredientMock, burger.ingredients.get(0));
        assertEquals(fillingIngredientMock, burger.ingredients.get(1));
    }

    @Test
    public void testBurgerInitialState() {
        assertNotNull(burger.ingredients);
        assertTrue(burger.ingredients.isEmpty());
        assertNull(burger.bun);
    }
}