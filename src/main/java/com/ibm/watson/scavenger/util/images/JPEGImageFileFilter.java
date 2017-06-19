package com.ibm.watson.scavenger.util.images;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JPEGImageFileFilter extends FileFilter implements java.io.FileFilter
{
public boolean accept(File f)
  {
  if (f.getName().toLowerCase().endsWith(".jpeg")) return true;
  if (f.getName().toLowerCase().endsWith(".jpg")) return true;
  if (f.getName().toLowerCase().endsWith(".png")) return true;
  if(f.isDirectory())return true;
  return false;
 }
public String getDescription()
  {
  return "JPEG/PNG files";
  }

} 

