package minecraft.planner.model.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Tag {
   private final Tag.Type type;
   private Tag.Type listType = null;
   private final String name;
   private Object value;

   public Tag(Tag.Type type, String name, Tag[] value) {
      this(type, name, value);
   }

   public Tag(String name, Tag.Type listType) {
      this(Tag.Type.TAG_List, name, listType);
   }

   public Tag(Tag.Type type, String name, Object value) {
      if (type == Tag.Type.TAG_Compound && !(value instanceof Tag[])) {
         throw new IllegalArgumentException();
      }

      switch (type) {
         case TAG_End:
            if (value != null) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_Byte:
            if (!(value instanceof Byte)) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_Short:
            if (!(value instanceof Short)) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_Int:
            if (!(value instanceof Integer)) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_Long:
            if (!(value instanceof Long)) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_Float:
            if (!(value instanceof Float)) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_Double:
            if (!(value instanceof Double)) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_Byte_Array:
            if (!(value instanceof byte[])) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_String:
            if (!(value instanceof String)) {
               throw new IllegalArgumentException();
            }
            break;
         case TAG_List:
            if (value instanceof Tag.Type) {
               this.listType = (Tag.Type)value;
               value = new Tag[0];
            } else {
               if (!(value instanceof Tag[])) {
                  throw new IllegalArgumentException();
               }

               this.listType = ((Tag[])value)[0].getType();
            }
            break;
         case TAG_Compound:
            if (!(value instanceof Tag[])) {
               throw new IllegalArgumentException();
            }
            break;
         default:
            throw new IllegalArgumentException();
      }

      this.type = type;
      this.name = name;
      this.value = value;
   }

   public Tag.Type getType() {
      return this.type;
   }

   public String getName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }

   public Tag.Type getListType() {
      return this.listType;
   }

   public void addTag(Tag tag) {
      if (this.type != Tag.Type.TAG_List && this.type != Tag.Type.TAG_Compound) {
         throw new RuntimeException();
      }

      Tag[] subtags = (Tag[])this.value;
      this.insertTag(tag, subtags.length);
   }

   public void insertTag(Tag tag, int index) {
      if (this.type != Tag.Type.TAG_List && this.type != Tag.Type.TAG_Compound) {
         throw new RuntimeException();
      }

      Tag[] subtags = (Tag[])this.value;
      if (subtags.length > 0 && this.type == Tag.Type.TAG_List && tag.getType() != this.getListType()) {
         throw new IllegalArgumentException();
      }

      if (index > subtags.length) {
         throw new IndexOutOfBoundsException();
      }

      Tag[] newValue = new Tag[subtags.length + 1];
      System.arraycopy(subtags, 0, newValue, 0, index);
      newValue[index] = tag;
      System.arraycopy(subtags, index, newValue, index + 1, subtags.length - index);
      this.value = newValue;
   }

   public Tag removeTag(int index) {
      if (this.type != Tag.Type.TAG_List && this.type != Tag.Type.TAG_Compound) {
         throw new RuntimeException();
      }

      Tag[] subtags = (Tag[])this.value;
      Tag victim = subtags[index];
      Tag[] newValue = new Tag[subtags.length - 1];
      System.arraycopy(subtags, 0, newValue, 0, index);
      System.arraycopy(subtags, ++index, newValue, index - 1, subtags.length - index);
      this.value = newValue;
      return victim;
   }

   public void removeSubTag(Tag tag) {
      if (this.type != Tag.Type.TAG_List && this.type != Tag.Type.TAG_Compound) {
         throw new RuntimeException();
      }

      if (tag != null) {
         Tag[] subtags = (Tag[])this.value;

         for (int i = 0; i < subtags.length; i++) {
            if (subtags[i] == tag) {
               this.removeTag(i);
               return;
            }

            if (subtags[i].type == Tag.Type.TAG_List || subtags[i].type == Tag.Type.TAG_Compound) {
               subtags[i].removeSubTag(tag);
            }
         }
      }
   }

   public Tag findTagByName(String name) {
      return this.findNextTagByName(name, null);
   }

   public Tag findNextTagByName(String name, Tag found) {
      if (this.type != Tag.Type.TAG_List && this.type != Tag.Type.TAG_Compound) {
         return null;
      }

      Tag[] subtags = (Tag[])this.value;
      Tag[] var7 = subtags;
      int var6 = subtags.length;

      for (int var5 = 0; var5 < var6; var5++) {
         Tag subtag = var7[var5];
         if (subtag.name == null && name == null || subtag.name != null && subtag.name.equals(name)) {
            return subtag;
         }

         Tag newFound = subtag.findTagByName(name);
         if (newFound != null && newFound != found) {
            return newFound;
         }
      }

      return null;
   }

   public static Tag readFrom(InputStream is) throws IOException {
      DataInputStream dis = new DataInputStream(new GZIPInputStream(is));
      byte type = dis.readByte();
      Tag tag = null;
      if (type == 0) {
         tag = new Tag(Tag.Type.TAG_End, null, (Tag[])null);
      } else {
         tag = new Tag(Tag.Type.values()[type], dis.readUTF(), readPayload(dis, type));
      }

      dis.close();
      return tag;
   }

   private static Object readPayload(DataInputStream dis, byte type) throws IOException {
      switch (type) {
         case 0:
            return null;
         case 1:
            return dis.readByte();
         case 2:
            return dis.readShort();
         case 3:
            return dis.readInt();
         case 4:
            return dis.readLong();
         case 5:
            return dis.readFloat();
         case 6:
            return dis.readDouble();
         case 7:
            int length = dis.readInt();
            byte[] ba = new byte[length];
            dis.readFully(ba);
            return ba;
         case 8:
            return dis.readUTF();
         case 9:
            byte lt = dis.readByte();
            int ll = dis.readInt();
            Tag[] lo = new Tag[ll];

            for (int i = 0; i < ll; i++) {
               lo[i] = new Tag(Tag.Type.values()[lt], null, readPayload(dis, lt));
            }

            return lo.length == 0 ? Tag.Type.values()[lt] : lo;
         case 10:
            Tag[] tags = new Tag[0];

            byte stt;
            do {
               stt = dis.readByte();
               String name = null;
               if (stt != 0) {
                  name = dis.readUTF();
               }

               Tag[] newTags = new Tag[tags.length + 1];
               System.arraycopy(tags, 0, newTags, 0, tags.length);
               newTags[tags.length] = new Tag(Tag.Type.values()[stt], name, readPayload(dis, stt));
               tags = newTags;
            } while (stt != 0);

            return tags;
         default:
            return null;
      }
   }

   public void writeTo(OutputStream os) throws IOException {
      GZIPOutputStream gzos;
      DataOutputStream dos = new DataOutputStream(gzos = new GZIPOutputStream(os));
      dos.writeByte(this.type.ordinal());
      if (this.type != Tag.Type.TAG_End) {
         dos.writeUTF(this.name);
         this.writePayload(dos);
      }

      gzos.flush();
      gzos.close();
   }

   private void writePayload(DataOutputStream dos) throws IOException {
      switch (this.type) {
         case TAG_End:
         default:
            break;
         case TAG_Byte:
            dos.writeByte((Byte)this.value);
            break;
         case TAG_Short:
            dos.writeShort((Short)this.value);
            break;
         case TAG_Int:
            dos.writeInt((Integer)this.value);
            break;
         case TAG_Long:
            dos.writeLong((Long)this.value);
            break;
         case TAG_Float:
            dos.writeFloat((Float)this.value);
            break;
         case TAG_Double:
            dos.writeDouble((Double)this.value);
            break;
         case TAG_Byte_Array:
            byte[] ba = (byte[])this.value;
            dos.writeInt(ba.length);
            dos.write(ba);
            break;
         case TAG_String:
            dos.writeUTF((String)this.value);
            break;
         case TAG_List:
            Tag[] list = (Tag[])this.value;
            dos.writeByte(this.getListType().ordinal());
            dos.writeInt(list.length);
            Tag[] var14 = list;
            int var13 = list.length;

            for (int var12 = 0; var12 < var13; var12++) {
               Tag tt = var14[var12];
               tt.writePayload(dos);
            }
            break;
         case TAG_Compound:
            Tag[] subtags = (Tag[])this.value;
            Tag[] var8 = subtags;
            int var7 = subtags.length;

            for (int var6 = 0; var6 < var7; var6++) {
               Tag st = var8[var6];
               Tag subtag = st;
               Tag.Type type = subtag.getType();
               dos.writeByte(type.ordinal());
               if (type != Tag.Type.TAG_End) {
                  dos.writeUTF(subtag.getName());
                  subtag.writePayload(dos);
               }
            }
      }
   }

   public void print() {
      this.print(this, 0);
   }

   private String getTypeString(Tag.Type type) {
      switch (type) {
         case TAG_End:
            return "TAG_End";
         case TAG_Byte:
            return "TAG_Byte";
         case TAG_Short:
            return "TAG_Short";
         case TAG_Int:
            return "TAG_Int";
         case TAG_Long:
            return "TAG_Long";
         case TAG_Float:
            return "TAG_Float";
         case TAG_Double:
            return "TAG_Double";
         case TAG_Byte_Array:
            return "TAG_Byte_Array";
         case TAG_String:
            return "TAG_String";
         case TAG_List:
            return "TAG_List";
         case TAG_Compound:
            return "TAG_Compound";
         default:
            return null;
      }
   }

   private void indent(int indent) {
      for (int i = 0; i < indent; i++) {
         System.out.print("   ");
      }
   }

   private void print(Tag t, int indent) {
      Tag.Type type = t.getType();
      if (type != Tag.Type.TAG_End) {
         String name = t.getName();
         this.indent(indent);
         System.out.print(this.getTypeString(t.getType()));
         if (name != null) {
            System.out.print("(\"" + t.getName() + "\")");
         }

         if (type == Tag.Type.TAG_Byte_Array) {
            byte[] b = (byte[])t.getValue();
            System.out.println(": [" + b.length + " bytes]");
         } else if (type == Tag.Type.TAG_List) {
            Tag[] subtags = (Tag[])t.getValue();
            System.out.println(": " + subtags.length + " entries of type " + this.getTypeString(t.getListType()));
            Tag[] var9 = subtags;
            int var8 = subtags.length;

            for (int var7 = 0; var7 < var8; var7++) {
               Tag st = var9[var7];
               this.print(st, indent + 1);
            }

            this.indent(indent);
            System.out.println("}");
         } else if (type == Tag.Type.TAG_Compound) {
            Tag[] subtags = (Tag[])t.getValue();
            System.out.println(": " + (subtags.length - 1) + " entries");
            this.indent(indent);
            System.out.println("{");
            Tag[] var15 = subtags;
            int var14 = subtags.length;

            for (int var13 = 0; var13 < var14; var13++) {
               Tag st = var15[var13];
               this.print(st, indent + 1);
            }

            this.indent(indent);
            System.out.println("}");
         } else {
            System.out.println(": " + t.getValue());
         }
      }
   }

   public enum Type {
      TAG_End,
      TAG_Byte,
      TAG_Short,
      TAG_Int,
      TAG_Long,
      TAG_Float,
      TAG_Double,
      TAG_Byte_Array,
      TAG_String,
      TAG_List,
      TAG_Compound;
   }
}
