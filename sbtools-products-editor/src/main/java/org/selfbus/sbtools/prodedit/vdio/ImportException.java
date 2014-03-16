package org.selfbus.sbtools.prodedit.vdio;

/**
 * An exception for import problems.
 */
public class ImportException extends Exception
{
   private static final long serialVersionUID = 6275504542833327669L;

   public ImportException()
   {
      super();
   }

   public ImportException(String message)
   {
      super(message);
   }

   public ImportException(Throwable cause)
   {
      super(cause);
   }

   public ImportException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public ImportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
   {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
