package net.disy.commons.swing.events;

import java.awt.datatransfer.FlavorListener;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionListener;
import net.disy.commons.core.model.listener.IChangeListener;

public interface ICheckInputValidListener
   extends ActionListener,
   ItemListener,
   ListSelectionListener,
   DocumentListener,
   IChangeListener,
   ChangeListener,
   TableModelListener,
   TreeSelectionListener,
   PropertyChangeListener,
   FlavorListener,
   ListDataListener {
   void checkInputValid();
}
