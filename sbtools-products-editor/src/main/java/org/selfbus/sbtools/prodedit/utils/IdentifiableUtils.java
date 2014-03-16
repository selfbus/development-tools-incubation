package org.selfbus.sbtools.prodedit.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.selfbus.sbtools.prodedit.model.interfaces.Identifiable;

/**
 * Utilities for objects that implement the {@link Identifiable} interface. 
 */
public final class IdentifiableUtils
{
   /**
    * Create a new unique ID > 0 that does not exist in the given list.
    * Returns 1 if the list is null.
    * 
    * @param list - the list to scan for existing IDs, may be null.
    * @return the created ID.
    */
   public static int createUniqueId(Collection<? extends Identifiable> list)
   {
      Set<Integer> used = new HashSet<Integer>(list.size() << 1);
      int maxId = 1;

      for (Identifiable o : list)
      {
         used.add(o.getId());
         maxId = Math.max(maxId, o.getId());
      }

      ++maxId;
      for (int tries = 0; tries < 100; ++tries)
      {
         int id = 1 + (int) (Math.random() * maxId);

         if (!used.contains(id))
            return id;
      }

      return maxId;
   }

   /**
    * Get the maximum ID that exists in the given list.
    * 
    * @param list - the list to scan for existing IDs.
    * @return the maximum ID or 0 if the list is empty.
    */
   public static int findMaxId(Collection<? extends Identifiable> list)
   {
      int maxId = 0;

      for (Identifiable o : list)
         maxId = Math.max(maxId, o.getId());

      return maxId;
   }

   /**
    * Find the {@link Identifiable} with the given ID.
    *
    * @param id - the ID to search
    * @return The first {@link Identifiable} with the ID, or null if not found.
    */
   public static Identifiable findById(Collection<? extends Identifiable> list, int id)
   {
      for (Identifiable o : list)
      {
         if (o.getId() == id)
            return o;
      }

      return null;
   }
}
