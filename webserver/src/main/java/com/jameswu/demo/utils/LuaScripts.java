package com.jameswu.demo.utils;

public class LuaScripts {
	public static final String BOOKING_SPECIALS_LUA_script =
			"""
					local n = tonumber(ARGV[1])\s
					if not n  or n == 0 then\s
						return 0\s
					end\s
					local vals = redis.call("HMGET", KEYS[1], "inventory", "booked");\s
					local total = tonumber(vals[1])\s
					local blocked = tonumber(vals[2])\s
					if not total or not blocked then\s
						return 0\s
					end\s
					if blocked + n <= total then\s
						redis.call("HINCRBY", KEYS[1], "booked", n)\s
						return n;\s
					end\s
					return 0
					""";
}
