package org.selfbus.sbtools.prodedit.binding;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * Handles validations.
 */
public class ListValidationHandler<B>
{
//   private static final Logger LOGGER = LoggerFactory.getLogger(ValidationHandler.class);

   private final Map<String, ExtBufferedValueModel> models = new HashMap<String, ExtBufferedValueModel>();
   private final ValueChangeListener valueChangeListener = new ValueChangeListener();
   private final SelectionChangeListener selectionChangeListener = new SelectionChangeListener();
   private final ValueHolder valuesValid = new ValueHolder(false);
   private final DefaultValidationResultModel validationResultModel = new DefaultValidationResultModel();
   private final PresentationModel<B> subject;
   private SelectionInList<B> selectionInList;
   private final Validator<B> validator;
   private Container container;

   /**
    * Create a validation handler.
    *
    * @param subject - the presentation model to wrap
    * @param validator - the validator to use.
    * 
    * @see #setValidatedContainer(Container)
    */
   public ListValidationHandler(PresentationModel<B> subject, Validator<B> validator)
   {
      Validate.notNull(subject);
      Validate.notNull(validator);

      this.subject = subject;
      this.validator = validator;
   }

   /**
    * Set the component tree that contains the GUI elements that are validated.
    * This is usually the panel containing the elements.
    * 
    * @param container - the component tree root
    */
   public void setValidatedContainer(Container container)
   {
      this.container = container;
   }
  
   /**
    * Looks up and lazily creates a ValueModel that adapts
    * the bound property with the specified name.
    * 
    * @see PresentationModel#getModel(String)
    */
   public AbstractValueModel getModel(String propertyName)
   {
      ExtBufferedValueModel model = models.get(propertyName);
      if (model == null)
      {
         model = new ExtBufferedValueModel(subject.getModel(propertyName), valuesValid);
         model.addValueChangeListener(valueChangeListener);
         models.put(propertyName, model);
      }
      return model;
   }

   /**
    * @return The validation result model.
    */
   public ValidationResultModel getValidationResultModel()
   {
      return validationResultModel;
   }

   /**
    * Register the validation handler with the selectionInList object.
    * The handler then discards modified values when the current object changes.
    * 
    * @param selectionInList - the {@link SelectionInList} model to register with
    */
   public void observeSelectionChange(SelectionInList<B> selectionInList)
   {
      if (this.selectionInList != null)
         this.selectionInList.removeValueChangeListener(selectionChangeListener);

      this.selectionInList = selectionInList;
      
      if (selectionInList != null)
         selectionInList.addValueChangeListener(selectionChangeListener);
   }

   /**
    * A private listener resets the changed values when the selection changes.
    */
   private class SelectionChangeListener implements PropertyChangeListener
   {
      @Override
      public void propertyChange(PropertyChangeEvent e)
      {
         for (ExtBufferedValueModel model : models.values())
         {
            if (model.isBuffering())
               model.reset();
         }

         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               valueChangeListener.propertyChange(null);
            }
         });
      }
   }

   /**
    * A private listener that invokes the validation process when triggered.
    */
   private class ValueChangeListener implements PropertyChangeListener, ActionListener
   {
      Timer checkTimer = new Timer(250, this);

      ValueChangeListener()
      {
         checkTimer.setRepeats(false);
      }

      @Override
      public void propertyChange(PropertyChangeEvent e)
      {
         if (e == null || !ObjectUtils.equals(e.getOldValue(), e.getNewValue()))
         {
            checkTimer.stop();
            checkTimer.start();
         }
      }

      @Override
      public void actionPerformed(ActionEvent e)
      {
         ValidationResult result;
         if (selectionInList != null && selectionInList.hasSelection())
            result = validator.validate(selectionInList.getValue());
         else result = new ValidationResult();

         ValidationComponentUtils.updateComponentTreeSeverity(container, result);
         validationResultModel.setResult(result);
         valuesValid.setValue(!result.hasErrors());

         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               valuesValid.setValue(false);
            }
         });
      }
   }
}
