package org.selfbus.sbtools.prodedit.tabs.project;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.text.JTextComponent;

import org.selfbus.sbtools.common.gui.actions.BasicAction;
import org.selfbus.sbtools.common.gui.components.CloseableComponent;
import org.selfbus.sbtools.common.gui.misc.ImageCache;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.actions.MoveNextSelectionInListAction;
import org.selfbus.sbtools.prodedit.actions.MovePrevSelectionInListAction;
import org.selfbus.sbtools.prodedit.actions.RemoveSelectionInListAction;
import org.selfbus.sbtools.prodedit.binding.ListValidationHandler;
import org.selfbus.sbtools.prodedit.internal.I18n;
import org.selfbus.sbtools.prodedit.model.AbstractProjectListener;
import org.selfbus.sbtools.prodedit.model.ProjectListener;
import org.selfbus.sbtools.prodedit.model.global.Language;
import org.selfbus.sbtools.prodedit.model.global.Project;
import org.selfbus.sbtools.prodedit.tabs.internal.CategoryElem;
import org.selfbus.sbtools.prodedit.utils.FontUtils;
import org.selfbus.sbtools.prodedit.vdio.LanguageMapper;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.BindingConverter;
import com.jgoodies.binding.value.ConverterValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 * An element that displays the project's {@link Language languages}.
 */
public class LanguagesElem implements CloseableComponent, CategoryElem
{
   private SelectionInList<Language> selectionInList = new SelectionInList<Language>(new LinkedList<Language>());
   private final PresentationModel<Language> detailsModel = new PresentationModel<Language>(selectionInList);
   private final Validator<Language> validator = new DetailsFormValidator();
   private final ListValidationHandler<Language> validationHandler = new ListValidationHandler<Language>(detailsModel, validator);

   @SuppressWarnings("unchecked")
   private final JList<Language> langList = BasicComponentFactory.createList(selectionInList);
   private final JScrollPane langScrollPane = new JScrollPane(langList);

   private final JPanel detailsPanel;
   private final JToolBar toolBar = new JToolBar();

   private final JTextComponent idField = BasicComponentFactory.createTextField(validationHandler.getModel("id"), false);
   private final JTextComponent nameField = BasicComponentFactory.createTextField(validationHandler.getModel("name"), false);
   private final JLabel etsIdField;

   /**
    * Create a {@link Language languages} display element.
    */
   public LanguagesElem()
   {
      FormLayout layout = new FormLayout("6dlu, left:pref, 4dlu, fill:pref:grow, 6dlu", 
         "8dlu, pref, 8dlu, pref, 4dlu, pref, 4dlu, pref, fill:pref:grow, pref, 4dlu");
      PanelBuilder builder = new PanelBuilder(layout);
      CellConstraints cc = new CellConstraints();
      
      builder.addLabel(I18n.getMessage("LanguagesElem.caption"), cc.rcw(2, 2, 3))
         .setFont(FontUtils.getCaptionFont());
      
      builder.addLabel(I18n.getMessage("LanguagesElem.id"), cc.rc(4, 2));
      builder.add(idField, cc.rc(4, 4));
      ValidationComponentUtils.setMandatoryBackground(idField);

      builder.addLabel(I18n.getMessage("LanguagesElem.name"), cc.rc(6, 2));
      builder.add(nameField, cc.rc(6, 4));
      ValidationComponentUtils.setMandatoryBackground(nameField);

      builder.addLabel(I18n.getMessage("LanguagesElem.etsId"), cc.rc(8, 2));
      etsIdField = BasicComponentFactory.createLabel(new ConverterValueModel(validationHandler.getModel("id"), etsIdValueConverter));
      builder.add(etsIdField, cc.rc(8, 4));

      builder.add(Box.createVerticalGlue(), cc.rcw(9, 2, 3));
      
      JComponent reportPane = ValidationResultViewFactory.createReportList(validationHandler.getValidationResultModel());
      builder.add(reportPane, cc.rcw(10, 2, 3));
      
      detailsPanel = builder.build();

      validationHandler.setValidatedContainer(detailsPanel);
      validationHandler.observeSelectionChange(selectionInList);

      ProdEdit.getInstance().getProjectService().addListener(projectListener);
      setupToolBar();
   }

   /**
    * Setup the tool bar.
    */
   private void setupToolBar()
   {
      //
      //  Action: add a language
      //
      toolBar.add(new BasicAction("add", I18n.getMessage("LanguagesElem.addTip"), ImageCache.getIcon("icons/add"))
      {
         private static final long serialVersionUID = 1;

         @Override
         public void actionEvent(ActionEvent event)
         {
            Project project = ProdEdit.getInstance().getProject();
            if (project != null)
            {
               Language newLang = project.createLanguage();
               selectionInList.setSelection(newLang);
               idField.requestFocus();
               idField.selectAll();
            }
         }
      });

      //
      //  Action: remove the current language
      //
      toolBar.add(new RemoveSelectionInListAction(selectionInList, I18n.getMessage("LanguagesElem.removeTip"))); 

      //
      //  Action: move the current value one step towards the beginning of the list.
      //
      toolBar.add(new MovePrevSelectionInListAction(selectionInList)); 

      //
      //  Action: move the current value one step towards the end of the list.
      //
      toolBar.add(new MoveNextSelectionInListAction(selectionInList)); 
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close()
   {
      ProdEdit.getInstance().getProjectService().removeListener(projectListener);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return I18n.getMessage("LanguagesElem.title");
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public JComponent getListPanel()
   {
      return langScrollPane;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public JComponent getDetailsPanel()
   {
      return detailsPanel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JToolBar getToolBar()
   {
      return toolBar;
   }

   /**
    * {@inheritDoc}
    */
   protected void updateContents()
   {
      final Project project = ProdEdit.getInstance().getProjectService().getProject();
      if (project == null)
         selectionInList.setList(new LinkedList<Language>());
      else selectionInList.setList(project.getLanguages());
   }

   /**
    * Private project listener.
    */
   private final ProjectListener projectListener = new AbstractProjectListener()
   {
      @Override
      public void projectChanged(Project project)
      {
         updateContents();
      }
   };

   /**
    * Private validator for details form input.
    */
   private class DetailsFormValidator implements Validator<Language>
   {
      @Override
      public ValidationResult validate(Language lang)
      {
         Project project = ProdEdit.getInstance().getProjectService().getProject();
         ValidationResult result = new ValidationResult();

         String id = idField.getText(); 
         if (id.length() != 2)
            result.addError(I18n.getMessage("LanguagesElem.errIdLength"), idField);
         else if (!id.matches("^\\w+$"))
            result.addError(I18n.getMessage("LanguagesElem.errIdInvalidChars"), idField);

         Language found = project.getLanguage(id);
         if (found != null && found != lang)
            result.addError(I18n.getMessage("LanguagesElem.errIdNotUnique"), idField);

         if (nameField.getText().isEmpty())
            result.addError(I18n.getMessage("LanguagesElem.errNameEmpty"), nameField);

         return result;
      }
   }

   /**
    * Converter that translates the language ID to the numeric ETS ID.
    */
   private final BindingConverter<String,String> etsIdValueConverter = new BindingConverter<String,String>()
   {
      @Override
      public String targetValue(String sourceValue)
      {
         if (sourceValue == null)
            return "";

         try
         {
            return Integer.toString(LanguageMapper.getEtsId(sourceValue));
         }
         catch (IllegalArgumentException e)
         {
            return I18n.getMessage("LanguagesElem.unknownEtsId");
         }
      }
      
      @Override
      public String sourceValue(String targetValue)
      {
         throw new IllegalAccessError("This converter is read-only");
      }
   };
}
