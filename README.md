# GenericAdapter 

An Easy to use adapter for android, a fork from [GenericAdapter](https://github.com/manojbhadane/GenericAdapter)

1. No need to create a separate class for each adapter
2. No need of ViewHolder 
3. More readable code

# Supports 2 types of adapters
***GenericAdapter :*** Adapter for simple usage

***GenericFilterAdapter :*** Adapter with list filtering capability

# Download

This library is available in **jitPack** which is the default Maven repository used in Android Studio.

## Gradle 
**Step 1.** Add it in your root build.gradle at the end of repositories
```Gradle
allprojects 
{
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency in your apps module build.gradle
```Gradle
dependencies {
    implementation 'com.github.ivankostadinovic:GenericAdapter:1.5.3'
}
```

# Usage

In App level build.gradle 
```Gradle
dataBinding {
        enabled true
}
```
### Sample code of adapter without filter
```Java
        adapter = new GenericAdapter<Radio, RvRadioItemBinding>(radios, R.layout.rv_radio_item) {
            @Override
            public void onBindData(Radio model, int position, RvRadioItemBinding dataBinding) {

            }

            @Override
            public void onItemClick(Radio model, int position) {

            }
        };
        recyclerView.setAdapter(adapter);
        //don't forget to set a LayoutManager to the recycler view
```
### Sample code of adapter with filter
```Java
        adapter = new GenericFilterAdapter<Radio, RvRadioItemBinding>(radios, R.layout.rv_radio_item, binding.editSearch) {
            @Override
            public void onBindData(Radio model, int position, RvRadioItemBinding dataBinding) {

            }

            @Override
            public void onItemClick(Radio model, int position) {
                
            }

            @Override
            public boolean filter(Radio item, String searchText) {
                return item.name.contains(text); //return true if the item matches the search text in the way you prefer
            }
        };
        recyclerView.setAdapter(adapter);
        //don't forget to set a LayoutManager to the recycler view
```

## OnCreateViewHolder override
You can optionally add things like focus listeners that would require to be set only once per view holder to the **onCreateHolder** method:
```Java
        ...
          @Override
            public void onCreateHolder(RvRadioItemBinding dataBinding) {

            }
        ...
```


## Pagination
Both adapters have support for pagination. If you wish to use pagination with these adapters, override the **loadMoreItems** method:
```java
        ...
          @Override
            public void loadMoreItems(int loadedCount) {
                //make API/db call
            }
        ...
        //when new items are fetched, add them to the adapter
        adapter.addItems(newItems);
```

## Binding data to the view
Instead of binding the data manually in the **onBindData** method, the library takes cares of this internally, and you can access that data through the XML.
The data variable name should be set to "data" in the layout for this to work. Example how a layout file should like in order to use this functionality.
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable
            name="data"
            type="com.arena.tv.models.model.Radio" />

        <import type="android.view.View" />
    </data>

    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        
        <TextView
            android:id="@+id/txt_name"
            android:text="@{data.name}"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

        <ImageView
            android:id="@+id/img_icon"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:focusable="false"
            android:focusableInTouchMode="false"
            app:imageUrl="@{data.stream_icon}" /> 
            <!-- imageUrl method is provided by a Binding Adapter, that is out of scope for this library-->
    </FrameLayout>

</layout>
```
If you don't like this, you can still bind the data in the **onBindData** method, and set up the XML layout accordingly.

## Adapter methods 
```java
        adapter = new GenericAdapter<Radio, RvRadioItemBinding>(radios, R.layout.rv_radio_item) {...}

        adapter.setItems(radios); //overrides the current list with a new list

        adapter.addItem(radio, itemPosition); //adds a single item at the given position

        adapter.addItems(radios); //adds new items at the end of the list

        adapter.removeItem(itemPosition); //removes item at the given position

        adapter.removeItem(radio); //removes the particular item, if it exists in the list

        adapter.clearItems(); //removes all the items from the adapter

        adapter.getItem(itemPosition); //retrieves the item at the given position

        adapter.updateItem(radio, itemPosition); //replaces the item at the position with a new item
```


# License

```
MIT License

Copyright (c) 2021 Ivan Kostadinovic

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

