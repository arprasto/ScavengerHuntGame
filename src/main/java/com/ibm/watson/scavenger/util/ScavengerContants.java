package com.ibm.watson.scavenger.util;

import java.io.File;
import java.io.IOException;

public class ScavengerContants 
{
	public static String TTS_uname = "xxxx",
			TTS_pass = "XXXX",
			STT_uname = "xxxx",
			STT_pass = "xxxx",
			ImageRecogAPIKey = "xxxx",
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
