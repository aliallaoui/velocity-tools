package org.apache.velocity.tools.generic;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Container for *either* an array *or* an object
 */

public class JsonContent
{
    /**
     * JSONObject content
     */
    private JSONObject jsonObject = null;
        
    /**
     * JSONArray content
     */
    private JSONArray jsonArray = null;

    /**
     * wraps the object into an hybrid JSON container if necessary
     */
    private static Object wrapIfNeeded(Object obj)
    {
        if (obj == null)
        {
            return obj;
        }
        else if (obj instanceof JSONArray)
        {
            return new JsonContent((JSONArray)obj);
        }
        else if (obj instanceof JSONObject)
        {
            return new JsonContent((JSONObject)obj);
        }
        else
        {
            return obj;
        }
    }
        
    /**
     * wraps an object
     */
    public JsonContent(JSONObject object)
    {
        jsonObject = object;
    }

    /**
     * wraps an array
     */
    public JsonContent(JSONArray array)
    {
        jsonArray = array;
    }

    /**
     * Get a value from root array
     * @param key
     * @return value, or null
     */
    public Object get(int index)
    {
        Object ret = null;
        if (jsonArray != null)
        {
            ret = wrapIfNeeded(jsonArray.get(index));
        }
        else if (jsonObject != null)
        {
            ret = wrapIfNeeded(jsonObject.get(String.valueOf(index)));
        }
        return ret;
    }
        
    /**
     * Get a property from root object
     * @param key
     * @return property value, or null
     */
    public Object get(String key)
    {
        Object ret = null;
        if (jsonArray != null)
        {
            try
            {
                ret = wrapIfNeeded(jsonArray.get(Integer.parseInt(key)));
            }
            catch (NumberFormatException nfe) {}
        }
        else if (jsonObject != null)
        {
            ret = wrapIfNeeded(jsonObject.get(key));
        }
        return ret;
    }
        
    /**
     * Iterate keys of root object.
     * @return iterator
     */
    public Iterator<String> keys()
    {
        return jsonObject == null ? null : jsonObject.keySet().iterator();
    }

    /**
     * Get set of root object keys.
     * @return
     */
    public Set<String> keySet()
    {
        return jsonObject == null ? null : jsonObject.keySet();
    }

    /**
     * Get an iterator. For a root object, returns an iterator over key names. For a root array, returns an iterator
     * over contained objects.
     * @return iterator
     */
    public Iterator iterator()
    {
        if (jsonObject != null)
        {
            return jsonObject.keySet().iterator();
        }
        else if (jsonArray != null)
        {
            return jsonArray.iterator();
        }
        return null;
    }

    /**
     * Get size of root object or array.
     * @return size
     */
    public int size()
    {
        return jsonObject == null ? jsonArray == null ? null : jsonArray.size() : jsonObject.size();
    }

    /**
     * Convert JSON object or array into string
     * @return JSON representation of the root object or array
     */
    public String toString()
    {
        return jsonObject == null ? jsonArray == null ? "null" : jsonArray.toString() : jsonObject.toString();
    }        

    /**
     * Check if wrapped object is null
     * @return true if wrapped object is null
     */
    public boolean isNull()
    {
        return jsonArray == null && jsonObject == null;
    }
        
    /**
     * Check if wrapped object is a JSONObject
     * @return true if wrapped object is a JSONObject         
     */
    public boolean isObject()
    {
        return jsonObject != null;
    }
        
    /**
     * Check if wrapped object is a JSONArray
     * @return true if wrapped object is a JSONArray
     */
    public boolean isArray()
    {
        return jsonArray != null;
    }

    public Map<String,Object> getMap()
    {
        Map<String,Object> res = new HashMap<String,Object>();
        if(jsonObject != null)
            {
                Set<Entry<String, Object>> set_up = jsonObject.entrySet();
                Iterator<Entry<String,Object>> it = set_up.iterator();
                while(it.hasNext())
                    {
                        Entry<String,Object> e = it.next();
                        res.put(e.getKey(),e.getValue());
                    }

            }
        return res;
    }

    public List<Map<String,Object>> getMapList()
    {
        List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
        if(jsonArray != null)
            {
                for(Object o:jsonArray)
                    {
                        if(o instanceof JSONObject)
                            {
                                Map<String,Object> jom = new HashMap<String,Object>();
                                Set<Entry<String, Object>> set_up = ((JSONObject)o).entrySet();
                                Iterator<Entry<String,Object>> it = set_up.iterator();
                                while(it.hasNext())
                                    {
                                        Entry<String,Object> e = it.next();
                                        jom.put(e.getKey(),e.getValue());
                                    }
                                res.add(jom);
                            }
                        //                            else log.warn("getMapList should be called only on simple jsonarray containing only jsonobjects");
                    }
            }
        return res;
    }

}
