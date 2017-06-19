package com.ibm.watson.scavenger.util;

import java.io.File;
import java.io.IOException;

public class ScavengerContants 
{
	public static String TTS_uname = "13719587-bb88-49de-a558-508df4ff0674",
			TTS_pass = "Gf161N5tk3eN",
			STT_uname = "7077f074-95e0-47fb-85b1-f985fd4ffc24",
			STT_pass = "GzZ0NhIb2oaH",
			ImageRecogAPIKey = "8481419871c4b6ec79a27dda81f436f109870d65",
			tmp_image_dir_path = "/tmp/Watson";
	
	public static File tmp_image_dir = null;
	
	static {
		tmp_image_dir = new File(tmp_image_dir_path);
				if(!tmp_image_dir.exists()){
					tmp_image_dir.mkdir();
					tmp_image_dir.setWritable(true);
				}
	}
}
