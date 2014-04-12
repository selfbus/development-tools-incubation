package org.selfbus.sbtools.prodedit.utils;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.Validate;
import org.selfbus.sbtools.prodedit.ProdEdit;
import org.selfbus.sbtools.prodedit.model.common.MultiLingualText;
import org.selfbus.sbtools.prodedit.model.global.Language;
import org.selfbus.sbtools.prodedit.model.global.Project;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Utility methods for handling {@link MultiLingualText}s.
 */
public final class MultiLingualTextUtil
{
   /**
    * Create GUI components for entering the values of a {@link MultiLingualText}. The created GUI
    * components are inserted into the builder's layout at the specified row. Rows are added to the
    * layout for the inserted components.
    * 
    * @param builder - the panel builder for creating the GUI components
    * @param row - the row in the layout for the first GUI component
    *
    * @return A map with the language ID as key and the property adapter for entering the text as
    *         value.
    */
   public static Map<String, PropertyAdapter<MultiLingualText.Element>> createFormElements(PanelBuilder builder, int row)
   {
      FormLayout layout = builder.getLayout();
      Validate.notNull(layout, "builder has no layout set");

      Map<String, PropertyAdapter<MultiLingualText.Element>> labelElems = new HashMap<String, PropertyAdapter<MultiLingualText.Element>>();
      Project project = ProdEdit.getInstance().getProject();
      ArrayListModel<Language> langs = project.getLanguages();
      String defaultLangId = project.getDefaultLangId();
      CellConstraints cc = new CellConstraints();

      MultiLingualText.Element noElem = new MultiLingualText.Element();
      for (Language lang : langs)
      {
         layout.insertRow(row++, RowSpec.decode("2dlu"));
         layout.insertRow(row, RowSpec.decode("p"));

         PropertyAdapter<MultiLingualText.Element> labelValue = new PropertyAdapter<MultiLingualText.Element>(noElem,
            "text");
         labelElems.put(lang.getId(), labelValue);

         JTextComponent nameField = BasicComponentFactory.createTextField(labelValue, false);

         boolean isDefault = lang.getId().equals(defaultLangId);
         String name = " " + lang.getName();
         if (isDefault)
            name += '*';

         builder.addLabel(name, cc.rc(row, 2));
         builder.add(nameField, cc.rc(row, 4));

         row++;
      }

      layout.invalidateLayout(builder.getPanel());

      return labelElems;
   }
}
