package com.jameswu.demo.model.payload;

import java.io.Serializable;
import java.util.HashMap;

public class NewOrderPayload extends HashMap<String, BuyingProductPayload>
		implements Serializable {}
