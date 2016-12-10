package com.ssca.format;

public class DexHeader {
	public byte[] magic           = new byte[8];
	public byte[] checksum        = new byte[4];
	public byte[] signature 	  = new byte[20];
	public byte[] file_size 	  = new byte[4];
	public byte[] header_size  	  = new byte[4];
	public byte[] endian_tag      = new byte[4];
	public byte[] link_size		  = new byte[4];
	public byte[] link_off 		  = new byte[4];
	public byte[] map_off 		  = new byte[4];
	public byte[] string_ids_size = new byte[4];
	public byte[] string_ids_off  = new byte[4];
	public byte[] type_ids_size   = new byte[4];
	public byte[] type_ids_off    = new byte[4];
	public byte[] proto_ids_size  = new byte[4];
	public byte[] proto_ids_off    = new byte[4];
	public byte[] field_ids_size  = new byte[4];
	public byte[] field_ids_off	  = new byte[4];
	public byte[] method_ids_size = new byte[4];
	public byte[] method_ids_off  = new byte[4];
	public byte[] class_defs_size = new byte[4];
	public byte[] class_defs_off  = new byte[4];
	public byte[] data_size		  = new byte[4];
	public byte[] data_off		  = new byte[4];
	
}
