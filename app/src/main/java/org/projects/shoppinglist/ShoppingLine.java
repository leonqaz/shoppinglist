package org.projects.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leon on 29-02-2016.
 */
public class ShoppingLine implements Parcelable{
    private Product product;
    private int count;

    public ShoppingLine()
    {}
    public ShoppingLine(Product prod, int c)
    {
        product = prod;
        count = c;
    }

    public void setProduct(Product p)
    {
        product = p;
    }

    public int getCount()
    {
        return count;
    }
    public void setCount(int newCount)
    {
        count = newCount;
    }
    public Product getProduct() {
        return product;
    }

    public static final Parcelable.Creator<ShoppingLine> CREATOR
            = new Parcelable.Creator<ShoppingLine>() {
        public ShoppingLine createFromParcel(Parcel in) {
            return new ShoppingLine(in);
        }

        public ShoppingLine[] newArray(int size) {
            return new ShoppingLine[size];
        }
    };

    private ShoppingLine(Parcel in) {
        count = in.readInt();
        product = new Product(in.readString());
    }

    @Override
    public String toString() {
        return count +" " + product.getProductName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeString(product.toString());
    }
    /***/
    @Override
    public boolean equals(Object other)
    {
        if(other == null)
            return false;
        if(other instanceof ShoppingLine )
        {
            return ((ShoppingLine) other).toString().equalsIgnoreCase(this.toString());
        }
        if(other instanceof Product )
            return other.equals(this.product);
        return super.equals(other);

    }



}
