package org.zeith.hammerlib.util.shaded.json.serapi;

import java.util.HashMap;
import java.util.Map;

public class SerializationContext
{
	public Map<String, Object> objects = new HashMap<>();

	public void set(String s, Object o)
	{
		objects.put(s, o);
	}
}