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
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerTestParameterized {

    private Burger burger;
    private final Bun bunMock;
    private final Ingredient ingredientMock1;
    private final Ingredient ingredientMock2;

    public BurgerTestParameterized(Bun bunMock, Ingredient ingredientMock1, Ingredient ingredientMock2) {
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
    public void testGetPrice() {
        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);

        float expectedPrice = bunMock.getPrice() * 2 + ingredientMock1.getPrice() + ingredientMock2.getPrice();
        float actualPrice = burger.getPrice();

        assertEquals(expectedPrice, actualPrice, 0.0001);
    }

    @Test
    public void testGetReceipt() {
        burger.setBuns(bunMock);
        burger.addIngredient(ingredientMock1);
        burger.addIngredient(ingredientMock2);

        String receipt = burger.getReceipt();

        String expectedReceipt = String.format(
                "(==== %s ====)%n" +
                        "= %s %s =%n" +
                        "= %s %s =%n" +
                        "(==== %s ====)%n" +
                        "%nPrice: %f%n",
                bunMock.getName(),
                ingredientMock1.getType().toString().toLowerCase(),
                ingredientMock1.getName(),
                ingredientMock2.getType().toString().toLowerCase(),
                ingredientMock2.getName(),
                bunMock.getName(),
                burger.getPrice()
        );

        assertEquals(expectedReceipt, receipt);
    }
}