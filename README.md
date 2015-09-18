HashMapAdapter
==============

This project provides a HashMapAdapter that seems to be missing from Android.

It can be used with a Spinner or AutoCompleteTextView to separate the data from the display text
much like the HTML \<OPTION\> tag.

```java

    mapData = new LinkedHashMap<String, String>();
    mapData.put("shamu", "Nexus 6");
    mapData.put("fugu", "Nexus Player");
    mapData.put("volantisg", "Nexus 9 (LTE)");
    mapData.put("volantis", "Nexus 9 (Wi-Fi)");
    mapData.put("hammerhead", "Nexus 5 (GSM/LTE)");
    mapData.put("razor", "Nexus 7 [2013] (Wi-Fi)");
    mapData.put("razorg", "Nexus 7 [2013] (Mobile)");
    mapData.put("mantaray", "Nexus 10");
    mapData.put("occam", "Nexus 4");
    mapData.put("nakasi", "Nexus 7 (Wi-Fi)");
    mapData.put("nakasig", "Nexus 7 (Mobile)");
    mapData.put("tungsten", "Nexus Q");

    // autocomplete only examines the KEY while filtering
    acadapter = new LinkedHashMapAdapter<String, String>(this, android.R.layout.simple_list_item_1, mapData, LinkedHashMapAdapter.FLAG_FILTER_ON_KEY);

    // autocomplete only examines the VALUE while filtering
    // acadapter = new LinkedHashMapAdapter<String, String>(this, android.R.layout.simple_list_item_1, mapData, LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE);

    // autocomplete examines the KEY or VALUE while filtering
    // acadapter = new LinkedHashMapAdapter<String, String>(this, android.R.layout.simple_list_item_1, mapData, LinkedHashMapAdapter.FLAG_FILTER_ON_KEY | LinkedHashMapAdapter.FLAG_FILTER_ON_VALUE);


    // fill in the EditText with the VALUE when an item is selected (default KEY)
    // acadapter = new LinkedHashMapAdapter<String, String>(this, android.R.layout.simple_list_item_1, mapData, LinkedHashMapAdapter.FLAG_FILTER_ON_KEY | LinkedHashMapAdapter.FLAG_FILTER_RESULT_USE_VALUE);

    autocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete);
    autocomplete.setAdapter(acadapter);
```


License
-------

    Copyright 2015 James Ayvaz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
