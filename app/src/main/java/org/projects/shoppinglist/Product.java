package org.projects.shoppinglist;

/**
 * Created by leon on 29-02-2016.
 */
public class Product {
    private String productName;

    public  Product ()
    {

    }
    public Product(String productName)
    {
        this.productName = productName;
    }
    public String getProductName()
    {
        return productName;
    }
    public void setProductName(String name)
    {
        productName = name;
    }

    @Override
    public String toString() {
        return productName;
    }
    @Override
    public boolean equals(Object other)
    {
        if(other == null)
            return false;
        if(other instanceof Product)
            return ((Product)other).toString().equalsIgnoreCase(this.toString());
        if(other instanceof ShoppingLine)
            return ((ShoppingLine)other).getProduct().toString().equalsIgnoreCase(this.toString());
        return super.equals(other);
    }
}
