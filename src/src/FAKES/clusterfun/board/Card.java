package clusterfun.board;
import java.lang.String;

/**
 * Fake Card Class
 */
//  Version History
//      12/2/08 Andrew Chan added algorithm pseudocode
public class Card
{
    /**
     * Enumeration for the properties of the card
     */
    public static enum Properties
    {
        /** The number of symbols on the card */
        Number,
        /** The color of the symbol on the card */
        Color,
        /** The symbol on the card */
        Symbol,
        /** The fill or shading of the card */
        Fill
    }
    
    /**
     * Enumeration for the number of symbols on the Card
     */
    public static enum NumberType
    {
        /** The first number */
        One,
        /** The second number */
        Two,
        /** The third number */
        Three
    }
    
    /**
     * Enumeration for the color of the Card
     */
    public static enum ColorType
    {
        /** The first color */
        Red,
        /** The second color */
        Purple,
        /** The third color */
        Green
    }
    
    
    /**
     * Enumeration for the symbol on the card
     */
    public static enum SymbolType
    {
        /** The first symbol */
        Squiggles,
        /** The second symbol */
        Diamonds,
        /** The third symbol */
        Ovals
    }
    
    /**
     * Enumeration for the fill on the card
     */
    public static enum FillType
    {
        /** The first fill */
        Solid,
        /** The second fill */
        Striped,
        /** The third fill */
        Empty
    }
    
    /**
     * The color of the card
     */
    private ColorType color;
    
    /**
     * The number of patterns on the card
     */
    private NumberType number;
    
    /**
     * The symbol on the card
     */
    private SymbolType symbol;
    
    /**
     * The fill or shading of the card
     */
    private FillType fill;

    /**
     * Constructor for a Card
     */
    public Card()
    {
        
    }

    /**
     * Constructor for a Card to set all the parameters
     * 
     * @param color     The color of the symbols on the card
     * @param number    The number of symbols on the card
     * @param symbol    The symbol on the card
     * @param fill      The fill or shading of the card
     */
    public Card(NumberType number, ColorType color, 
                SymbolType symbol, FillType fill)
    {
       this.color = color;
       this.number = number;
       this.symbol = symbol;
       this.fill = fill;
    }
    
    /**
     * Constructor for a Card to set all the parameters using strings
     * 
     * @param color     The color of the symbols on the card
     * @param number    The number of symbols on the card
     * @param symbol    The symbol on the card
     * @param fill      The fill or shading of the card
     */
    public Card(String number, String color, 
                String symbol, String fill)
    {
       this.color = ColorType.valueOf(color);
       this.number = NumberType.valueOf(number);
       this.symbol = SymbolType.valueOf(symbol);
       this.fill = FillType.valueOf(fill);
    }
    
    /**
     * Saves the color of the card
     * 
     * @param color the color to be saved
     */
    public void setColor(ColorType color)
    {
        this.color = color;
    }
    
    /**
     * Requests the color of the card
     * 
     * @return the color of the card
     */
    public ColorType getColor()
    {
        return color;
    }

    /**
     * Saves the number of symbols on the card
     * 
     * @param number the number symbols on the card to be saved
     */
    public void setNumber(NumberType number)
    {
        this.number = number;
    }
    
    /**
     * Requests the number of symbols on the card
     * 
     * @return the number of symbols on the card
     */
    public NumberType getNumber()
    {
        return number;
    }

    /**
     * Saves the symbol used on the card
     * 
     * @param symbol the symbol used on the card
     */
    public void setSymbol(SymbolType symbol)
    {
        this.symbol = symbol;
    }
    
    /**
     * Requests the symbol used on the card
     * 
     * @return the symbol used on the card
     */
    public SymbolType getSymbol()
    {
        return symbol;
    }
    
    /**
     * Saves the fill or shading of the card
     * 
     * @param fill the fill or shading of the card
     */
    public void setFill(FillType fill)
    {
        this.fill = fill;
    }
    
    /**
     * Requests the fill or shading of the card
     * 
     * @return the fill or shading of the card
     */
    public FillType getFill()
    {
        return fill;
    }
    
    /**
     * Requests a string representation of the card
     *
     * @return a string representation of the card
     */
    @Override
    public String toString()
    {
        return getNumber() + " " + getColor()
               + " " + getSymbol() + " " + getFill();
    }
    
    /**
     * Returns the property value given a property
     * 
     * @param property  the requested property
     * @return          the requested property value
     */
    public Enum getProperty(Properties property)
    {
        //SWITCH return the requested property value
        switch(property)
        {
            case Color:
                return getColor();
            case Fill:
                return getFill();
            case Number:
                return getNumber();
            case Symbol:
                return getSymbol();
            default:
                return null;
        }
    }
    
    public boolean equals(Card compareCard)
    {
        return this.toString().equals(compareCard.toString());
    }
}
