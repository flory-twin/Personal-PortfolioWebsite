package kflory.web.portfolio.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.bson.types.Binary;

public class ImageImportUtility
{
   private static int defaultChunkSize = 4096;
   
   public static byte[] readBytesFromFilesystem(Path filePath)
         throws AccessDeniedException, NoSuchFileException, NotDirectoryException, IOException, Exception
   {
      byte[] bytes = new byte[0];
      
      try (FileChannel fileReadChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
         long fileByteCount = fileReadChannel.size();
         int chunkSize = -1;
         bytes = new byte[(int) fileByteCount];
         
         if (!(fileByteCount > 0))
         {
            throw new Exception("The passed image file was 0 bytes.");
         } else if (fileByteCount >= defaultChunkSize) {
            chunkSize = defaultChunkSize;
         } else {
            chunkSize = (int) fileByteCount;
         }
         
         ByteBuffer chunk = ByteBuffer.allocate(chunkSize);
         
         int numberOfBytesLastRead = fileReadChannel.read(chunk);
         try (ByteArrayOutputStream outBuffer = new ByteArrayOutputStream()) {
         
            while (numberOfBytesLastRead > 0)
            {
               // Store the last chunk, then pull the next chunk of bytes into the buffer.
               outBuffer.write(chunk.array());   
               chunk.clear();
               numberOfBytesLastRead = fileReadChannel.read(chunk);
            }
            
            //Convert output stream contents to org.bson.types.Binary.
            bytes = outBuffer.toByteArray();
            chunk.clear();
         }
      }
      
      return bytes;
   } 
   
   public static Binary readBinaryFromFilesystem(byte[] bytes) {
      //Convert output stream contents to org.bson.types.Binary.
      return new Binary(bytes);
   }
}
