package org.projects.shoppinglist;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;


/**
 * Created by leon on 04-04-2016.
 */

public class ShoppingBag extends FirebaseListAdapter {


    private Firebase mref;
    private int viewId;
    private ShoppingLine backupLine;
    private int backupIndex;
    private ShoppingLine lastChanged = null;
    private static int noItems = -1;
    private int updatedCount = noItems;
    private AppendMethod appendMethod = AppendMethod.add;


    public ShoppingBag(Activity activity,Class modelClass, int modelLayout, Firebase ref, int viewId)
    {
        super(activity, modelClass, modelLayout, ref );
        mref=ref;
        this.viewId = viewId;
    }

    @Override
    protected void populateView(View view, Object o, int i) {

        TextView text = (TextView)view.findViewById(viewId);
        text.setText(o.toString());

    }

    public void deleteItem(int index)
    {
        Firebase itemRef = getRef(index);
        backupLine =(ShoppingLine) getItem(index);
        itemRef.removeValue();
        backupIndex = index;
        updatedCount--;
        lastChanged = backupLine;

    }

    public void undoDeleteItem()
    {
       updateBag(backupLine);
    }
    public void clear()
    {
        mref.removeValue();
        updatedCount = noItems;
    }


    public void setAppendMethod(AppendMethod appendMethod)
    {
        this.appendMethod = appendMethod;
    }

    public void updateBag(ShoppingLine line)
    {
        int numberOfItems = getCount();
        updatedCount = numberOfItems;

        lastChanged = line;
        if(appendMethod == AppendMethod.update)
        {
            for (int i = 0; i < numberOfItems; i++)
            {
                ShoppingLine l = (ShoppingLine)  getItem(i);
                if(l.equals(line.getProduct())) {
                    l.setCount(l.getCount() + line.getCount());
                    getRef(i).removeValue();
                    line = l;
                }
            }
        }
        lastChanged = line;
        mref.push().setValue(line);
        updatedCount ++;

    }

    @Override
    public String toString() {
        if(updatedCount == noItems && getCount()<=0 )
            return "";

        int numberOfItems = getCount();
        //If there is more items in snapshot than updatedCount we have deleted an item
        boolean isdeleted = numberOfItems > updatedCount && lastChanged != null;
        //If there is less items in snapshot than updatedCount we have added an item
        boolean addItem = numberOfItems < updatedCount;


        StringBuilder sb = new StringBuilder();
        ArrayList<ShoppingLine> lines = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++)
        {
            lines.add((ShoppingLine) getItem(i));
        }

        if(isdeleted)
        {
            lines.remove(lastChanged);
        }
        if(addItem) {
            if(AppendMethod.update == appendMethod)
                lines.remove(lastChanged.getProduct());

            lines.add(lastChanged);
        }
        for(ShoppingLine line:lines)
        {
            sb.append(line.toString());
            sb.append("\r\n");
        }

        return sb.toString();
    }
}
